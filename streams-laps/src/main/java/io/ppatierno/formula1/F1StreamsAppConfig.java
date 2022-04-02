/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import java.util.Properties;

import io.ppatierno.formula1.config.KafkaStreamsBaseConfig;

public class F1StreamsAppConfig extends KafkaStreamsBaseConfig {

    private static final String DEFAULT_F1_STREAMS_INPUT_TOPIC = "f1-telemetry-drivers";
    private static final String DEFAULT_F1_STREAMS_OUTPUT_TOPIC = "f1-telemetry-best-overall-sector";

    private F1StreamsAppConfig(String kafkaBootstrapServers, boolean kafkaTlsEnabled, String kafkaTruststoreLocation, String kafkaTruststorePassword,
                                String kafkaSaslMechanism, String kafkaSaslUsername, String kafkaSalsPassword,
                                int f1StreamsReplicationFactor, String f1StreamsInputTopic, String f1StreamsOutputTopic) {
        super(kafkaBootstrapServers, kafkaTlsEnabled, kafkaTruststoreLocation, kafkaTruststorePassword, 
                kafkaSaslMechanism, kafkaSaslUsername, kafkaSalsPassword,
                f1StreamsReplicationFactor, f1StreamsInputTopic, f1StreamsOutputTopic);
    }

    public static F1StreamsAppConfig fromEnv() {
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
        return new F1StreamsAppConfig(kafkaBootstrapServers, kafkaTlsEnabled, kafkaTruststoreLocation, kafkaTruststorePassword, 
                                        kafkaSaslMechanism, kafkaSaslUsername, kafkaSaslPassword,
                                        f1StreamsReplicationFactor, f1StreamsInputTopic, f1StreamsOutputTopic);
    }

    public static Properties getProperties(F1StreamsAppConfig config) {
        return KafkaStreamsBaseConfig.getProperties(config);
    }

    @Override
    public String toString() {
        return "F1StreamsAppConfig[" +
                "kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                ", kafkaTlsEnabled=" + this.kafkaTlsEnabled +
                ", kafkaTruststoreLocation=" +  this.kafkaTruststoreLocation +
                ", kafkaTruststorePassword=" +  this.kafkaTruststorePassword +
                ", kafkaSaslMechanism=" +  this.kafkaSaslMechanism +
                ", kafkaSaslUsername=" +  this.kafkaSaslUsername +
                ", kafkaSaslPassword=" +  this.kafkaSaslPassword +
                ", f1StreamsReplicationFactor=" + this.f1StreamsReplicationFactor +
                ", f1StreamsInputTopic=" + this.f1StreamsInputTopic +
                ", f1StreamsOutputTopic=" + this.f1StreamsOutputTopic +
                "]";
    }
}
