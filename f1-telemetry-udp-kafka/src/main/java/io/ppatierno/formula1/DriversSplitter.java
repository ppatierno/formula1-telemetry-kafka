/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.packets.Packet;
import io.ppatierno.formula1.packets.PacketCarSetupData;
import io.ppatierno.formula1.packets.PacketCarStatusData;
import io.ppatierno.formula1.packets.PacketCarTelemetryData;
import io.ppatierno.formula1.packets.PacketFinalClassificationData;
import io.ppatierno.formula1.packets.PacketLapData;
import io.ppatierno.formula1.packets.PacketMotionData;
import io.ppatierno.formula1.packets.PacketParticipantsData;
import io.ppatierno.formula1.packets.PacketSessionData;

import java.util.List;

public class DriversSplitter {

    private Session session;

    public DriversSplitter(Session session) {
        this.session = session;
    }

    public List<Driver> splitDrivers(Packet packet) {
        switch (packet.getHeader().getPacketId()) {
            case MOTION:
                this.session.updateMotion((PacketMotionData) packet);
                System.out.println(session);
                break;
            case SESSION:
                this.session.updateSession((PacketSessionData) packet);
                System.out.println(session);
                break;
            case LAP_DATA:
                this.session.updateLapData((PacketLapData) packet);
                System.out.println(session);
                break;
            case EVENT:
                break;
            case PARTICIPANTS:
                this.session.updateDrivers((PacketParticipantsData) packet);
                System.out.println(session);
                break;
            case CAR_SETUPS:
                this.session.updateCarSetup((PacketCarSetupData) packet);
                System.out.println(session);
                break;
            case CAR_TELEMETRY:
                this.session.updateCarTelemetry((PacketCarTelemetryData) packet);
                System.out.println(session);
                break;
            case CAR_STATUS:
                this.session.updateCarStatus((PacketCarStatusData) packet);
                System.out.println(session);
                break;
            case FINAL_CLASSIFICATION:
                this.session.updateFinalClassification((PacketFinalClassificationData) packet);
                System.out.println(session);
                break;
            case LOBBY_INFO:
                break;
        }

        return this.session.getDrivers();
    }
}
