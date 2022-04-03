/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.config;

import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

/**
 * Common configuration for any Kafka Streams application
 */
public class KafkaStreamsCommonConfig {

    public static final String F1_STREAMS_INTERNAL_REPLICATION_FACTOR_ENV = "F1_STREAMS_INTERNAL_REPLICATION_FACTOR";
    public static final String F1_STREAMS_INPUT_TOPIC_ENV = "F1_STREAMS_INPUT_TOPIC";
    public static final String F1_STREAMS_OUTPUT_TOPIC_ENV = "F1_STREAMS_OUTPUT_TOPIC";

    protected static final int DEFAULT_F1_STREAMS_INTERNAL_REPLICATION_FACTOR = 1;
    private static final String DEFAULT_F1_STREAMS_INPUT_TOPIC = "f1-telemetry-input-topic";
    private static final String DEFAULT_F1_STREAMS_OUTPUT_TOPIC = "f1-telemetry-output-topic";

    protected final KafkaCommonConfig common;
    protected final int f1StreamsReplicationFactor;
    protected final String f1StreamsInputTopic;
    protected final String f1StreamsOutputTopic;

    protected KafkaStreamsCommonConfig(KafkaCommonConfig common, int f1StreamsReplicationFactor, String f1StreamsInputTopic, String f1StreamsOutputTopic) {
        this.common = common;
        this.f1StreamsReplicationFactor = f1StreamsReplicationFactor;
        this.f1StreamsInputTopic = f1StreamsInputTopic;
        this.f1StreamsOutputTopic = f1StreamsOutputTopic;
    }

    public static KafkaStreamsCommonConfig fromEnv(String defaultF1StreamsInputTopic, String defaultF1StreamsOutputTopic) {
        KafkaCommonConfig common = KafkaCommonConfig.fromEnv();
        int f1StreamsReplicationFactor = System.getenv(F1_STREAMS_INTERNAL_REPLICATION_FACTOR_ENV) == null ? DEFAULT_F1_STREAMS_INTERNAL_REPLICATION_FACTOR : Integer.parseInt(System.getenv(F1_STREAMS_INTERNAL_REPLICATION_FACTOR_ENV));
        String f1StreamsInputTopic = System.getenv(F1_STREAMS_INPUT_TOPIC_ENV) == null ? defaultF1StreamsInputTopic : System.getenv(F1_STREAMS_INPUT_TOPIC_ENV);
        String f1StreamsOutputTopic = System.getenv(F1_STREAMS_OUTPUT_TOPIC_ENV) == null ? defaultF1StreamsOutputTopic : System.getenv(F1_STREAMS_OUTPUT_TOPIC_ENV);
        return new KafkaStreamsCommonConfig(common, f1StreamsReplicationFactor, f1StreamsInputTopic, f1StreamsOutputTopic);
    }

    public static Properties getProperties(KafkaStreamsCommonConfig config) {
        Properties props = KafkaCommonConfig.getProperties(config.common);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, config.getF1StreamsReplicationFactor());
        return props;
    }

    public KafkaCommonConfig getCommon() {
        return common;
    }

    public int getF1StreamsReplicationFactor() {
        return f1StreamsReplicationFactor;
    }

    public String getF1StreamsInputTopic() {
        return f1StreamsInputTopic;
    }

    public String getF1StreamsOutputTopic() {
        return f1StreamsOutputTopic;
    }

    @Override
    public String toString() {
        return "KafkaStreamsCommonConfig[" +
                "common=" + this.common +
                ", f1StreamsReplicationFactor=" + this.f1StreamsReplicationFactor +
                ", f1StreamsInputTopic=" + this.f1StreamsInputTopic +
                ", f1StreamsOutputTopic=" + this.f1StreamsOutputTopic +
                ']';
    }
}
