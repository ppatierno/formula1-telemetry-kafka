/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class F1UdpKafkaApp {

    public static void main(String[] args) throws Exception {
        F1UdpKafkaAppConfig config = F1UdpKafkaAppConfig.fromEnv();
        Session session = new Session();

        CamelContext camelContext = new DefaultCamelContext();

        camelContext.getRegistry().bind("packet-decoder", new PacketEventDecoder());
        camelContext.getRegistry().bind("drivers-splitter", new DriversSplitter(session));

        camelContext.addRoutes(new DispatchRouteBuilder(config));
        camelContext.addRoutes(new RawPacketsRouteBuilder(config));
        camelContext.addRoutes(new EventsRouteBuilder(config, session));
        camelContext.addRoutes(new DriversRouteBuilder(config));

        camelContext.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}