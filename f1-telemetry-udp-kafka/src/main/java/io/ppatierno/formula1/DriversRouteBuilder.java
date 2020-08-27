/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.packets.Packet;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DriversRouteBuilder extends RouteBuilder {

    private static Logger log = LoggerFactory.getLogger(DriversRouteBuilder.class);

   @Override
    public void configure() throws Exception {
        from("netty:udp://0.0.0.0:20777?decoders=#packet-decoder&sync=false")
                .wireTap("direct:raw-packets")
                // going to group all packets by frameId (it's the correlation key) so that
                // we can update the drivers info (lap, motion, ...) in one step and sending
                // driver messages with all data
                .aggregate(new Expression() {
                    @Override
                    public <Long> Long evaluate(Exchange exchange, Class<Long> type) {
                        Packet packet = (Packet) exchange.getIn().getBody();
                        Long frameId = exchange.getContext().getTypeConverter().convertTo(type, packet.getHeader().getFrameIdentifier());
                        log.debug("Packet FrameId = {}", frameId);
                        return frameId;
                    }
                }, (oldExchange, newExchange) -> {
                    // a new group is started
                    if (oldExchange == null) {
                        List<Packet> list = new ArrayList<>();
                        list.add((Packet) newExchange.getIn().getBody());
                        newExchange.getIn().setBody(list);
                        return newExchange;
                    } else {
                        List<Packet> list = (List<Packet>) oldExchange.getIn().getBody();
                        list.add((Packet) newExchange.getIn().getBody());
                        oldExchange.getIn().setBody(list);
                    }
                    return oldExchange;
                })
                .completionOnNewCorrelationGroup()
                .completionTimeout(10000)
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
