/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class Application {

    public static void main(String[] args) throws Exception {
        Session session = new Session();

        CamelContext camelContext = new DefaultCamelContext();

        camelContext.getRegistry().bind("packet-decoder", new PacketEventDecoder());
        camelContext.getRegistry().bind("drivers-splitter", new DriversSplitter(session));

        camelContext.addRoutes(new RawPacketsRouteBuilder());
        camelContext.addRoutes(new DriversRouteBuilder(session));

        camelContext.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}