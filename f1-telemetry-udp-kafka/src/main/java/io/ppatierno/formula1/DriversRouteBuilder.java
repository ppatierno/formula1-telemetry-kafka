/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;

public class DriversRouteBuilder extends RouteBuilder {

   @Override
    public void configure() throws Exception {
        from("netty:udp://0.0.0.0:20777?decoders=#packet-decoder&sync=false")
                .wireTap("direct:raw-packets")
                .split().method("drivers-splitter", "splitDrivers")
                .process(exchange -> {
                    Driver driver = (Driver) exchange.getIn().getBody();
                    exchange.getIn().setHeader(KafkaConstants.KEY, driver.getParticipantData().getDriverId().name());
                })
                .to("kafka:f1-telemetry-drivers?brokers=localhost:9092")
                .routeId("udp-kafka-drivers")
                .log("${body}");
    }
}
