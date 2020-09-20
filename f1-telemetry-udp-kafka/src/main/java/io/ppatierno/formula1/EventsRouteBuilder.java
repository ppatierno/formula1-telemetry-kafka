/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.enums.EventCode;
import io.ppatierno.formula1.enums.PacketId;
import io.ppatierno.formula1.packets.Packet;
import io.ppatierno.formula1.packets.PacketEventData;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Route getting raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast,
 * filtering them to select the EVENT only and sending them to Kafka
 */
public class EventsRouteBuilder extends RouteBuilder  {

    private Session session;

    public EventsRouteBuilder(Session session) {
        this.session = session;
    }

    @Override
    public void configure() throws Exception {
        // get raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast
        from("direct:raw-events")
        .filter(exchange -> {
            Packet packet = (Packet) exchange.getIn().getBody();
            return packet.getHeader().getPacketId() == PacketId.EVENT;
        })
        .process(exchange -> {
            PacketEventData packetEventData = (PacketEventData) exchange.getIn().getBody();
            this.session.updateEventData(packetEventData);
        })
        .to("kafka:f1-telemetry-events?brokers=localhost:9092&clientId=events")
        .routeId("udp-kafka-events")
        .log(LoggingLevel.DEBUG, "${body}")
        .log(LoggingLevel.INFO, "Packet[frameId = ${body.header.frameIdentifier}, packetId = ${body.header.packetId}]");
    }
}
