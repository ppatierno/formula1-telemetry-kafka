/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class F1KafkaInfluxDBApp {

    private static Logger log = LoggerFactory.getLogger(F1KafkaInfluxDBApp.class);

    public static void main(String[] args) throws Exception {
        F1KafkaInfluxDBAppConfig config = F1KafkaInfluxDBAppConfig.fromEnv();

        log.info("Config: {}", config);

        InfluxDB influxDB = InfluxDBFactory.connect(config.getInfluxDbUrl());
        influxDB.query(new Query("CREATE DATABASE " + "formula1", "formula1"));

        CamelContext camelContext = new DefaultCamelContext();
        camelContext.getRegistry().bind("connectionBean", influxDB);

        camelContext.addRoutes(new DriversPointRouteBuilder(config));
        camelContext.addRoutes(new EventsPointRouteBuilder(config));

        camelContext.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}
