/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.packets.PacketCarSetupData;
import io.ppatierno.formula1.packets.PacketLapData;
import io.ppatierno.formula1.packets.PacketMotionData;
import io.ppatierno.formula1.packets.PacketParticipantsData;

import java.util.ArrayList;
import java.util.List;

public class Race {

    private List<Driver> drivers;

    public Race() {

    }

    public void updateDrivers(PacketParticipantsData packetParticipantsData) {
        if (this.drivers == null) {
            this.drivers = new ArrayList<>(packetParticipantsData.getNumActiveCars());
            for (PacketParticipantsData.ParticipantData pd : packetParticipantsData.getParticipants()) {
                Driver driver = new Driver(pd);
                this.drivers.add(driver);
            }
        }
        // TODO: what happens when it's not the first PacketPartecipantsData? should we updated the drivers?
    }

    public void updateMotion(PacketMotionData packetMotionData) {
        if (this.drivers != null && !this.drivers.isEmpty()) {
            for (int i = 0; i < this.drivers.size(); i++) {
                this.drivers.get(i).setCarMotionData(packetMotionData.getCarMotionData().get(i));
            }
        }
    }

    public void updateLapData(PacketLapData packetLapData) {
        if (this.drivers != null && !this.drivers.isEmpty()) {
            for (int i = 0; i < this.drivers.size(); i++) {
                this.drivers.get(i).setLapData(packetLapData.getLapData().get(i));
            }
        }
    }

    public void updateCarSetup(PacketCarSetupData packetCarSetupData) {
        if (this.drivers != null && !this.drivers.isEmpty()) {
            for (int i = 0; i < this.drivers.size(); i++) {
                this.drivers.get(i).setCarSetupData(packetCarSetupData.getCarSetupData().get(i));
            }
        }
    }

    public Driver getDriver(io.ppatierno.formula1.enums.Driver driverId) {
        return this.drivers.stream().filter(d -> d.getParticipantData().getDriverId() == driverId).findFirst().get();
    }

    @Override
    public String toString() {
        return "Race[drivers=" + this.drivers +
                "]";
    }
}
