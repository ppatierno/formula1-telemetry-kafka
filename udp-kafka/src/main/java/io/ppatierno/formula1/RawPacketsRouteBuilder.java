/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.camel.KafkaEndpoint;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import io.ppatierno.formula1.camel.KafkaEndpoint.KafkaEndpointBuilder;

/**
 * Route getting raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast
 * and sending them to Kafka
 */
public class RawPacketsRouteBuilder extends RouteBuilder {

    private final F1UdpKafkaAppConfig config;
    private final KafkaEndpoint kafkaEndpoint;

    public RawPacketsRouteBuilder(F1UdpKafkaAppConfig config) {
        this.config = config;
        KafkaEndpointBuilder kafkaEndpointBuilder = new KafkaEndpoint.KafkaEndpointBuilder()
                .withBootstrapServers(this.config.getCommon().getKafkaBootstrapServers())
                .withTopic(this.config.getF1RawPacketsTopic())
                .withClientId("raw-packets")
                .withTlsEnabled(this.config.getCommon().isKafkaTlsEnabled())
                .withTruststoreLocation(this.config.getCommon().getKafkaTruststoreLocation())
                .withTruststorePassword(this.config.getCommon().getKafkaTruststorePassword())
                .withSaslMechanism(this.config.getCommon().getKafkaSaslMechanism());

        if ("PLAIN".equals(this.config.getCommon().getKafkaSaslMechanism()) &&
            this.config.getCommon().getKafkaSaslUsername() != null && this.config.getCommon().getKafkaSaslPassword() != null) {
                String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + this.config.getCommon().getKafkaSaslUsername() + "\" password=\"" + this.config.getCommon().getKafkaSaslPassword() + "\";";
                kafkaEndpointBuilder.withSaslJassConfig(saslJaasConfig);
        }
        
        this.kafkaEndpoint = kafkaEndpointBuilder.build();        
        log.info("KafkaEndpoint = {}", this.kafkaEndpoint);
    }

    @Override
    public void configure() throws Exception {
        // get raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast
        from("direct:raw-packets")
        .to(this.kafkaEndpoint.toString())
        .routeId("udp-kafka-raw-packets")
        .log(LoggingLevel.TRACE, "${body}")
        .log(LoggingLevel.DEBUG, "Packet[frameId = ${body.header.frameIdentifier}, packetId = ${body.header.packetId}]");
    }
}
