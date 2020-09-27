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

public class F1KafkaInfluxDBApp {

    public static void main(String[] args) throws Exception {

        InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
        influxDB.query(new Query("CREATE DATABASE " + "formula1", "formula1"));

        CamelContext camelContext = new DefaultCamelContext();
        camelContext.getRegistry().bind("connectionBean", influxDB);

        camelContext.addRoutes(new DriversPointRouteBuilder());
        camelContext.addRoutes(new EventsPointRouteBuilder());

        camelContext.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}
