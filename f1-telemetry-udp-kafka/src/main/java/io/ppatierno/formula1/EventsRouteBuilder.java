/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.data.*;
import io.ppatierno.formula1.enums.PacketId;
import io.ppatierno.formula1.packets.Packet;
import io.ppatierno.formula1.packets.PacketEventData;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;

/**
 * Route getting raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast,
 * filtering them to select the EVENT only and building Event instance to send to Kafka
 */
public class EventsRouteBuilder extends RouteBuilder  {

    private final F1UdpKafkaAppConfig config;
    private final Session session;
    private String kafkaEndpoint;

    public EventsRouteBuilder(F1UdpKafkaAppConfig config, Session session) {
        this.config = config;
        this.session = session;
        this.kafkaEndpoint = "kafka:" + this.config.getF1EventsTopic() + "?" +
                "brokers=" + this.config.getKafkaBootstrapServers() +
                "&clientId=events" +
                "&valueSerializer=io.ppatierno.formula1.EventSerializer";
        if (this.config.getKafkaTruststoreLocation() != null && this.config.getKafkaTruststorePassword() != null) {
            this.kafkaEndpoint += "&sslTruststoreLocation=" + this.config.getKafkaTruststoreLocation() +
                    "&sslTruststorePassword=" + this.config.getKafkaTruststorePassword() +
                    "&sslTruststoreType=PKCS12" +
                    "&securityProtocol=SSL";
        }
        log.info("kafkaEndpoint = {}", this.kafkaEndpoint);
    }

    @Override
    public void configure() throws Exception {
        // get raw Packet instances (as body) from the "udp-multicast-dispatcher" route thanks to multicast
        from("direct:events")
        .filter(exchange -> {
            Packet packet = (Packet) exchange.getIn().getBody();
            return packet.getHeader().getPacketId() == PacketId.EVENT;
        })
        .process(exchange -> {
            PacketEventData packetEventData = (PacketEventData) exchange.getIn().getBody();
            this.session.updateEventData(packetEventData);
            exchange.getIn().setHeader(KafkaConstants.KEY, packetEventData.getEventCode().name());
            exchange.getIn().setBody(this.buildEvent(packetEventData));
        })
        .to(this.kafkaEndpoint)
        .routeId("udp-kafka-events")
        .log(LoggingLevel.TRACE, "${body}")
        .log(LoggingLevel.DEBUG, "Event[id = ${body?.participantData.driverId}, code = ${body.eventData.eventCode}]");
    }

    private Event buildEvent(PacketEventData packetEventData) {
        ParticipantData participantData = null;
        switch (packetEventData.getEventCode()) {
            case SESSION_STARTED:
            case SESSION_ENDED:
            case DRS_ENABLED:
            case DRS_DISABLED:
            case CHEQUERED_FLAG:
                break;
            case FASTEST_LAP:
                FastestLap fastestLap = packetEventData.getEventDataDetails().getFastestLap();
                participantData = this.session.getDrivers().get(fastestLap.getVehicleIdx()).getParticipantData();
                break;
            case RETIREMENT:
                Retirement retirement = packetEventData.getEventDataDetails().getRetirement();
                participantData = this.session.getDrivers().get(retirement.getVehicleIdx()).getParticipantData();
                break;
            case TEAM_MATE_IN_PITS:
                TeamMateInPits teamMateInPits = packetEventData.getEventDataDetails().getTeamMateInPits();
                participantData = this.session.getDrivers().get(teamMateInPits.getVehicleIdx()).getParticipantData();
                break;
            case RACE_WINNER:
                RaceWinner raceWinner = packetEventData.getEventDataDetails().getRaceWinner();
                participantData = this.session.getDrivers().get(raceWinner.getVehicleIdx()).getParticipantData();
                break;
            case PENALTY_ISSUED:
                Penalty penalty = packetEventData.getEventDataDetails().getPenalty();
                participantData = this.session.getDrivers().get(penalty.getVehicleIdx()).getParticipantData();
                break;
            case SPEED_TRAP_TRIGGERED:
                SpeedTrap speedTrap = packetEventData.getEventDataDetails().getSpeedTrap();
                participantData = this.session.getDrivers().get(speedTrap.getVehicleIdx()).getParticipantData();
                break;
        }
        return new Event(participantData, packetEventData);
    }
}
