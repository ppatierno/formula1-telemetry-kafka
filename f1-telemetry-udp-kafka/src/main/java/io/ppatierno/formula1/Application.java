/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.packets.PacketCarSetupData;
import io.ppatierno.formula1.packets.PacketCarStatusData;
import io.ppatierno.formula1.packets.PacketCarTelemetryData;
import io.ppatierno.formula1.packets.PacketFinalClassificationData;
import io.ppatierno.formula1.packets.PacketLapData;
import io.ppatierno.formula1.packets.PacketMotionData;
import io.ppatierno.formula1.packets.PacketParticipantsData;
import io.ppatierno.formula1.packets.PacketSessionData;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import io.ppatierno.formula1.packets.Packet;

public class Application {

    public static void main(String[] args) throws Exception {
        Session session = new Session();

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
                            session.updateMotion((PacketMotionData) packet);
                            System.out.println(session);
                            break;
                        case SESSION:
                            session.updateSession((PacketSessionData) packet);
                            System.out.println(session);
                            break;
                        case LAP_DATA:
                            session.updateLapData((PacketLapData) packet);
                            System.out.println(session);
                            break;
                        case EVENT:
                            break;
                        case PARTICIPANTS:
                            session.updateDrivers((PacketParticipantsData) packet);
                            System.out.println(session);
                            break;
                        case CAR_SETUPS:
                            session.updateCarSetup((PacketCarSetupData) packet);
                            System.out.println(session);
                            break;
                        case CAR_TELEMETRY:
                            session.updateCarTelemetry((PacketCarTelemetryData) packet);
                            System.out.println(session);
                            break;
                        case CAR_STATUS:
                            session.updateCarStatus((PacketCarStatusData) packet);
                            System.out.println(session);
                            break;
                        case FINAL_CLASSIFICATION:
                            session.updateFinalClassification((PacketFinalClassificationData) packet);
                            System.out.println(session);
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