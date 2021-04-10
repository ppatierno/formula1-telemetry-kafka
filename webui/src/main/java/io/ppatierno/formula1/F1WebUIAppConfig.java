package io.ppatierno.formula1;

import io.ppatierno.formula1.config.KafkaBaseConfig;

public class F1WebUIAppConfig extends KafkaBaseConfig {

    private static final String F1_DRIVERS_GROUP_ID_ENV = "F1_DRIVERS_GROUP_ID";
    private static final String F1_DRIVERS_TOPIC_ENV = "F1_DRIVERS_TOPIC";

    private static final String DEFAULT_F1_DRIVERS_GROUP_ID = "f1-drivers-webgroup";
    private static final String DEFAULT_F1_DRIVERS_TOPIC = "f1-telemetry-drivers";

    private final String f1DriversGroupId;
    private final String f1DriversTopic;

    private F1WebUIAppConfig(String kafkaBootstrapServers, String f1DriversGroupId, String f1DriversTopic) {
        super(kafkaBootstrapServers, null, null);
        this.f1DriversGroupId = f1DriversGroupId;
        this.f1DriversTopic = f1DriversTopic;
    }

    public static F1WebUIAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        String f1DriversGroupId = System.getenv(F1_DRIVERS_GROUP_ID_ENV) == null ? DEFAULT_F1_DRIVERS_GROUP_ID : System.getenv(F1_DRIVERS_GROUP_ID_ENV);
        String f1DriversTopic = System.getenv(F1_DRIVERS_TOPIC_ENV) == null ? DEFAULT_F1_DRIVERS_TOPIC : System.getenv(F1_DRIVERS_TOPIC_ENV);
        return new F1WebUIAppConfig(kafkaBootstrapServers, f1DriversGroupId, f1DriversTopic);
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
                ", f1DriversGroupId=" + this.f1DriversGroupId +
                ", f1DriversTopic=" + this.f1DriversTopic +
                "]";
    }
}
