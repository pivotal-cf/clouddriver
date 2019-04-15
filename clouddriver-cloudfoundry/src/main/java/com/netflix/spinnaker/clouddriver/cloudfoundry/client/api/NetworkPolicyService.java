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

package com.netflix.spinnaker.clouddriver.cloudfoundry.client.api;

import com.netflix.spinnaker.clouddriver.cloudfoundry.client.model.networking.NetworkPolicy;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import java.util.List;

public interface NetworkPolicyService {
  @GET("/networking/v1/external/policies")
  NetworkPolicy all(@Query("id") List<String> listOfAppGuids);

  @POST("/networking/v1/external/policies")
  NetworkPolicy createNetworkPolicy(@Body NetworkPolicy networkPolicy);

  @POST("/networking/v1/external/policies/delete")
  Response delete(@Body NetworkPolicy networkPolicy);
}
