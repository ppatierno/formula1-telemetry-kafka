/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */

package io.ppatierno.formula1;

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

    public DriversAvgSpeedPointRouteBuilder(F1KafkaInfluxDBAppConfig config) {
        this.config = config;
    }

    @Override
    public void configure() throws Exception {

        from("kafka:" + this.config.getF1DriversAvgSpeedTopic() + "?" +
                "brokers=" + this.config.getKafkaBootstrapServers() +
                "&valueDeserializer=org.apache.kafka.common.serialization.IntegerDeserializer")
        .process(exchange -> {
            Integer avgSpeed = (Integer) exchange.getIn().getBody();

            Point point = Point.measurement("maxspeed")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("driverhashtag", exchange.getIn().getHeader(KafkaConstants.KEY, String.class))
                    .addField("avgspeed", avgSpeed)
                    .build();

            exchange.getIn().setBody(point);
        })
        .to("influxdb://connectionBean?datab:aseName=" + this.config.getInfluxDbDatabase() + "&retentionPolicy=autogen")
        .routeId("kafka-influxdb-drivers-avg-speed")
        .log(LoggingLevel.TRACE, "${body}");
    }
}
