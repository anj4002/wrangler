/*
 * Copyright © 2017-2019 Cask Data, Inc.
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

 package io.cdap.directives.aggregates;

 import io.cdap.wrangler.api.Arguments;
 import io.cdap.wrangler.api.Directive;
 import io.cdap.wrangler.api.DirectiveArgument;
 import io.cdap.wrangler.api.ExecutorContext;
 import io.cdap.wrangler.api.parser.ByteSize;
 import io.cdap.wrangler.api.parser.TokenType;
 import io.cdap.wrangler.api.parser.TimeDuration;
 import io.cdap.wrangler.api.parser.UsageDefinition;
 import io.cdap.wrangler.api.Row;
 
 import java.util.ArrayList;
 import java.util.List;
 
 /**
  * Directive to aggregate byte sizes and time durations
  * across rows and produce summarized outputs.
  */
 public final class AggregateStats implements Directive {
 
     /** Column containing byte size input */
     private String byteSizeColumn;
 
     /** Column containing time duration input */
     private String timeColumn;
 
     /** Output column for byte size aggregation */
     private String outputByteColumn;
 
     /** Output column for time aggregation */
     private String outputTimeColumn;
 
     /** Desired output unit for byte size */
     private String outputSizeUnit = "MB";
 
     /** Desired output unit for time */
     private String outputTimeUnit = "seconds";
 
     /** Aggregation strategy - total or average */
     private String aggregationType = "total";
 
     /** Total accumulated byte count */
     private long totalBytes = 0;
 
     /** Total accumulated time in nanoseconds */
     private long totalNanos = 0;
 
     /** Row count */
     private long count = 0;
 
     @Override
     public UsageDefinition define() {
         return UsageDefinition.builder("aggregate-stats")
                 .add("byte_col", TokenType.BYTE_SIZE)
                 .add("time_col", TokenType.TIME_DURATION)
                 .add("out_byte_col", TokenType.TEXT)
                 .add("out_time_col", TokenType.TEXT)
                 .build();
     }
 
     public void initialize(final ExecutorContext context, final List<DirectiveArgument> args) {
         this.byteSizeColumn = args.get(0).value().toString();
         this.timeColumn = args.get(1).value().toString();
         this.outputByteColumn = args.get(2).value().toString();
         this.outputTimeColumn = args.get(3).value().toString();
     }
 
     @Override
     public void initialize(final Arguments args) {
         if (args.contains("outputSizeUnit")) {
             outputSizeUnit = args.value("outputSizeUnit");
         }
         if (args.contains("outputTimeUnit")) {
             outputTimeUnit = args.value("outputTimeUnit");
         }
         if (args.contains("aggregationType")) {
             aggregationType = args.value("aggregationType");
         }
     }
 
     @Override
     public List<Row> execute(final List<Row> rows, final ExecutorContext context) {
         for (Row row : rows) {
             Object byteVal = row.getValue(byteSizeColumn);
             Object timeVal = row.getValue(timeColumn);
 
             if (byteVal != null) {
                 try {
                     ByteSize size = new ByteSize(byteVal.toString());
                     totalBytes += size.getBytes();
                 } catch (Exception e) {
                     throw new RuntimeException("Invalid byte size format: " + byteVal, e);
                 }
             }
 
             if (timeVal != null) {
                 try {
                     TimeDuration duration = new TimeDuration(timeVal.toString());
                     totalNanos += duration.getMilliseconds() * 1_000_000;
                 } catch (Exception e) {
                     throw new RuntimeException("Invalid time duration format: " + timeVal, e);
                 }
             }
 
             count++;
         }
 
         double finalSize = convertBytes(totalBytes, outputSizeUnit);
         double finalTime = aggregationType.equalsIgnoreCase("average")
                 ? convertTime(totalNanos / (double) count, outputTimeUnit)
                 : convertTime(totalNanos, outputTimeUnit);
 
         Row output = new Row();
         output.add(outputByteColumn, finalSize);
         output.add(outputTimeColumn, finalTime);
 
         List<Row> result = new ArrayList<>();
         result.add(output);
         return result;
     }
 
     /**
      * Converts byte value into specified unit.
      */
     private double convertBytes(final long bytes, final String unit) {
         switch (unit.toUpperCase()) {
             case "KB": return bytes / 1024.0;
             case "MB": return bytes / (1024.0 * 1024);
             case "GB": return bytes / (1024.0 * 1024 * 1024);
             default: return bytes;
         }
     }
 
     /**
      * Converts nanosecond value into specified unit.
      */
     private double convertTime(final double nanos, final String unit) {
         switch (unit.toLowerCase()) {
             case "ms": return nanos / 1_000_000.0;
             case "s":
             case "seconds": return nanos / 1_000_000_000.0;
             case "minutes": return nanos / (60.0 * 1_000_000_000);
             default: return nanos;
         }
     }
 
     @Override
     public void destroy() {
         // No cleanup required
     }
 }
 