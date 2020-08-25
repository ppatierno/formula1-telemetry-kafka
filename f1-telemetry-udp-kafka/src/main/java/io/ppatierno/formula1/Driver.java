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

/**
 * Represents a single driver with all related data during a session
 */
public class Driver {

    private PacketParticipantsData.ParticipantData participantData;
    private PacketMotionData.CarMotionData carMotionData;
    private PacketLapData.LapData lapData;
    private PacketCarSetupData.CarSetupData carSetupData;
    private PacketCarTelemetryData.CarTelemetryData carTelemetryData;
    private PacketCarStatusData.CarStatusData carStatusData;
    private PacketFinalClassificationData.FinalClassificationData finalClassificationData;

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

    public PacketCarTelemetryData.CarTelemetryData getCarTelemetryData() {
        return carTelemetryData;
    }

    public void setCarTelemetryData(PacketCarTelemetryData.CarTelemetryData carTelemetryData) {
        this.carTelemetryData = carTelemetryData;
    }

    public PacketCarStatusData.CarStatusData getCarStatusData() {
        return carStatusData;
    }

    public void setCarStatusData(PacketCarStatusData.CarStatusData carStatusData) {
        this.carStatusData = carStatusData;
    }

    public PacketFinalClassificationData.FinalClassificationData getFinalClassificationData() {
        return finalClassificationData;
    }

    public void setFinalClassificationData(PacketFinalClassificationData.FinalClassificationData finalClassificationData) {
        this.finalClassificationData = finalClassificationData;
    }

    @Override
    public String toString() {
        return "Driver[participantData=" + this.participantData +
                ",carMotionData=" + this.carMotionData +
                ",lapData=" + this.lapData +
                ",carSetupData=" + this.carSetupData +
                ",catTelemetryData=" + this.carTelemetryData +
                ",carStatusData=" + this.carStatusData +
                ",finalClassificationData=" + this.finalClassificationData +
                "]";
    }
}
