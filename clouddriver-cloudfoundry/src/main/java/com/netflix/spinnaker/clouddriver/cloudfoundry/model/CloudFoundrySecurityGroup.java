/*
 * Copyright 2019 Pivotal, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.cloudfoundry.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netflix.spinnaker.clouddriver.cloudfoundry.CloudFoundryCloudProvider;
import com.netflix.spinnaker.clouddriver.model.SecurityGroup;
import com.netflix.spinnaker.clouddriver.model.SecurityGroupSummary;
import com.netflix.spinnaker.clouddriver.model.securitygroups.Rule;
import com.netflix.spinnaker.moniker.Moniker;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;
import java.util.Set;

@Value
@EqualsAndHashCode(of = "id", callSuper = false)
@Builder
@JsonDeserialize(builder = CloudFoundrySecurityGroup.CloudFoundrySecurityGroupBuilder.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CloudFoundrySecurityGroup implements SecurityGroup {
  final String type = CloudFoundryCloudProvider.ID;
  final String cloudProvider = CloudFoundryCloudProvider.ID;
  final String id;
  final String name;
  final String application;
  final String destinationApplication;
  final String accountName;
  final Set<Rule> outboundRules;

  @Override
  public Moniker getMoniker() {
    return null;
  }

  @Override
  public String getRegion() {
    return null;
  }

  @Override
  public Set<Rule> getInboundRules() {
    return null;
  }

  @Override
  public SecurityGroupSummary getSummary() {
    return new CloudFoundrySecurityGroupSummary( name, id);
  }

  @Override
  public Map<String, String> getLabels() {
    return null;
  }
}
