/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.model.Driver;
import io.ppatierno.formula1.model.Telemetry;
import io.ppatierno.formula1.serializers.DriverDeserializer;
import io.ppatierno.formula1.serializers.DriverSerializer;
import io.ppatierno.formula1.serializers.JsonDeserializer;
import io.ppatierno.formula1.serializers.JsonSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class F1StreamsApp {

    private static Logger log = LoggerFactory.getLogger(F1StreamsApp.class);

    public static void main(String[] args) {
        F1StreamsAppConfig config = F1StreamsAppConfig.fromEnv();

        log.info("Config: {}", config);

        Properties props = F1StreamsAppConfig.getProperties(config);
        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "f1-telemetry-streams-telemetry");
        props.setProperty(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        Serde<Driver> driverSerdes = Serdes.serdeFrom(new DriverSerializer(), new DriverDeserializer());
        Serde<Telemetry> telemetrySerdes = Serdes.serdeFrom(new JsonSerializer<Telemetry>(), new JsonDeserializer<Telemetry>());

        KStream<String, Telemetry> telemetryStream =
        streamsBuilder
                .stream(config.getStreamsCommon().getF1StreamsInputTopic(), Consumed.with(Serdes.String(), driverSerdes))
                .filter((driverId, driver) -> driver.hasValidTelemetry() && driver.getLapData().getLapDistance() > 0)
                .map((driverId, driver) -> new KeyValue<>(driver.getHashtag(), new Telemetry(driver)));

        telemetryStream
                .to(config.getStreamsCommon().getF1StreamsOutputTopic(), Produced.with(Serdes.String(), telemetrySerdes));

        Topology topology = streamsBuilder.build();
        log.info("{}", topology.describe());

        KafkaStreams kafkaStreams = new KafkaStreams(topology, props);

        CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                kafkaStreams.close();
                latch.countDown();
            }
        });

        try {
            kafkaStreams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
