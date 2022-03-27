/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.serializers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.ppatierno.formula1.data.CarMotionData;
import io.ppatierno.formula1.data.CarSetupData;
import io.ppatierno.formula1.data.CarStatusData;
import io.ppatierno.formula1.data.CarTelemetryData;
import io.ppatierno.formula1.data.FinalClassificationData;
import io.ppatierno.formula1.data.LapData;
import io.ppatierno.formula1.data.ParticipantData;
import io.ppatierno.formula1.model.Driver;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class DriverDeserializer implements Deserializer<Driver> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Driver deserialize(String topic, byte[] bytes) {
        return deserialize(topic, null, bytes);
    }

    @Override
    public Driver deserialize(String topic, Headers headers, byte[] bytes) {
        ByteBuf bb = Unpooled.wrappedBuffer(bytes);
        Driver driver = new Driver(new ParticipantData().fill(bb));
        if (bb.isReadable()) {
            driver.setCarMotionData(new CarMotionData().fill(bb));
        }
        if (bb.isReadable()) {
            driver.setLapData(new LapData().fill(bb));
        }
        if (bb.isReadable()) {
            driver.setCarSetupData(new CarSetupData().fill(bb));
        }
        if (bb.isReadable()) {
            driver.setCarTelemetryData(new CarTelemetryData().fill(bb));
        }
        if (bb.isReadable()) {
            driver.setCarStatusData(new CarStatusData().fill(bb));
        }
        if (bb.isReadable()) {
            driver.setFinalClassificationData(new FinalClassificationData().fill(bb));
        }
        return driver;
    }

    @Override
    public void close() {

    }
}
