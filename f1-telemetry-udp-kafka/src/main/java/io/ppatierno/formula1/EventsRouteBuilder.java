/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.enums.PacketId;
import io.ppatierno.formula1.packets.Packet;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Route getting raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast,
 * filtering them to select the EVENT only and sending them to Kafka
 */
public class EventsRouteBuilder extends RouteBuilder  {

    @Override
    public void configure() throws Exception {
        // get raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast
        from("direct:raw-events")
        .filter(exchange -> {
            Packet packet = (Packet) exchange.getIn().getBody();
            return packet.getHeader().getPacketId() == PacketId.EVENT;
        })
        .to("kafka:f1-telemetry-events?brokers=localhost:9092&clientId=events")
        .routeId("udp-kafka-events")
        .log(LoggingLevel.DEBUG, "${body}")
        .log(LoggingLevel.INFO, "Packet[frameId = ${body.header.frameIdentifier}, packetId = ${body.header.packetId}]");
    }
}
