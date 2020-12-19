/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class F1ConsumerApp {

    private static Logger log = LoggerFactory.getLogger(F1ConsumerApp.class);

    private AtomicBoolean consuming = new AtomicBoolean(true);
    private ExecutorService executorService;
    private F1ConsumerAppConfig config;

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
        this.executorService = Executors.newFixedThreadPool(2);
        this.executorService.submit(new F1DriverConsumer(this.config));
        this.executorService.submit(new F1EventConsumer(this.config));
    }

    public void stop() throws InterruptedException {
        this.consuming.set(false);
        this.executorService.awaitTermination(10000, TimeUnit.MILLISECONDS);
        this.executorService.shutdownNow();
    }

    private class F1DriverConsumer implements Runnable {

        private Logger log = LoggerFactory.getLogger(F1DriverConsumer.class);

        private F1ConsumerAppConfig config;

        public F1DriverConsumer(F1ConsumerAppConfig config) {
            this.config = config;
        }

        @Override
        public void run() {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafkaBootstrapServers());
            props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getF1DriversGroupId());
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.ppatierno.formula1.DriverDeserializer");

            KafkaConsumer<String, Driver> consumer = null;

            try {
                consumer = new KafkaConsumer<>(props);
                consumer.subscribe(Collections.singleton(config.getF1DriversTopic()));

                while (consuming.get()) {
                    ConsumerRecords<String, Driver> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, Driver> record : records) {
                        log.info("Driver record topic = {}, partition = {}, key = {}, value = {}",
                                record.topic(), record.partition(), record.key(), record.value());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (consumer != null)
                    consumer.close();
            }
        }
    }

    private class F1EventConsumer implements Runnable {

        private Logger log = LoggerFactory.getLogger(F1EventConsumer.class);

        private F1ConsumerAppConfig config;

        public F1EventConsumer(F1ConsumerAppConfig config) {
            this.config = config;
        }

        @Override
        public void run() {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafkaBootstrapServers());
            props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getF1EventsGroupId());
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.ppatierno.formula1.EventDeserializer");

            KafkaConsumer<String, Event> consumer = null;

            try {
                consumer = new KafkaConsumer<>(props);
                consumer.subscribe(Collections.singleton(config.getF1EventsTopic()));

                while (consuming.get()) {
                    ConsumerRecords<String, Event> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, Event> record : records) {
                        log.info("Event record topic = {}, partition = {}, key = {}, value = {}",
                                record.topic(), record.partition(), record.key(), record.value());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (consumer != null)
                    consumer.close();
            }
        }
    }
}
