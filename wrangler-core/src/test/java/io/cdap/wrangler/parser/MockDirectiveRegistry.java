/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 package io.cdap.wrangler.parser;
 import io.cdap.cdap.api.artifact.ArtifactSummary;
 import io.cdap.directives.aggregates.AggregateStats;
 import io.cdap.wrangler.api.DirectiveLoadException;
 import io.cdap.wrangler.registry.DirectiveInfo;
 import io.cdap.wrangler.registry.DirectiveRegistry;
 import java.io.Closeable;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Map;


 
 
 /**
  * A mock implementation of the DirectiveRegistry for testing purposes.
  */
 public class MockDirectiveRegistry implements DirectiveRegistry, Closeable {
 
   private final Map<String, DirectiveInfo> directives = new HashMap<>();
 
   public MockDirectiveRegistry() {
     try {
       // Register the aggregate-stats directive using fromSystem
       directives.put("aggregate-stats", DirectiveInfo.fromSystem(AggregateStats.class));
     } catch (Exception e) {
       throw new RuntimeException("Failed to register mock directive", e);
     }
   }
 
   @Override
   public Iterable<DirectiveInfo> list(String namespace) {
     return directives.values();
   }
 
   @Override
   public DirectiveInfo get(String namespace, String name) throws DirectiveLoadException {
     return directives.get(name);
   }
 
   @Override
   public void reload(String namespace) throws DirectiveLoadException {
     // No-op in mock
   }
 
   @Override
   public ArtifactSummary getLatestWranglerArtifact() {
     return null;
   }
 
   @Override
   public void close() {
     // No cleanup required for mock
   }
 }
 
