/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.builder.RouteBuilder;

/**
 * Route getting raw packets from UDP decoding them as Packet instances and multicasting
 * them to multiple destinations for different processing.
 */
public class DispatchRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("netty:udp://0.0.0.0:20777?decoders=#packet-decoder&sync=false")
                .multicast()
                .parallelProcessing()
                .to("direct:raw-packets", "direct:raw-events", "direct:drivers")
                .routeId("udp-multicast-dispatcher");
    }
}
