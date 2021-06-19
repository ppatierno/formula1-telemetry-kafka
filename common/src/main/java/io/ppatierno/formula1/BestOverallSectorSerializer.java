/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BestOverallSectorSerializer implements Serializer<BestOverallSector> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, BestOverallSector bestOverallSector) {
        return serialize(topic, null, bestOverallSector);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, BestOverallSector bestOverallSector) {
        byte[] serializedDriver;
        if (bestOverallSector == null) {
            return null;
        } else {
            if (bestOverallSector.getDriver() != null) {
                serializedDriver = bestOverallSector.getDriver().getBytes(StandardCharsets.UTF_8);
            } else {
                serializedDriver = new byte[0];
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(2 + 2 + 4 + 4 + serializedDriver.length);
        buffer.putShort(bestOverallSector.getSector());
        buffer.putInt(serializedDriver.length);
        buffer.put(serializedDriver);
        buffer.putInt(bestOverallSector.getTimeInMs());
        buffer.putShort(bestOverallSector.getLapNum());
        return buffer.array();
    }

    @Override
    public void close() {

    }
}
