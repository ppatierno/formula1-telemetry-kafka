/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class F1WebConsumer {

    private static Logger log = LoggerFactory.getLogger(F1WebConsumer.class);

    private Vertx vertx;
    private KafkaConsumer<String, Driver> consumer;

    public F1WebConsumer(Vertx vertx) {
        this.vertx = vertx;
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "f1-drivers-webgroup");
        this.consumer = KafkaConsumer.create(vertx, config, new StringDeserializer(), new DriverDeserializer());
    }

    public void start() {
        this.consumer.handler(record -> {
            // TODO: write to the eventbus for the Web UI
            log.info("record = {}", record);
            this.vertx.eventBus().publish("f1-race-ranking", driverToJson(record.value()));
        });
        this.consumer.subscribe("f1-telemetry-drivers", done -> {
            if (done.succeeded()) {
                log.info("Successfully subscribed to the f1-telemetry-drivers topic");
            } else {
                log.error("Error subscribing to the f1-telemetry-drivers topic", done.cause());
            }
        });
    }

    private JsonObject driverToJson(Driver driver) {
        JsonObject json = new JsonObject();
        json.put("position", driver.getLapData().getCarPosition());
        json.put("hashtag", driver.getHashtag());
        json.put("name", driver.getParticipantData().getDriverId().name().replace("_", " "));
        json.put("positiongain", driver.getLapData().getGridPosition() - driver.getLapData().getCarPosition());
        return json;
    }
}
