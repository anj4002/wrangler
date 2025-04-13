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
 import io.cdap.wrangler.api.DirectiveContext;
 import java.util.HashMap;
 import java.util.Map;


 
 
 /**
  * A mock DirectiveContext for testing.
  */
 public class MockDirectiveContext implements DirectiveContext {
 
   private final Map<String, Object> store = new HashMap<>();
 

   public <T> T get(String name) {
     return (T) store.get(name);
   }
 
   public void set(String name, Object value) {
     store.put(name, value);
   }
 
   @Override
   public boolean isExcluded(String directiveName) {
     return false; // For testing, assume no directive is excluded
   }
 
   @Override
   public boolean hasAlias(String directiveName) {
     return false; // No aliases in test
   }
 
   @Override
   public String getAlias(String directiveName) {
     return directiveName; // Return same as input for simplicity
   }
 }
 
