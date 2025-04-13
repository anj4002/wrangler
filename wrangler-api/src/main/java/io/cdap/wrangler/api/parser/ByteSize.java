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
 * A parser class that handles byte size values like "10KB", "1.5MB", etc.
 */
public class ByteSize implements Token {
  private final long bytes;
  private final String rawValue;

  public ByteSize(String rawValue) {
    this.rawValue = rawValue;
    this.bytes = parseToBytes(rawValue);
  }

  private long parseToBytes(String input) {
    String unit = input.replaceAll("[0-9.]+", "").toUpperCase();
    double number = Double.parseDouble(input.replaceAll("[^0-9.]", ""));
    switch (unit) {
      case "KB": return (long) (number * 1024);
      case "MB": return (long) (number * 1024 * 1024);
      case "GB": return (long) (number * 1024 * 1024 * 1024);
      case "TB": return (long) (number * 1024L * 1024 * 1024 * 1024);
      default: throw new IllegalArgumentException("Unsupported byte unit: " + unit);
    }
  }

  public long getBytes() {
    return bytes;
  }

  @Override
  public Object value() {
    return bytes;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(bytes);
  }
}
