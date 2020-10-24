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

    private final F1UdpKafkaAppConfig config;

    public DispatchRouteBuilder(F1UdpKafkaAppConfig config) {
        this.config = config;
    }

    @Override
    public void configure() throws Exception {
        from("netty:udp://0.0.0.0:" + this.config.getUdpPort() + "?decoders=#packet-decoder&sync=false")
                .multicast()
                .parallelProcessing()
                .to("direct:raw-packets", "direct:events", "direct:drivers")
                .routeId("udp-multicast-dispatcher");
    }
}
