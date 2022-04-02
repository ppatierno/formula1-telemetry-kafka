/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.config;

import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

/**
 * Base configuration for any Kafka Streams application
 */
public class KafkaStreamsBaseConfig extends KafkaBaseConfig {

    public static final String F1_STREAMS_INTERNAL_REPLICATION_FACTOR_ENV = "F1_STREAMS_INTERNAL_REPLICATION_FACTOR";
    public static final String F1_STREAMS_INPUT_TOPIC_ENV = "F1_STREAMS_INPUT_TOPIC";
    public static final String F1_STREAMS_OUTPUT_TOPIC_ENV = "F1_STREAMS_OUTPUT_TOPIC";

    protected static final int DEFAULT_F1_STREAMS_INTERNAL_REPLICATION_FACTOR = 1;
    private static final String DEFAULT_F1_STREAMS_INPUT_TOPIC = "f1-telemetry-input-topic";
    private static final String DEFAULT_F1_STREAMS_OUTPUT_TOPIC = "f1-telemetry-output-topic";

    protected final int f1StreamsReplicationFactor;
    protected final String f1StreamsInputTopic;
    protected final String f1StreamsOutputTopic;

    protected KafkaStreamsBaseConfig(String kafkaBootstrapServers, boolean kafkaTlsEnabled, String kafkaTruststoreLocation, String kafkaTruststorePassword,
                                        String kafkaSaslMechanism, String kafkaSaslUsername, String kafkaSalsPassword,
                                        int f1StreamsReplicationFactor, String f1StreamsInputTopic, String f1StreamsOutputTopic) {
        super(kafkaBootstrapServers, kafkaTlsEnabled, kafkaTruststoreLocation, kafkaTruststorePassword, kafkaSaslMechanism, kafkaSaslUsername, kafkaSalsPassword);
        this.f1StreamsReplicationFactor = f1StreamsReplicationFactor;
        this.f1StreamsInputTopic = f1StreamsInputTopic;
        this.f1StreamsOutputTopic = f1StreamsOutputTopic;
    }

    public static KafkaStreamsBaseConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        boolean kafkaTlsEnabled = System.getenv(KAFKA_TLS_ENABLED) == null ? DEFAULT_KAFKA_TLS_ENABLED : Boolean.parseBoolean(System.getenv(KAFKA_TLS_ENABLED));
        String kafkaTruststoreLocation = System.getenv(KAFKA_TRUSTSTORE_LOCATION_ENV);
        String kafkaTruststorePassword = System.getenv(KAFKA_TRUSTSTORE_PASSWORD_ENV);
        String kafkaSaslMechanism = System.getenv(KAFKA_SASL_MECHANISM);
        String kafkaSaslUsername = System.getenv(KAFKA_SASL_USERNAME);
        String kafkaSaslPassword = System.getenv(KAFKA_SASL_PASSWORD);
        int f1StreamsReplicationFactor = System.getenv(F1_STREAMS_INTERNAL_REPLICATION_FACTOR_ENV) == null ? DEFAULT_F1_STREAMS_INTERNAL_REPLICATION_FACTOR : Integer.parseInt(System.getenv(F1_STREAMS_INTERNAL_REPLICATION_FACTOR_ENV));
        String f1StreamsInputTopic = System.getenv(F1_STREAMS_INPUT_TOPIC_ENV) == null ? DEFAULT_F1_STREAMS_INPUT_TOPIC : System.getenv(F1_STREAMS_INPUT_TOPIC_ENV);
        String f1StreamsOutputTopic = System.getenv(F1_STREAMS_OUTPUT_TOPIC_ENV) == null ? DEFAULT_F1_STREAMS_OUTPUT_TOPIC : System.getenv(F1_STREAMS_OUTPUT_TOPIC_ENV);
        return new KafkaStreamsBaseConfig(kafkaBootstrapServers, kafkaTlsEnabled, kafkaTruststoreLocation, kafkaTruststorePassword, 
                                            kafkaSaslMechanism, kafkaSaslUsername, kafkaSaslPassword,
                                            f1StreamsReplicationFactor, f1StreamsInputTopic, f1StreamsOutputTopic);
    }

    public static Properties getProperties(KafkaStreamsBaseConfig config) {
        Properties props = KafkaBaseConfig.getProperties(config);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, config.getF1StreamsReplicationFactor());
        return props;
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
}
