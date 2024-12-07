/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.model.Driver;
import io.ppatierno.formula1.serializers.DriverDeserializer;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class F1WebConsumer {

    private static final Logger log = LoggerFactory.getLogger(F1WebConsumer.class);

    private final Vertx vertx;
    private final KafkaConsumer<String, Driver> consumer;
    private final F1WebUIAppConfig config;

    public F1WebConsumer(Vertx vertx, F1WebUIAppConfig config) {
        this.vertx = vertx;
        this.config = config;
        Properties props = F1WebUIAppConfig.getProperties(config);
        this.consumer = KafkaConsumer.create(vertx, props, new StringDeserializer(), new DriverDeserializer());
    }

    public void start() {
        this.consumer.handler(record -> {
            log.trace("record = {}", record);
            this.vertx.eventBus().publish("f1-race-ranking", driverToJson(record.value()));
        });
        this.consumer.subscribe(this.config.getF1DriversTopic(), done -> {
            if (done.succeeded()) {
                log.info("Successfully subscribed to the f1-telemetry-drivers topic");
            } else {
                log.error("Error subscribing to the f1-telemetry-drivers topic", done.cause());
            }
        });
    }

    private JsonObject driverToJson(Driver driver) {
        JsonObject json = new JsonObject();
        json.put("name", driver.getParticipantData().getDriverId().name().replace("_", " "));
        json.put("hashtag", driver.getHashtag());
        if (driver.getLapData() != null) {
            json.put("position", driver.getLapData().getCarPosition());
            json.put("positiongain", driver.getLapData().getGridPosition() - driver.getLapData().getCarPosition());
        }
        if (driver.getCarStatusData() != null) {
            json.put("tyre", driver.getCarStatusData().getVisualTyreCompound());
        }
        return json;
    }
}
