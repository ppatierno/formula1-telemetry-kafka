/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

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
}
