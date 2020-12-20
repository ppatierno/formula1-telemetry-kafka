/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.data.FastestLap;
import io.ppatierno.formula1.data.SpeedTrap;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

public class EventsPointRouteBuilder extends RouteBuilder {

    private final F1KafkaInfluxDBAppConfig config;

    public EventsPointRouteBuilder(F1KafkaInfluxDBAppConfig config) {
        this.config = config;
    }

    @Override
    public void configure() throws Exception {

        from("kafka:" + this.config.getF1EventsTopic() + "?" +
                "brokers=" + this.config.getKafkaBootstrapServers() +
                "&valueDeserializer=io.ppatierno.formula1.EventDeserializer")
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
            }
            exchange.getIn().setBody(point);
        })
        .to("influxdb://connectionBean?databaseName=formula1&retentionPolicy=autogen")
        .routeId("kafka-influxdb-events")
        .log(LoggingLevel.TRACE, "${body}")
        .log(LoggingLevel.DEBUG, "Event[${body.tags}]");
    }
}
