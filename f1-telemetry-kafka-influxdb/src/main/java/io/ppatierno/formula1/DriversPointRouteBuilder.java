/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

/**
 * Route getting Driver message from Apache Kafka and writing them to InfluxDB as data point
 */
public class DriversPointRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("kafka:f1-telemetry-drivers?" +
                "brokers=localhost:9092" +
                "&valueDeserializer=io.ppatierno.formula1.DriverDeserializer")
        .process(exchange -> {

            Driver driver = (Driver) exchange.getIn().getBody();

            Point point = Point.measurement("telemetry")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("driverId", driver.getParticipantData().getDriverId().name())
                    .addField("enginerpm", driver.getCarTelemetryData().getEngineRPM())
                    .addField("speed", driver.getCarTelemetryData().getSpeed())
                    .addField("throttle", driver.getCarTelemetryData().getThrottle())
                    .addField("brake", driver.getCarTelemetryData().getBrake())
                    .build();

            exchange.getIn().setBody(point);
        })
        .to("influxdb://connectionBean?databaseName=drivers&retentionPolicy=autogen")
        .routeId("kafka-influxdb-drivers")
        .log(LoggingLevel.INFO, "${body}");
    }
}
