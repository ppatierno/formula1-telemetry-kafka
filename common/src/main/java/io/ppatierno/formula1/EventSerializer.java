/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.ppatierno.formula1.data.*;
import io.ppatierno.formula1.packets.PacketEventData;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class EventSerializer implements Serializer<Event> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Event event) {
        return serialize(topic, null, event);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Event event) {
        ByteBuf bb = Unpooled.buffer(this.rawBytesSize(event));
        // some events don't have participant data, so it won't be serialized
        if (event.getParticipantData() != null) {
            event.getParticipantData().fillBuffer(bb);
        }
        event.getEventData().fillBuffer(bb);
        return bb.array();
    }

    @Override
    public void close() {

    }

    /**
     * Get the raw size in bytes of an Event with the related non-null serialized field
     *
     * @param event Event instance to determinate the size in bytes
     * @return the raw size in bytes of the Driver instance
     */
    public int rawBytesSize(Event event) {
        int size = 0;
        size += event.getParticipantData() != null ? ParticipantData.SIZE : 0;
        size += event.getEventData() != null ? PacketEventData.SIZE : 0;
        return size;
    }
}
