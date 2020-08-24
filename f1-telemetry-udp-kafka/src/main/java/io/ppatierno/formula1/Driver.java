/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.packets.PacketCarSetupData;
import io.ppatierno.formula1.packets.PacketLapData;
import io.ppatierno.formula1.packets.PacketMotionData;
import io.ppatierno.formula1.packets.PacketParticipantsData;

public class Driver {

    private PacketParticipantsData.ParticipantData participantData;
    private PacketMotionData.CarMotionData carMotionData;
    private PacketLapData.LapData lapData;
    private PacketCarSetupData.CarSetupData carSetupData;

    public Driver(PacketParticipantsData.ParticipantData participantData) {
        this.participantData = participantData;
    }

    public PacketParticipantsData.ParticipantData getParticipantData() {
        return participantData;
    }

    public void setParticipantData(PacketParticipantsData.ParticipantData participantData) {
        this.participantData = participantData;
    }

    public PacketMotionData.CarMotionData getCarMotionData() {
        return carMotionData;
    }

    public void setCarMotionData(PacketMotionData.CarMotionData carMotionData) {
        this.carMotionData = carMotionData;
    }

    public PacketLapData.LapData getLapData() {
        return lapData;
    }

    public void setLapData(PacketLapData.LapData lapData) {
        this.lapData = lapData;
    }

    public PacketCarSetupData.CarSetupData getCarSetupData() {
        return carSetupData;
    }

    public void setCarSetupData(PacketCarSetupData.CarSetupData carSetupData) {
        this.carSetupData = carSetupData;
    }

    @Override
    public String toString() {
        return "Driver[participantData=" + this.participantData +
                ",carMotionData=" + this.carMotionData +
                ",lapData=" + this.lapData +
                ",carSetupData=" + this.carSetupData +
                "]";
    }
}
