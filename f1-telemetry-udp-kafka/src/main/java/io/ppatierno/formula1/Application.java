/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import io.ppatierno.formula1.packets.Packet;

public class Application {

    public static void main(String[] args) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();

        camelContext.getRegistry().bind("packet-decoder", new PacketEventDecoder());

        camelContext.addRoutes(new RouteBuilder(){

            @Override
            public void configure() throws Exception {
                
                from("netty:udp://0.0.0.0:20777?decoders=#packet-decoder&sync=false")
                .process(exchange -> {
                    // useless Processor, temporary used to check that decoding is working fine
                    Packet packet = (Packet) exchange.getIn().getBody();
                    System.out.println("PacketId=" + packet.getHeader().getPacketId());
                })
                .routeId("upd-kafka")
                .log("${body}");
            }
            
        });

        camelContext.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}