/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.ppatierno.formula1.data.ParticipantData;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class DriverSerializer implements Serializer<Driver> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Driver driver) {
        return serialize(topic, null, driver);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Driver driver) {
        ByteBuf bb = Unpooled.buffer(ParticipantData.SIZE);
        driver.getParticipantData().fillBuffer(bb);
        // TODO: serialize other fields
        return bb.array();
    }

    @Override
    public void close() {

    }
}
