package io.ppatierno.formula1;

import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;

import io.ppatierno.formula1.config.KafkaBaseConfig;

public class F1WebUIAppConfig extends KafkaBaseConfig {

    private static final String F1_DRIVERS_GROUP_ID_ENV = "F1_DRIVERS_GROUP_ID";
    private static final String F1_DRIVERS_TOPIC_ENV = "F1_DRIVERS_TOPIC";

    private static final String DEFAULT_F1_DRIVERS_GROUP_ID = "f1-drivers-webgroup";
    private static final String DEFAULT_F1_DRIVERS_TOPIC = "f1-telemetry-drivers";

    private final String f1DriversGroupId;
    private final String f1DriversTopic;

    private F1WebUIAppConfig(String kafkaBootstrapServers, boolean kafkaTlsEnabled, String kafkaTruststoreLocation, String kafkaTruststorePassword,
                                String kafkaSaslMechanism, String kafkaSaslUsername, String kafkaSalsPassword,
                                String f1DriversGroupId, String f1DriversTopic) {
        super(kafkaBootstrapServers, kafkaTlsEnabled, kafkaTruststoreLocation, kafkaTruststorePassword, kafkaSaslMechanism, kafkaSaslUsername, kafkaSalsPassword);
        this.f1DriversGroupId = f1DriversGroupId;
        this.f1DriversTopic = f1DriversTopic;
    }

    public static F1WebUIAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        boolean kafkaTlsEnabled = System.getenv(KAFKA_TLS_ENABLED) == null ? DEFAULT_KAFKA_TLS_ENABLED : Boolean.parseBoolean(System.getenv(KAFKA_TLS_ENABLED));
        String kafkaTruststoreLocation = System.getenv(KAFKA_TRUSTSTORE_LOCATION_ENV);
        String kafkaTruststorePassword = System.getenv(KAFKA_TRUSTSTORE_PASSWORD_ENV);
        String kafkaSaslMechanism = System.getenv(KAFKA_SASL_MECHANISM);
        String kafkaSaslUsername = System.getenv(KAFKA_SASL_USERNAME);
        String kafkaSaslPassword = System.getenv(KAFKA_SASL_PASSWORD);
        String f1DriversGroupId = System.getenv(F1_DRIVERS_GROUP_ID_ENV) == null ? DEFAULT_F1_DRIVERS_GROUP_ID : System.getenv(F1_DRIVERS_GROUP_ID_ENV);
        String f1DriversTopic = System.getenv(F1_DRIVERS_TOPIC_ENV) == null ? DEFAULT_F1_DRIVERS_TOPIC : System.getenv(F1_DRIVERS_TOPIC_ENV);
        return new F1WebUIAppConfig(kafkaBootstrapServers, kafkaTlsEnabled, kafkaTruststoreLocation, kafkaTruststorePassword,
                                    kafkaSaslMechanism, kafkaSaslUsername, kafkaSaslPassword,
                                    f1DriversGroupId, f1DriversTopic);
    }

    public static Properties getProperties(F1WebUIAppConfig config) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafkaBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getF1DriversGroupId());
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

    public String getF1DriversGroupId() {
        return f1DriversGroupId;
    }

    public String getF1DriversTopic() {
        return f1DriversTopic;
    }

    @Override
    public String toString() {
        return "F1WebUIAppConfig[" +
                "kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                ", kafkaTlsEnabled=" + this.kafkaTlsEnabled +
                ", kafkaTruststoreLocation=" +  this.kafkaTruststoreLocation +
                ", kafkaTruststorePassword=" +  this.kafkaTruststorePassword +
                ", kafkaSaslMechanism=" +  this.kafkaSaslMechanism +
                ", kafkaSaslUsername=" +  this.kafkaSaslUsername +
                ", kafkaSaslPassword=" +  this.kafkaSaslPassword +
                ", f1DriversGroupId=" + this.f1DriversGroupId +
                ", f1DriversTopic=" + this.f1DriversTopic +
                "]";
    }
}
