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
    protected static final String KAFKA_TLS_ENABLED = "KAFKA_TLS_ENABLED";
    protected static final String KAFKA_TRUSTSTORE_LOCATION_ENV = "KAFKA_TRUSTSTORE_LOCATION";
    protected static final String KAFKA_TRUSTSTORE_PASSWORD_ENV = "KAFKA_TRUSTSTORE_PASSWORD";
    protected static final String KAFKA_SASL_MECHANISM = "KAFKA_SASL_MECHANISM";
    protected static final String KAFKA_SASL_USERNAME = "KAFKA_SASL_USERNAME";
    protected static final String KAFKA_SASL_PASSWORD = "KAFKA_SASL_PASSWORD";

    protected static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    protected static final boolean DEFAULT_KAFKA_TLS_ENABLED = false;

    protected final String kafkaBootstrapServers;
    protected final boolean kafkaTlsEnabled;
    protected final String kafkaTruststoreLocation;
    protected final String kafkaTruststorePassword;
    protected final String kafkaSaslMechanism;
    protected final String kafkaSaslUsername;
    protected final String kafkaSaslPassword;

    protected KafkaBaseConfig(String kafkaBootstrapServers, boolean kafkaTlsEnabled, String kafkaTruststoreLocation, String kafkaTruststorePassword,
                                String kafkaSaslMechanism, String kafkaSaslUsername, String kafkaSaslPassword) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.kafkaTlsEnabled = kafkaTlsEnabled;                            
        this.kafkaTruststoreLocation = kafkaTruststoreLocation;
        this.kafkaTruststorePassword = kafkaTruststorePassword;
        this.kafkaSaslMechanism = kafkaSaslMechanism;
        this.kafkaSaslUsername = kafkaSaslUsername;
        this.kafkaSaslPassword = kafkaSaslPassword;
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }    

    public boolean isKafkaTlsEnabled() {
        return kafkaTlsEnabled;
    }

    public String getKafkaTruststoreLocation() {
        return kafkaTruststoreLocation;
    }

    public String getKafkaTruststorePassword() {
        return kafkaTruststorePassword;
    }

    public String getKafkaSaslMechanism() {
        return kafkaSaslMechanism;
    }

    public String getKafkaSaslUsername() {
        return kafkaSaslUsername;
    }

    public String getKafkaSaslPassword() {
        return kafkaSaslPassword;
    }    
}
