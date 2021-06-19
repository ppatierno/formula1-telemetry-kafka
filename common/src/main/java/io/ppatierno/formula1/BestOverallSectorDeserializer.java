/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BestOverallSectorDeserializer implements Deserializer<BestOverallSector> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public BestOverallSector deserialize(String topic, byte[] bytes) {
        return deserialize(topic, null, bytes);
    }

    @Override
    public BestOverallSector deserialize(String topic, Headers headers, byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            short sector = buffer.getShort();
            int driverSize = buffer.getInt();
            byte[] driverBytes = new byte[driverSize];
            buffer.get(driverBytes);
            String driver = new String(driverBytes, StandardCharsets.UTF_8);
            int timeInMs = buffer.getInt();
            short lapNum = buffer.getShort();
            return new BestOverallSector(sector, driver, timeInMs, lapNum);
        }
    }

    @Override
    public void close() {

    }
}
