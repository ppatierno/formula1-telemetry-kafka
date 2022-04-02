/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;

import java.util.Properties;

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

    public static Properties getProperties(KafkaBaseConfig config) {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, config.getKafkaBootstrapServers());
        if (config.isKafkaTlsEnabled()) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
            if (config.getKafkaTruststoreLocation() != null && config.getKafkaTruststorePassword() != null) {
                props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "PKCS12");
                props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, config.getKafkaTruststoreLocation());
                props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, config.getKafkaTruststorePassword());
            }
        }

        if ("PLAIN".equals(config.getKafkaSaslMechanism()) &&
                config.getKafkaSaslUsername() != null && config.getKafkaSaslPassword() != null) {
            props.put(SaslConfigs.SASL_MECHANISM, config.getKafkaSaslMechanism());
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL".equals(props.getProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG)) ? "SASL_SSL" : "SASL_PLAINTEXT");
            String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + config.getKafkaSaslUsername() + "\" password=\"" + config.getKafkaSaslPassword() + "\";";
            props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
        }
        return props;
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
