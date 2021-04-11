/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.config;

/**
 * Base configuration for any Kafka application
 */
public class KafkaBaseConfig {

    protected static final String KAFKA_BOOTSTRAP_SERVERS_ENV = "KAFKA_BOOTSTRAP_SERVERS";
    protected static final String KAFKA_TRUSTSTORE_LOCATION_ENV = "KAFKA_TRUSTSTORE_LOCATION";
    protected static final String KAFKA_TRUSTSTORE_PASSWORD_ENV = "KAFKA_TRUSTSTORE_PASSWORD";

    protected static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";

    protected final String kafkaBootstrapServers;
    protected final String kafkaTruststoreLocation;
    protected final String kafkaTruststorePassword;

    protected KafkaBaseConfig(String kafkaBootstrapServers, String kafkaTruststoreLocation, String kafkaTruststorePassword) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.kafkaTruststoreLocation = kafkaTruststoreLocation;
        this.kafkaTruststorePassword = kafkaTruststorePassword;
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    public String getKafkaTruststoreLocation() {
        return kafkaTruststoreLocation;
    }

    public String getKafkaTruststorePassword() {
        return kafkaTruststorePassword;
    }
}
