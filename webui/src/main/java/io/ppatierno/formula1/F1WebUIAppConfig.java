package io.ppatierno.formula1;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import io.ppatierno.formula1.config.KafkaCommonConfig;

public class F1WebUIAppConfig {

    private static final String F1_DRIVERS_GROUP_ID_ENV = "F1_DRIVERS_GROUP_ID";
    private static final String F1_DRIVERS_TOPIC_ENV = "F1_DRIVERS_TOPIC";

    private static final String DEFAULT_F1_DRIVERS_GROUP_ID = "f1-drivers-webgroup";
    private static final String DEFAULT_F1_DRIVERS_TOPIC = "f1-telemetry-drivers";

    private final KafkaCommonConfig common;
    private final String f1DriversGroupId;
    private final String f1DriversTopic;

    private F1WebUIAppConfig(KafkaCommonConfig common, String f1DriversGroupId, String f1DriversTopic) {
        this.common = common;
        this.f1DriversGroupId = f1DriversGroupId;
        this.f1DriversTopic = f1DriversTopic;
    }

    public static F1WebUIAppConfig fromEnv() {
        KafkaCommonConfig common = KafkaCommonConfig.fromEnv();
        String f1DriversGroupId = System.getenv(F1_DRIVERS_GROUP_ID_ENV) == null ? DEFAULT_F1_DRIVERS_GROUP_ID : System.getenv(F1_DRIVERS_GROUP_ID_ENV);
        String f1DriversTopic = System.getenv(F1_DRIVERS_TOPIC_ENV) == null ? DEFAULT_F1_DRIVERS_TOPIC : System.getenv(F1_DRIVERS_TOPIC_ENV);
        return new F1WebUIAppConfig(common, f1DriversGroupId, f1DriversTopic);
    }

    public static Properties getProperties(F1WebUIAppConfig config) {
        Properties props = KafkaCommonConfig.getProperties(config.common);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getF1DriversGroupId());
        return props;
    }

    public KafkaCommonConfig getCommon() {
        return common;
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
                "common=" + this.common +
                ", f1DriversGroupId=" + this.f1DriversGroupId +
                ", f1DriversTopic=" + this.f1DriversTopic +
                "]";
    }
}
