/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.packets.Packet;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawPacketsRouteBuilder extends RouteBuilder {

    private static Logger log = LoggerFactory.getLogger(RawPacketsRouteBuilder.class);

    @Override
    public void configure() throws Exception {
        from("direct:raw-packets")
        .process(exchange -> {
            // useless Processor, temporary used to check that decoding is working fine
            Packet packet = (Packet) exchange.getIn().getBody();
            log.debug("PacketId = {}", packet.getHeader().getPacketId());
        })
        .to("kafka:f1-telemetry?brokers=localhost:9092&clientId=raw-packets")
        .routeId("udp-kafka-raw-packets")
        .log("${body}");
    }
}
