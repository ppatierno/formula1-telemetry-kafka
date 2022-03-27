/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.camel.KafkaEndpoint;
import io.ppatierno.formula1.data.FastestLap;
import io.ppatierno.formula1.data.SpeedTrap;
import io.ppatierno.formula1.enums.EventCode;
import io.ppatierno.formula1.model.Event;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

public class EventsPointRouteBuilder extends RouteBuilder {

    private final F1KafkaInfluxDBAppConfig config;
    private KafkaEndpoint kafkaEndpoint;

    public EventsPointRouteBuilder(F1KafkaInfluxDBAppConfig config) {
        this.config = config;
        KafkaEndpoint.KafkaEndpointBuilder kafkaEndpointBuilder = new KafkaEndpoint.KafkaEndpointBuilder()
                .withBootstrapServers(this.config.getKafkaBootstrapServers())
                .withTopic(this.config.getF1EventsTopic())
                .withClientId("kafka-influxdb-events")
                .withGroupId("f1-kafka-influxdb-events-group")
                .withValueDeserializer("io.ppatierno.formula1.serializers.EventDeserializer")
                .withTlsEnabled(this.config.isKafkaTlsEnabled())
                .withTruststoreLocation(this.config.getKafkaTruststoreLocation())
                .withTruststorePassword(this.config.getKafkaTruststorePassword())
                .withSaslMechanism(this.config.getKafkaSaslMechanism());

        if ("PLAIN".equals(this.config.getKafkaSaslMechanism()) &&
                this.config.getKafkaSaslUsername() != null && this.config.getKafkaSaslPassword() != null) {
            String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + this.config.getKafkaSaslUsername() + "\" password=\"" + this.config.getKafkaSaslPassword() + "\";";
            kafkaEndpointBuilder.withSaslJassConfig(saslJaasConfig);
        }

        this.kafkaEndpoint = kafkaEndpointBuilder.build();
        log.info("KafkaEndpoint = {}", this.kafkaEndpoint);
    }

    @Override
    public void configure() throws Exception {
        from(this.kafkaEndpoint.toString())
        .filter(exchange ->  {
            Event event = (Event) exchange.getIn().getBody();
            // we are interested in following events only
            return event.getEventData().getEventCode() == EventCode.FASTEST_LAP ||
                    event.getEventData().getEventCode() == EventCode.SPEED_TRAP_TRIGGERED;
        })
        .process(exchange -> {

            Event event = (Event) exchange.getIn().getBody();

            Point point = null;
            switch (event.getEventData().getEventCode()) {
                case FASTEST_LAP:
                    FastestLap fastestLap = event.getEventData().getEventDataDetails().getFastestLap();
                    point = Point.measurement("fastestlap")
                            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                            .tag("driverid", event.getParticipantData().getDriverId().name())
                            .tag("driverhashtag", event.getHashtag())
                            .addField("laptime", fastestLap.getLapTime())
                            .build();
                    break;
                case SPEED_TRAP_TRIGGERED:
                    SpeedTrap speedTrap = event.getEventData().getEventDataDetails().getSpeedTrap();
                    point = Point.measurement("speedtrap")
                            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                            .tag("driverid", event.getParticipantData().getDriverId().name())
                            .tag("driverhashtag", event.getHashtag())
                            .addField("speed", speedTrap.getSpeed())
                            .build();
                    break;
                default:
                    // should never happen due to the previous filter operation (resolves warning about missing cases)
                    throw new IllegalArgumentException("Event " + event.getEventData().getEventCode().name() + " not handled");
            }
            exchange.getIn().setBody(point);
        })
        .to("influxdb://connectionBean?databaseName=" + this.config.getInfluxDbDatabase() + "&retentionPolicy=autogen")
        .routeId("kafka-influxdb-events")
        .log(LoggingLevel.TRACE, "${body}")
        .log(LoggingLevel.DEBUG, "Event[${body.tags}]");
    }
}
