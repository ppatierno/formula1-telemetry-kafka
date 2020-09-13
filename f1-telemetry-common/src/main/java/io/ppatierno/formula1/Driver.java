/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.data.CarMotionData;
import io.ppatierno.formula1.data.CarSetupData;
import io.ppatierno.formula1.data.CarStatusData;
import io.ppatierno.formula1.data.CarTelemetryData;
import io.ppatierno.formula1.data.FinalClassificationData;
import io.ppatierno.formula1.data.LapData;
import io.ppatierno.formula1.data.ParticipantData;

/**
 * Represents a single driver with all related data during a session
 */
public class Driver {

    private ParticipantData participantData;
    private CarMotionData carMotionData;
    private LapData lapData;
    private CarSetupData carSetupData;
    private CarTelemetryData carTelemetryData;
    private CarStatusData carStatusData;
    private FinalClassificationData finalClassificationData;

    private String hashtag;

    public Driver(ParticipantData participantData) {
        this.participantData = participantData;
        String driverId = participantData.getDriverId().name();
        this.hashtag = "#" + driverId.charAt(0) + driverId.charAt(driverId.indexOf("_") + 1) + participantData.getRaceNumber();
    }

    public ParticipantData getParticipantData() {
        return participantData;
    }

    public void setParticipantData(ParticipantData participantData) {
        this.participantData = participantData;
    }

    public CarMotionData getCarMotionData() {
        return carMotionData;
    }

    public void setCarMotionData(CarMotionData carMotionData) {
        this.carMotionData = carMotionData;
    }

    public LapData getLapData() {
        return lapData;
    }

    public void setLapData(LapData lapData) {
        this.lapData = lapData;
    }

    public CarSetupData getCarSetupData() {
        return carSetupData;
    }

    public void setCarSetupData(CarSetupData carSetupData) {
        this.carSetupData = carSetupData;
    }

    public CarTelemetryData getCarTelemetryData() {
        return carTelemetryData;
    }

    public void setCarTelemetryData(CarTelemetryData carTelemetryData) {
        this.carTelemetryData = carTelemetryData;
    }

    public CarStatusData getCarStatusData() {
        return carStatusData;
    }

    public void setCarStatusData(CarStatusData carStatusData) {
        this.carStatusData = carStatusData;
    }

    public FinalClassificationData getFinalClassificationData() {
        return finalClassificationData;
    }

    public void setFinalClassificationData(FinalClassificationData finalClassificationData) {
        this.finalClassificationData = finalClassificationData;
    }

    public String getHashtag() {
        return hashtag;
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
