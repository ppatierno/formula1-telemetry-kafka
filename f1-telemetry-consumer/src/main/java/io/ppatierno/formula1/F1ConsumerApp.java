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

    private AtomicBoolean consuming = new AtomicBoolean(true);
    private ExecutorService executorService;

    public static void main(String[] args) throws Exception {

        F1ConsumerApp f1ConsumerApp = new F1ConsumerApp();

        f1ConsumerApp.start();
        System.in.read();
        f1ConsumerApp.stop();
    }

    public void start() {
        this.executorService = Executors.newFixedThreadPool(1);
        this.executorService.submit(new F1Consumer());
    }

    public void stop() throws InterruptedException {
        this.consuming.set(false);
        this.executorService.awaitTermination(10000, TimeUnit.MILLISECONDS);
        this.executorService.shutdownNow();
    }

    private class F1Consumer implements Runnable {

        private Logger log = LoggerFactory.getLogger(F1Consumer.class);

        @Override
        public void run() {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "f1-drivers-group");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.ppatierno.formula1.DriverDeserializer");

            KafkaConsumer<String, Driver> consumer = null;

            try {
                consumer = new KafkaConsumer<>(props);
                consumer.subscribe(Collections.singleton("f1-telemetry-drivers"));

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
}
