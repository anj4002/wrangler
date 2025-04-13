/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

 package io.cdap.wrangler.parser;
 import io.cdap.wrangler.api.parser.ByteSize;
 import io.cdap.wrangler.api.parser.TimeDuration;
 import org.junit.Assert;
 import org.junit.Test;
 
 
 
 
 public class ByteSizeAndTimeDurationTest {
     
     @Test
   public void testByteSizeParsing() {
     Assert.assertEquals(10240L, new ByteSize("10kb").getBytes());
     Assert.assertEquals(1572864L, new ByteSize("1.5MB").getBytes());
     Assert.assertEquals(1073741824L, new ByteSize("1GB").getBytes());
     Assert.assertEquals(42L, new ByteSize("42").getBytes());
   }
 
   @Test
   public void testTimeDurationParsing() {
     Assert.assertEquals(5L, new TimeDuration("5ms").getMilliseconds());
     Assert.assertEquals(2100L, new TimeDuration("2.1s").getMilliseconds());
     Assert.assertEquals(1000L, new TimeDuration("1s").getMilliseconds());
     Assert.assertEquals(123L, new TimeDuration("123").getMilliseconds());
   }
 }
 
