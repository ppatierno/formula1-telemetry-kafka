/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

public class F1KafkaInfluxDBAppConfig {

    private static final String KAFKA_BOOTSTRAP_SERVERS_ENV = "KAFKA_BOOTSTRAP_SERVERS";
    private static final String INFLUXDB_URL_ENV = "INFLUXDB_URL";

    private static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String DEFAULT_INFLUXDB_URL = "http://localhost:8086";

    private final String kafkaBootstrapServers;
    private final String influxDbUrl;

    private F1KafkaInfluxDBAppConfig(String kafkaBootstrapServers, String influxDbUrl) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.influxDbUrl = influxDbUrl;
    }

    public static F1KafkaInfluxDBAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        String influxDbUrl = System.getenv(INFLUXDB_URL_ENV) == null ? DEFAULT_INFLUXDB_URL : System.getenv(INFLUXDB_URL_ENV);
        return new F1KafkaInfluxDBAppConfig(kafkaBootstrapServers, influxDbUrl);
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    public String getInfluxDbUrl() {
        return influxDbUrl;
    }

    @Override
    public String toString() {
        return "F1KafkaInfluxDBAppConfig[" +
                "kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                ",influxDbUrl=" + this.influxDbUrl +
                "]";
    }
}
