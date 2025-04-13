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

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * A parser class that handles time duration values like "5ms", "2.1s", etc.
 */
public class TimeDuration implements Token {
  private final long milliseconds;
  private final String rawValue;

  public TimeDuration(String rawValue) {
    this.rawValue = rawValue;
    this.milliseconds = parseToMillis(rawValue);
  }

  private long parseToMillis(String input) {
    String unit = input.replaceAll("[0-9.]+", "").toLowerCase();
    double number = Double.parseDouble(input.replaceAll("[^0-9.]", ""));
    switch (unit) {
      case "ms": return (long) number;
      case "s": return (long) (number * 1000);
      case "m": return (long) (number * 60 * 1000);
      case "h": return (long) (number * 60 * 60 * 1000);
      default: throw new IllegalArgumentException("Unsupported time unit: " + unit);
    }
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  @Override
  public Object value() {
    return milliseconds;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(milliseconds);
  }
}
