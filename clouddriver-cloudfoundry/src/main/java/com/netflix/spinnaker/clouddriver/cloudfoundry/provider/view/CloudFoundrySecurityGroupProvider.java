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

package com.netflix.spinnaker.clouddriver.cloudfoundry.provider.view;

import com.netflix.spinnaker.clouddriver.cloudfoundry.CloudFoundryCloudProvider;
import com.netflix.spinnaker.clouddriver.cloudfoundry.model.CloudFoundrySecurityGroup;
import com.netflix.spinnaker.clouddriver.model.SecurityGroupProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@Component
public class CloudFoundrySecurityGroupProvider implements SecurityGroupProvider<CloudFoundrySecurityGroup> {
  @Override
  public String getCloudProvider() {
    return CloudFoundryCloudProvider.ID;
  }

  @Override
  public Collection<CloudFoundrySecurityGroup> getAll(boolean includeRules) {
    return null;
  }

  @Override
  public Collection<CloudFoundrySecurityGroup> getAllByRegion(boolean includeRules, String region) {
    return null;
  }

  @Override
  public Collection<CloudFoundrySecurityGroup> getAllByAccount(boolean includeRules, String account) {
    return null;
  }

  @Override
  public Collection<CloudFoundrySecurityGroup> getAllByAccountAndName(boolean includeRules, String account, String name) {
    return null;
  }

  @Override
  public Collection<CloudFoundrySecurityGroup> getAllByAccountAndRegion(boolean includeRule, String account, String region) {
    return null;
  }

  @Override
  public CloudFoundrySecurityGroup get(String account, String region, String name, String vpcId) {
    return null;
  }
}
