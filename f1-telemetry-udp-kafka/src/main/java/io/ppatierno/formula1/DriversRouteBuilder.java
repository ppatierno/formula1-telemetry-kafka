/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.builder.RouteBuilder;

public class DriversRouteBuilder extends RouteBuilder {

    private Session session;

    public DriversRouteBuilder(Session session) {
        this.session = session;
    }

    @Override
    public void configure() throws Exception {
        from("netty:udp://0.0.0.0:20777?decoders=#packet-decoder&sync=false")
                .wireTap("direct:raw-packets")
                .split().method("drivers-splitter", "splitDrivers")
                .to("kafka:f1-telemetry-drivers?brokers=localhost:9092")
                .routeId("udp-kafka-drivers")
                .log("${body}");
    }
}
