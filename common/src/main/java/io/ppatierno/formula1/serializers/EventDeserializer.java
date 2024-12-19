/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.serializers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.ppatierno.formula1.data.ParticipantData;
import io.ppatierno.formula1.model.Event;
import io.ppatierno.formula1.packets.PacketEventData;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class EventDeserializer implements Deserializer<Event> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Event deserialize(String topic, byte[] bytes) {
        return deserialize(topic, null, bytes);
    }

    @Override
    public Event deserialize(String topic, Headers headers, byte[] bytes) {
        ByteBuf bb = Unpooled.wrappedBuffer(bytes);
        ParticipantData participantData = null;
        // check if the raw bytes contains the participant data, depending the kind of event to deserialize
        if (bytes.length > PacketEventData.SIZE) {
            participantData = new ParticipantData().fill(bb);
        }
        return new Event(participantData, (PacketEventData) new PacketEventData().fill(bb));
    }

    @Override
    public void close() {

    }
}
