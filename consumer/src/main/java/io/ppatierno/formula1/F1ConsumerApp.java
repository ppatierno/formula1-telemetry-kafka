/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.model.BestOverallSector;
import io.ppatierno.formula1.model.Driver;
import io.ppatierno.formula1.model.Event;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class F1ConsumerApp {

    private static Logger log = LoggerFactory.getLogger(F1ConsumerApp.class);

    private ExecutorService executorService;
    private F1ConsumerAppConfig config;

    private F1Consumer<String, Driver> f1DriverConsumer;
    private F1Consumer<String, Event> f1EventConsumer;
    private F1Consumer<String, Integer> f1DriverAvgSpeedConsumer;
    private F1Consumer<String, BestOverallSector> f1BestOverallSectorConsumer;

    public F1ConsumerApp(F1ConsumerAppConfig config) {
        this.config = config;
    }

    public static void main(String[] args) throws Exception {
        F1ConsumerAppConfig config = F1ConsumerAppConfig.fromEnv();
        F1ConsumerApp f1ConsumerApp = new F1ConsumerApp(config);

        log.info("Config: {}", config);

        f1ConsumerApp.start();
        System.in.read();
        f1ConsumerApp.stop();
    }

    public void start() {
        this.setupConsumers();

        this.executorService = Executors.newFixedThreadPool(4);
        this.executorService.submit(this.f1DriverConsumer);
        this.executorService.submit(this.f1EventConsumer);
        this.executorService.submit(this.f1DriverAvgSpeedConsumer);
        this.executorService.submit(this.f1BestOverallSectorConsumer);
    }

    public void stop() throws InterruptedException {
        this.f1DriverConsumer.stop();
        this.f1EventConsumer.stop();
        this.f1DriverAvgSpeedConsumer.stop();
        this.f1BestOverallSectorConsumer.stop();
        this.executorService.awaitTermination(10000, TimeUnit.MILLISECONDS);
        this.executorService.shutdownNow();
    }

    private void setupConsumers() {
        Properties propsF1DriverConsumer = F1ConsumerAppConfig.getProperties(config);
        propsF1DriverConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, config.getF1DriversGroupId());
        propsF1DriverConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        propsF1DriverConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.ppatierno.formula1.serializers.DriverDeserializer");
        this.f1DriverConsumer = new F1Consumer<>(propsF1DriverConsumer, Collections.singleton(config.getF1DriversTopic()));

        Properties propsF1EventConsumer = F1ConsumerAppConfig.getProperties(config);
        propsF1EventConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, config.getF1EventsGroupId());
        propsF1EventConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        propsF1EventConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.ppatierno.formula1.serializers.EventDeserializer");
        this.f1EventConsumer = new F1Consumer<>(propsF1EventConsumer, Collections.singleton(config.getF1EventsTopic()));

        Properties propsF1DriverAvgSpeedConsumer = F1ConsumerAppConfig.getProperties(config);
        propsF1DriverAvgSpeedConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, config.getF1DriversAvgSpeedGroupId());
        propsF1DriverAvgSpeedConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        propsF1DriverAvgSpeedConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        this.f1DriverAvgSpeedConsumer = new F1Consumer<>(propsF1DriverAvgSpeedConsumer, Collections.singleton(config.getF1DriversAvgSpeedTopic()));

        Properties propsF1BestOverallSectorConsumer = F1ConsumerAppConfig.getProperties(config);
        propsF1BestOverallSectorConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, config.getF1BestOverallSectorGroupId());
        propsF1BestOverallSectorConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ShortDeserializer");
        propsF1BestOverallSectorConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.ppatierno.formula1.serializers.BestOverallSectorDeserializer");
        this.f1BestOverallSectorConsumer = new F1Consumer<>(propsF1BestOverallSectorConsumer, Collections.singleton(config.getF1BestOverallSectorTopic()));
    }
}
