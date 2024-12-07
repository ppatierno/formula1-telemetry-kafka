/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import java.util.concurrent.CountDownLatch;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class F1KafkaInfluxDBApp {

    private static final Logger log = LoggerFactory.getLogger(F1KafkaInfluxDBApp.class);

    public static void main(String[] args) throws Exception {
        log.info("Season {}", PacketConfig.getSeason());
        F1KafkaInfluxDBAppConfig config = F1KafkaInfluxDBAppConfig.fromEnv();

        log.info("Config: {}", config);

        InfluxDB influxDB = InfluxDBFactory.connect(config.getInfluxDbUrl());
        influxDB.query(new Query("CREATE DATABASE " + config.getInfluxDbDatabase(), config.getInfluxDbDatabase()));

        CamelContext camelContext = new DefaultCamelContext();
        camelContext.getRegistry().bind("connectionBean", influxDB);

        camelContext.addRoutes(new DriversPointRouteBuilder(config));
        camelContext.addRoutes(new EventsPointRouteBuilder(config));
        camelContext.addRoutes(new DriversAvgSpeedPointRouteBuilder(config));

        CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    camelContext.close();
                } catch (Exception e) {
                    log.error("Error closing CamelContext", e);
                } finally {
                    latch.countDown();
                }
            }
        });

        try {
            camelContext.start();
            latch.await();
        } catch (Throwable e) {
            log.error("Error starting CamelContext", e);
            System.exit(1);
        }
        System.exit(0);
    }
}
