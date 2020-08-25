/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.packets.Packet;
import org.apache.camel.builder.RouteBuilder;

public class RawPacketsRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("netty:udp://0.0.0.0:20777?decoders=#packet-decoder&sync=false")
        .process(exchange -> {
            // useless Processor, temporary used to check that decoding is working fine
            Packet packet = (Packet) exchange.getIn().getBody();
            System.out.println("PacketId=" + packet.getHeader().getPacketId());
        })
        .to("kafka:f1-telemetry?brokers=localhost:9092")
        .routeId("udp-kafka-raw-packets")
        .log("${body}");
    }
}
