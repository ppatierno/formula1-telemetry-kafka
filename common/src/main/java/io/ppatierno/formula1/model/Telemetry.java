/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.model;

/**
 * Represents telemetry data from a driver simplified for JSON serialization
 */
public class Telemetry {

    private final Driver driver;

    public Telemetry(Driver driver) {
        this.driver = driver;
    }

    public String getDriverHashtag() {
        return this.driver.getHashtag();
    }

    public String getDriverShortName() {
        return this.driver.getShortName();
    } 

    public int getSpeed() {
        return this.driver.getCarTelemetryData().getSpeed();
    }

    public int getRpm() {
        return this.driver.getCarTelemetryData().getEngineRPM();
    }

    public int getGear() {
        return this.driver.getCarTelemetryData().getGear();
    }

    public float getThrottle() {
        return this.driver.getCarTelemetryData().getThrottle() * 100;
    }

    public float getBrake() {
        return this.driver.getCarTelemetryData().getBrake() * 100;
    }

    public short getDrs() {
        return this.driver.getCarTelemetryData().getDrs();
    }

    public short getLap() {
        return this.driver.getLapData().getCurrentLapNum();
    }

    public float getDistance() {
        return this.driver.getLapData().getLapDistance();
    }

    public float getTotalDistance() {
        return this.driver.getLapData().getTotalDistance();
    }

    public int getBestOverallSector1TimeInMS() {
        return this.driver.getLapData().getBestOverallSector1TimeInMS();
    }

    public int getBestOverallSector2TimeInMS() {
        return this.driver.getLapData().getBestOverallSector2TimeInMS();
    }

    public int getBestOverallSector3TimeInMS() {
        return this.driver.getLapData().getBestOverallSector3TimeInMS();
    }
}
