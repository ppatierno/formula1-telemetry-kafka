/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.enums.Driver;
import io.ppatierno.formula1.enums.PacketId;
import io.ppatierno.formula1.packets.PacketCarSetupData;
import io.ppatierno.formula1.packets.PacketLapData;
import io.ppatierno.formula1.packets.PacketMotionData;
import io.ppatierno.formula1.packets.PacketParticipantsData;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import io.ppatierno.formula1.packets.Packet;

public class Application {

    public static void main(String[] args) throws Exception {
        Race race = new Race();

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

                    switch (packet.getHeader().getPacketId()) {
                        case MOTION:
                            race.updateMotion((PacketMotionData) packet);
                            System.out.println(race);
                            break;
                        case SESSION:
                            break;
                        case LAP_DATA:
                            race.updateLapData((PacketLapData) packet);
                            System.out.println(race);
                            break;
                        case EVENT:
                            break;
                        case PARTICIPANTS:
                            race.updateDrivers((PacketParticipantsData) packet);
                            System.out.println(race);
                            break;
                        case CAR_SETUPS:
                            race.updateCarSetup((PacketCarSetupData) packet);
                            System.out.println(race);
                            break;
                        case CAR_TELEMETRY:
                            break;
                        case CAR_STATUS:
                            break;
                        case FINAL_CLASSIFICATION:
                            break;
                        case LOBBY_INFO:
                            break;
                    }
                })
                .to("kafka:f1-telemetry?brokers=localhost:9092")
                .routeId("udp-kafka")
                .log("${body}");
            }
            
        });

        camelContext.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}