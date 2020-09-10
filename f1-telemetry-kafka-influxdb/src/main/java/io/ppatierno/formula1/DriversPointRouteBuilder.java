/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Route getting Driver message from Apache Kafka and writing them to InfluxDB as data point
 */
public class DriversPointRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("kafka:f1-telemetry-drivers?" +
                "brokers=localhost:9092" +
                "&valueDeserializer=io.ppatierno.formula1.DriverSerializer")
        .process(exchange -> {

            // TODO: process each Driver into a Point

        })
        .to("influxdb://connectionBean?databaseName=drivers&retentionPolicy=autogen")
        .routeId("kafka-influxdb-drivers")
        .log(LoggingLevel.INFO, "${body}");
    }
}
