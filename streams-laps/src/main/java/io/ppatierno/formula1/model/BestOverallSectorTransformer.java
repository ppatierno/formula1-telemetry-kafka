/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.model;

import io.ppatierno.formula1.Driver;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

public class BestOverallSectorTransformer implements Transformer<String, Driver, KeyValue<Short, BestOverallSector>> {

    private ProcessorContext context;
    private KeyValueStore<Short, BestOverallSector> kvStore;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.kvStore = context.getStateStore("best-overall-sector-store");
    }

    @Override
    public KeyValue<Short, BestOverallSector> transform(String key, Driver record) {
        BestOverallSector bestOverallSector = this.kvStore.get((short)1);
        if ((bestOverallSector == null && record.getLapData().getBestOverallSector1TimeInMS() != 0) ||
            (bestOverallSector != null && record.getLapData().getBestOverallSector1TimeInMS() != 0 && record.getLapData().getBestOverallSector1TimeInMS() < bestOverallSector.getTimeInMs())) {
            bestOverallSector = new BestOverallSector((short)1, key, record.getLapData().getBestOverallSector1TimeInMS(), record.getLapData().getBestOverallSector1LapNum());
            this.storeAndForward((short)1, bestOverallSector);
        }

        bestOverallSector = this.kvStore.get((short)2);
        if ((bestOverallSector == null && record.getLapData().getBestOverallSector2TimeInMS() != 0) ||
            (bestOverallSector != null && record.getLapData().getBestOverallSector2TimeInMS() != 0 && record.getLapData().getBestOverallSector2TimeInMS() < bestOverallSector.getTimeInMs())) {
            bestOverallSector = new BestOverallSector((short)2, key, record.getLapData().getBestOverallSector2TimeInMS(), record.getLapData().getBestOverallSector2LapNum());
            this.storeAndForward((short)2, bestOverallSector);
        }

        bestOverallSector = this.kvStore.get((short)3);
        if ((bestOverallSector == null && record.getLapData().getBestOverallSector3TimeInMS() != 0) ||
            (bestOverallSector != null && record.getLapData().getBestOverallSector3TimeInMS() != 0 && record.getLapData().getBestOverallSector3TimeInMS() < bestOverallSector.getTimeInMs())) {
            bestOverallSector = new BestOverallSector((short)3, key, record.getLapData().getBestOverallSector3TimeInMS(), record.getLapData().getBestOverallSector3LapNum());
            this.storeAndForward((short)3, bestOverallSector);
        }
        return null;
    }

    private void storeAndForward(Short sector, BestOverallSector bestOverallSector) {
        this.kvStore.put(sector, bestOverallSector);
        this.context.forward(sector, bestOverallSector);
    }

    @Override
    public void close() {

    }
}
