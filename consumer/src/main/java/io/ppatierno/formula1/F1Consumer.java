/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class F1Consumer<K, V> implements Runnable {

    private final Logger log = LoggerFactory.getLogger(F1Consumer.class);

    private final AtomicBoolean consuming = new AtomicBoolean(true);
    private final Properties props;
    private final Collection<String> topics;

    public F1Consumer(Properties props, Collection<String> topics) {
        this.props = props;
        this.topics = topics;
    }

    public void stop() {
        this.consuming.set(false);
    }

    @Override
    public void run() {
        KafkaConsumer<K, V> consumer = null;

        try {
            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(topics);

            while (consuming.get()) {
                ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<K, V> record : records) {
                    log.info("Record topic = {}, partition = {}, key = {}, value = {}",
                            record.topic(), record.partition(), record.key(), record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (consumer != null) {
                log.info("Close consumer");
                consumer.close();
            }
        }
    }
}
