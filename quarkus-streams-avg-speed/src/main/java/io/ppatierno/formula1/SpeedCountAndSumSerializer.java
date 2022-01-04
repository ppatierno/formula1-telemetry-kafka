/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class SpeedCountAndSumSerializer implements Serializer<SpeedCountAndSum> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, SpeedCountAndSum data) {
        return serialize(topic, null, data);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, SpeedCountAndSum data) {
        ByteBuf bb = Unpooled.buffer(4 + 4);
        bb.writeInt(data.getCount());
        bb.writeInt(data.getSum());
        return bb.array();
    }

    @Override
    public void close() {

    }
}
