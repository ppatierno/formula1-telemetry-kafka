/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.packets.Packet;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Route getting raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast.
 * It aggregates the packets with the same frameId (as correlation key) and uses them to update drivers data to send to Kafka.
 */
public class DriversRouteBuilder extends RouteBuilder {

    private static Logger log = LoggerFactory.getLogger(DriversRouteBuilder.class);

    private static int AGGREGATION_COMPLETION_TIMEOUT = 10000;

    private final F1UdpKafkaAppConfig config;
    private String kafkaEndpoint;

    public DriversRouteBuilder(F1UdpKafkaAppConfig config) {
        this.config = config;
        this.kafkaEndpoint =
                "kafka:" + this.config.getF1DriversTopic() + "?" +
                "brokers=" + this.config.getKafkaBootstrapServers() +
                "&clientId=drivers" +
                "&valueSerializer=io.ppatierno.formula1.DriverSerializer";
        if (this.config.getKafkaTruststoreLocation() != null && this.config.getKafkaTruststorePassword() != null) {
            this.kafkaEndpoint += "&sslTruststoreLocation=" + this.config.getKafkaTruststoreLocation() +
                    "&sslTruststorePassword=" + this.config.getKafkaTruststorePassword() +
                    "&sslTruststoreType=PKCS12" +
                    "&securityProtocol=SSL";
        }
        log.info("kafkaEndpoint = {}", this.kafkaEndpoint);
    }

    @Override
    public void configure() throws Exception {
        // get raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast
        from("direct:drivers")
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
                        // grouping packets within same frame (same frameId) as a list
                        list.add((Packet) newExchange.getIn().getBody());
                        newExchange.getIn().setBody(list);
                        return newExchange;
                    } else {
                        List<Packet> list = (List<Packet>) oldExchange.getIn().getBody();
                        // adding new packet as part of the same frame to the current list
                        list.add((Packet) newExchange.getIn().getBody());
                        oldExchange.getIn().setBody(list);
                    }
                    return oldExchange;
                })
                .completionOnNewCorrelationGroup()
                .completionTimeout(AGGREGATION_COMPLETION_TIMEOUT)
                .split().method("drivers-splitter", "splitDrivers")
                .process(exchange -> {
                    Driver driver = (Driver) exchange.getIn().getBody();
                    exchange.getIn().setHeader(KafkaConstants.KEY, driver.getParticipantData().getDriverId().name());
                })
                .to(this.kafkaEndpoint)
                .log(LoggingLevel.TRACE, "${body}")
                .log(LoggingLevel.DEBUG, "Driver[id = ${body.participantData.driverId}, hashtag = ${body.hashtag}]");
    }
}
