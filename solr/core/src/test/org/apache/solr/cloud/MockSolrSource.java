/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.cloud;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.cloud.ZkStateReader;

public class MockSolrSource {

  @SuppressWarnings("DirectInvocationOnMock")
  public static ZkController makeSimpleMock(
      Overseer overseer, ZkStateReader reader, SolrZkClient zkClient) {
    ZkController zkControllerMock = mock(ZkController.class);
    final DistributedClusterStateUpdater distributedClusterStateUpdater;
    if (overseer == null) {
      // When no overseer is passed, the Overseer queue does nothing. Replicate this in how we
      // handle distributed state updates by doing nothing as well...
      distributedClusterStateUpdater = mock(DistributedClusterStateUpdater.class);
      overseer = mock(Overseer.class);
      when(overseer.getDistributedClusterStateUpdater()).thenReturn(distributedClusterStateUpdater);
    } else {
      // Use the same configuration for state updates as the Overseer.
      distributedClusterStateUpdater = overseer.getDistributedClusterStateUpdater();
    }

    if (reader != null && zkClient == null) {
      zkClient = reader.getZkClient();
    } else {
      reader = mock(ZkStateReader.class);
      when(reader.getZkClient()).thenReturn(zkClient);
    }

    when(zkControllerMock.getOverseer()).thenReturn(overseer);
    when(zkControllerMock.getZkStateReader()).thenReturn(reader);
    when(zkControllerMock.getZkClient()).thenReturn(zkClient);
    when(zkControllerMock.getOverseer()).thenReturn(overseer);
    when(zkControllerMock.getDistributedClusterStateUpdater())
        .thenReturn(distributedClusterStateUpdater);
    return zkControllerMock;
  }
}
