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
    private F1WebUIAppConfig config;

    public F1WebConsumer(Vertx vertx, F1WebUIAppConfig config) {
        this.vertx = vertx;
        this.config = config;
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.config.getKafkaBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.config.getF1DriversGroupId());
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
