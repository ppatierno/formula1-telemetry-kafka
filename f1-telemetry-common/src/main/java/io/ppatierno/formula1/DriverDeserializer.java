/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.ppatierno.formula1.data.ParticipantData;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class DriverDeserializer implements Deserializer<Driver> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Driver deserialize(String topic, byte[] bytes) {
        return deserialize(topic, null, bytes);
    }

    @Override
    public Driver deserialize(String topic, Headers headers, byte[] bytes) {
        ByteBuf bb = Unpooled.wrappedBuffer(bytes);
        ParticipantData participantData = new ParticipantData();
        participantData.fill(bb);
        Driver driver = new Driver(participantData);
        // TODO: deserialize other fields
        return driver;
    }

    @Override
    public void close() {

    }
}
