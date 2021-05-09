/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.model;

public class BestOverallSector {

    private final short sector;
    private final String driver;
    private final int timeInMs;
    private final short lapNum;

    public BestOverallSector(short sector, String driver, int timeInMs, short lapNum)
    {
        this.sector = sector;
        this.driver = driver;
        this.timeInMs = timeInMs;
        this.lapNum = lapNum;
    }

    public short getSector() {
        return sector;
    }

    public String getDriver() {
        return driver;
    }

    public int getTimeInMs() {
        return timeInMs;
    }

    public short getLapNum() {
        return lapNum;
    }
}