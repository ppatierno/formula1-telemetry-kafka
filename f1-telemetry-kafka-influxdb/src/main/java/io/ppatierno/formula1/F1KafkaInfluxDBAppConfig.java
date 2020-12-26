/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

public class F1KafkaInfluxDBAppConfig {

    private static final String KAFKA_BOOTSTRAP_SERVERS_ENV = "KAFKA_BOOTSTRAP_SERVERS";
    private static final String INFLUXDB_URL_ENV = "INFLUXDB_URL";
    private static final String INFLUXDB_DB_ENV = "INFLUXDB_DB";
    private static final String F1_DRIVERS_TOPIC_ENV = "F1_DRIVERS_TOPIC";
    private static final String F1_EVENTS_TOPIC_ENV = "F1_EVENTS_TOPIC";

    private static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String DEFAULT_INFLUXDB_URL = "http://localhost:8086";
    private static final String DEFAULT_INFLUXDB_DB = "formula1";
    private static final String DEFAULT_F1_DRIVERS_TOPIC = "f1-telemetry-drivers";
    private static final String DEFAULT_F1_EVENTS_TOPIC = "f1-telemetry-events";

    private final String kafkaBootstrapServers;
    private final String influxDbUrl;
    private final String influxDbDatabase;
    private final String f1DriversTopic;
    private final String f1EventsTopic;

    private F1KafkaInfluxDBAppConfig(String kafkaBootstrapServers, String influxDbUrl, String influxDbDatabase, String f1DriversTopic, String f1EventsTopic) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.influxDbUrl = influxDbUrl;
        this.influxDbDatabase = influxDbDatabase;
        this.f1DriversTopic = f1DriversTopic;
        this.f1EventsTopic = f1EventsTopic;
    }

    public static F1KafkaInfluxDBAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        String influxDbUrl = System.getenv(INFLUXDB_URL_ENV) == null ? DEFAULT_INFLUXDB_URL : System.getenv(INFLUXDB_URL_ENV);
        String influxDbDatabase = System.getenv(INFLUXDB_DB_ENV) == null ? DEFAULT_INFLUXDB_DB : System.getenv(INFLUXDB_DB_ENV);
        String f1DriversTopic = System.getenv(F1_DRIVERS_TOPIC_ENV) == null ? DEFAULT_F1_DRIVERS_TOPIC : System.getenv(F1_DRIVERS_TOPIC_ENV);
        String f1EventsTopic = System.getenv(F1_EVENTS_TOPIC_ENV) == null ? DEFAULT_F1_EVENTS_TOPIC : System.getenv(F1_EVENTS_TOPIC_ENV);
        return new F1KafkaInfluxDBAppConfig(kafkaBootstrapServers, influxDbUrl, influxDbDatabase, f1DriversTopic, f1EventsTopic);
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    public String getInfluxDbUrl() {
        return influxDbUrl;
    }

    public String getInfluxDbDatabase() {
        return influxDbDatabase;
    }

    public String getF1DriversTopic() {
        return f1DriversTopic;
    }

    public String getF1EventsTopic() {
        return f1EventsTopic;
    }

    @Override
    public String toString() {
        return "F1KafkaInfluxDBAppConfig[" +
                "kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                ", influxDbUrl=" + this.influxDbUrl +
                ", influxDbDatabase=" + this.influxDbDatabase +
                ", f1DriversTopic=" + this.f1DriversTopic +
                ", f1EventsTopic=" + this.f1EventsTopic +
                "]";
    }
}
