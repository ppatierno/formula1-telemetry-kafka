/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.model;

import io.ppatierno.formula1.Driver;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

public class BestOverallSectorProcessor implements Processor<String, Driver, String, BestOverallSector> {

    @Override
    public void init(ProcessorContext<String, BestOverallSector> context) {

    }

    @Override
    public void process(Record<String, Driver> record) {

    }

    @Override
    public void close() {

    }
}
