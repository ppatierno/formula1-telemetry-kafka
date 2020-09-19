/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Route getting raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast
 * and sending them to Kafka
 */
public class RawPacketsRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // get raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast
        from("direct:raw-packets")
        .to("kafka:f1-telemetry-packets?brokers=localhost:9092&clientId=raw-packets")
        .routeId("udp-kafka-raw-packets")
        .log(LoggingLevel.DEBUG, "${body}")
        .log(LoggingLevel.INFO, "Packet[frameId = ${body.header.frameIdentifier}, packetId = ${body.header.packetId}]");
    }
}
