/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.serializers;

import io.ppatierno.formula1.PacketEncoder;
import io.ppatierno.formula1.packets.Packet;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class PacketSerializer implements Serializer<Packet> {

    PacketEncoder encoder = new PacketEncoder();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Packet packet) {
        return serialize(topic, null, packet);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Packet packet) {
        return this.encoder.encode(packet).array();
    }

    @Override
    public void close() {

    }
}
