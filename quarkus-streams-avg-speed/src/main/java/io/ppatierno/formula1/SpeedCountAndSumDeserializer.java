/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class SpeedCountAndSumDeserializer implements Deserializer<SpeedCountAndSum> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public SpeedCountAndSum deserialize(String topic, byte[] data) {
        return deserialize(topic, null, data);
    }

    @Override
    public SpeedCountAndSum deserialize(String topic, Headers headers, byte[] data) {
        ByteBuf bb = Unpooled.wrappedBuffer(data);
        int count = bb.readInt();
        int sum = bb.readInt();
        return new SpeedCountAndSum(count, sum);
    }

    @Override
    public void close() {

    }
}
