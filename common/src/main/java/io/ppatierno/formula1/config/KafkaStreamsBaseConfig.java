/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.config;

/**
 * Base configuration for any Kafka Streams application
 */
public class KafkaStreamsBaseConfig extends KafkaBaseConfig {

    public static final String F1_STREAMS_INPUT_TOPIC_ENV = "F1_STREAMS_INPUT_TOPIC";
    public static final String F1_STREAMS_OUTPUT_TOPIC_ENV = "F1_STREAMS_OUTPUT_TOPIC";

    private static final String DEFAULT_F1_STREAMS_INPUT_TOPIC = "f1-telemetry-input-topic";
    private static final String DEFAULT_F1_STREAMS_OUTPUT_TOPIC = "f1-telemetry-output-topic";

    protected final String f1StreamsInputTopic;
    protected final String f1StreamsOutputTopic;

    protected KafkaStreamsBaseConfig(String kafkaBootstrapServers, String f1StreamsInputTopic, String f1StreamsOutputTopic) {
        super(kafkaBootstrapServers, false, null, null, null, null, null);
        this.f1StreamsInputTopic = f1StreamsInputTopic;
        this.f1StreamsOutputTopic = f1StreamsOutputTopic;
    }

    public static KafkaStreamsBaseConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        String f1StreamsInputTopic = System.getenv(F1_STREAMS_INPUT_TOPIC_ENV) == null ? DEFAULT_F1_STREAMS_INPUT_TOPIC : System.getenv(F1_STREAMS_INPUT_TOPIC_ENV);
        String f1StreamsOutputTopic = System.getenv(F1_STREAMS_OUTPUT_TOPIC_ENV) == null ? DEFAULT_F1_STREAMS_OUTPUT_TOPIC : System.getenv(F1_STREAMS_OUTPUT_TOPIC_ENV);
        return new KafkaStreamsBaseConfig(kafkaBootstrapServers, f1StreamsInputTopic, f1StreamsOutputTopic);
    }

    public String getF1StreamsInputTopic() {
        return f1StreamsInputTopic;
    }

    public String getF1StreamsOutputTopic() {
        return f1StreamsOutputTopic;
    }
}
