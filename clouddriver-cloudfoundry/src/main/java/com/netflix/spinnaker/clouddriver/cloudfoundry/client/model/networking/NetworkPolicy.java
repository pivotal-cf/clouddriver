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

package com.netflix.spinnaker.clouddriver.cloudfoundry.client.model.networking;

import lombok.Data;

import javax.annotation.Nullable;
import java.util.List;

@Data
public class NetworkPolicy {
  @Nullable
  private final long totalPolicies;
  private final List<Policy> policies;

  @Data
  public static class Policy {
    private final Source source;
    private final Destination destination;

    @Data
    public static class Source {
      private final String id;
    }

    @Data
    public static class Destination {
      private final String id;
      private final String protocol;
      private final Ports ports;

      @Data
      public static class Ports {
        private final int start;
        private final int end;
      }
    }
  }

}
