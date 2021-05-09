/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.model;

public class BestOverallSector {

    private final int bestOverallSector1TimeInMS;
    private final short bestOverallSector1LapNum;
    private final int bestOverallSector2TimeInMS;
    private final short bestOverallSector2LapNum;
    private final int bestOverallSector3TimeInMS;
    private final short bestOverallSector3LapNum;

    public BestOverallSector(int bestOverallSector1TimeInMS, short bestOverallSector1LapNum,
                            int bestOverallSector2TimeInMS, short bestOverallSector2LapNum,
                            int bestOverallSector3TimeInMS, short bestOverallSector3LapNum)
    {
        this.bestOverallSector1TimeInMS = bestOverallSector1TimeInMS;
        this.bestOverallSector1LapNum = bestOverallSector1LapNum;
        this.bestOverallSector2TimeInMS = bestOverallSector2TimeInMS;
        this.bestOverallSector2LapNum = bestOverallSector2LapNum;
        this.bestOverallSector3TimeInMS = bestOverallSector3TimeInMS;
        this.bestOverallSector3LapNum = bestOverallSector3LapNum;
    }

    public int getBestOverallSector1TimeInMS() {
        return bestOverallSector1TimeInMS;
    }

    public short getBestOverallSector1LapNum() {
        return bestOverallSector1LapNum;
    }

    public int getBestOverallSector2TimeInMS() {
        return bestOverallSector2TimeInMS;
    }

    public short getBestOverallSector2LapNum() {
        return bestOverallSector2LapNum;
    }

    public int getBestOverallSector3TimeInMS() {
        return bestOverallSector3TimeInMS;
    }

    public short getBestOverallSector3LapNum() {
        return bestOverallSector3LapNum;
    }
}