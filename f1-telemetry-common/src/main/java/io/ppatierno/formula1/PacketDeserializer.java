/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.ppatierno.formula1.packets.Packet;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class PacketDeserializer implements Deserializer<Packet> {

    PacketDecoder decoder = new PacketDecoder();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Packet deserialize(String topic, byte[] bytes) {
        return null;
    }

    @Override
    public Packet deserialize(String topic, Headers headers, byte[] bytes) {
        ByteBuf bb = Unpooled.wrappedBuffer(bytes);
        return this.decoder.decode(bb);
    }

    @Override
    public void close() {

    }
}
