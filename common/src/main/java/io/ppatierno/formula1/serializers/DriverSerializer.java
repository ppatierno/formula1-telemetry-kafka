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
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class DriverSerializer implements Serializer<Driver> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Driver driver) {
        return serialize(topic, null, driver);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Driver driver) {
        ByteBuf bb = Unpooled.buffer(this.rawBytesSize(driver));
        driver.getParticipantData().fillBuffer(bb);
        if (bb.isWritable()) {
            driver.getCarMotionData().fillBuffer(bb);
        }
        if (bb.isWritable()) {
            driver.getLapData().fillBuffer(bb);
        }
        if (bb.isWritable()) {
            driver.getCarSetupData().fillBuffer(bb);
        }
        if (bb.isWritable()) {
            driver.getCarTelemetryData().fillBuffer(bb);
        }
        if (bb.isWritable()) {
            driver.getCarStatusData().fillBuffer(bb);
        }
        if (bb.isWritable()) {
            driver.getFinalClassificationData().fillBuffer(bb);
        }
        return bb.array();
    }

    @Override
    public void close() {

    }

    /**
     * Get the raw size in bytes of a Driver with the related non-null serialized field
     *
     * @param driver Driver instance to determinate the size in bytes
     * @return the raw size in bytes of the Driver instance
     */
    public int rawBytesSize(Driver driver) {
        int size = 0;
        size += driver.getParticipantData() != null ? ParticipantData.SIZE : 0;
        size += driver.getCarMotionData() != null ? CarMotionData.SIZE : 0;
        size += driver.getLapData() != null ? LapData.SIZE : 0;
        size += driver.getCarSetupData() != null ? CarSetupData.SIZE : 0;
        size += driver.getCarTelemetryData() != null ? CarTelemetryData.SIZE : 0;
        size += driver.getCarStatusData() != null ? CarStatusData.SIZE : 0;
        size += driver.getFinalClassificationData() != null ? FinalClassificationData.SIZE : 0;
        return size;
    }
}
