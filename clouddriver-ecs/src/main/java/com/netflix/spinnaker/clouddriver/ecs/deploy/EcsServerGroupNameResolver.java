/*
 * Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under
 * the License.
 */

package com.netflix.spinnaker.clouddriver.ecs.deploy;

import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.model.*;
import com.google.common.collect.Lists;
import com.netflix.frigga.Names;
import com.netflix.spinnaker.clouddriver.helpers.AbstractServerGroupNameResolver;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EcsServerGroupNameResolver extends AbstractServerGroupNameResolver {
  private static final String PHASE = "ECS_DEPLOY";
  private static final int MAX_NEXT_SERVER_GROUP_ATTEMPTS = 5;

  String ecsClusterName;
  AmazonECS ecs;
  String region;

  @Override
  public String getPhase() {
    return PHASE;
  }

  @Override
  public String getRegion() {
    return region;
  }

  @Override
  public List<TakenSlot> getTakenSlots(String familyName) {
    List<String> relevantServices = new ArrayList<>();
    String nextToken = null;
    do {
      ListServicesRequest request = new ListServicesRequest().withCluster(ecsClusterName);
      if (nextToken != null) {
        request.setNextToken(nextToken);
      }

      ListServicesResult result = ecs.listServices(request);
      for (String serviceArn : result.getServiceArns()) {
        if (serviceArn.contains(familyName)) {
          relevantServices.add(serviceArn);
        }
      }

      nextToken = result.getNextToken();
    } while (nextToken != null && nextToken.length() != 0);

    List<TakenSlot> slots = new ArrayList<>();
    List<List<String>> serviceBatches = Lists.partition(relevantServices, 10);
    for (List<String> serviceBatch : serviceBatches) {
      DescribeServicesRequest request =
          new DescribeServicesRequest().withCluster(ecsClusterName).withServices(serviceBatch);
      DescribeServicesResult result = ecs.describeServices(request);
      for (Service service : result.getServices()) {
        Names names = Names.parseName(service.getServiceName());
        slots.add(
            new TakenSlot(service.getServiceName(), names.getSequence(), service.getCreatedAt()));
      }
    }

    return slots;
  }

  @Override
  public String resolveNextServerGroupName(
      String application, String stack, String details, Boolean ignoreSequence) {
    String originalNextServerGroupName =
        super.resolveNextServerGroupName(application, stack, details, ignoreSequence);
    String nextServerGroupName = originalNextServerGroupName;
    String clusterName = combineAppStackDetail(application, stack, details);

    boolean nextServerGroupNameAlreadyExists = true;
    int attempts = 0;
    while (nextServerGroupNameAlreadyExists && attempts++ <= MAX_NEXT_SERVER_GROUP_ATTEMPTS) {
      // An ECS service with this name might exist already in "Draining" state,
      // so it would not show up in the "taken slots" list.
      // We need to describe it to determine if it does exist before using the name
      DescribeServicesRequest request =
          new DescribeServicesRequest()
              .withCluster(ecsClusterName)
              .withServices(nextServerGroupName);
      DescribeServicesResult result = ecs.describeServices(request);

      if (result.getServices().isEmpty()
          || result.getServices().get(0).getStatus().equals("INACTIVE")) {
        // an active or draining ECS service with this name was not found
        nextServerGroupNameAlreadyExists = false;
        break;
      }

      int nextSequence = generateNextSequence(nextServerGroupName);
      nextServerGroupName =
          generateServerGroupName(application, stack, details, nextSequence, false);
    }

    if (nextServerGroupNameAlreadyExists) {
      throw new IllegalArgumentException(
          "All server group names for cluster " + clusterName + " in " + region + " are taken.");
    }

    return nextServerGroupName;
  }

  public static String getEcsFamilyName(String serverGroupName) {
    // Format: family-name-v001
    return serverGroupName.substring(0, serverGroupName.lastIndexOf('-'));
  }

  public static String getEcsContainerName(String serverGroupName) {
    // Format: family-name-v001, container name = v001
    return serverGroupName.substring(
        serverGroupName.lastIndexOf('-') + 1, serverGroupName.length());
  }
}
