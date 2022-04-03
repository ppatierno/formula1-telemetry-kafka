/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */

package io.ppatierno.formula1;

import io.ppatierno.formula1.camel.KafkaEndpoint;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

/**
 * Route getting Driver average speed message from Apache Kafka and writing them to InfluxDB as data point
 */
public class DriversAvgSpeedPointRouteBuilder extends RouteBuilder {

    private final F1KafkaInfluxDBAppConfig config;
    private KafkaEndpoint kafkaEndpoint;

    public DriversAvgSpeedPointRouteBuilder(F1KafkaInfluxDBAppConfig config) {
        this.config = config;
        KafkaEndpoint.KafkaEndpointBuilder kafkaEndpointBuilder = new KafkaEndpoint.KafkaEndpointBuilder()
                .withBootstrapServers(this.config.getCommon().getKafkaBootstrapServers())
                .withTopic(this.config.getF1DriversAvgSpeedTopic())
                .withClientId("kafka-influxdb-drivers-avg-speed")
                .withGroupId("f1-kafka-influxdb-drivers-avg-speed-group")
                .withValueDeserializer("org.apache.kafka.common.serialization.IntegerDeserializer")
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
        from(this.kafkaEndpoint.toString())
        .process(exchange -> {
            Integer avgSpeed = (Integer) exchange.getIn().getBody();

            Point point = Point.measurement("avgspeed")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("driverhashtag", exchange.getIn().getHeader(KafkaConstants.KEY, String.class))
                    .addField("avgspeed", avgSpeed)
                    .build();

            exchange.getIn().setBody(point);
        })
        .to("influxdb://connectionBean?databaseName=" + this.config.getInfluxDbDatabase() + "&retentionPolicy=autogen")
        .routeId("kafka-influxdb-drivers-avg-speed")
        .log(LoggingLevel.TRACE, "${body}");
    }
}
