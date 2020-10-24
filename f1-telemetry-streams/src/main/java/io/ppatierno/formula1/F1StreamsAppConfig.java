/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

public class F1StreamsAppConfig {

    private static final String KAFKA_BOOTSTRAP_SERVERS_ENV = "KAFKA_BOOTSTRAP_SERVERS";

    private static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";

    private final String kafkaBootstrapServers;

    private F1StreamsAppConfig(String kafkaBootstrapServers) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
    }

    public static F1StreamsAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        return new F1StreamsAppConfig(kafkaBootstrapServers);
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    @Override
    public String toString() {
        return "F1StreamsAppConfig[" +
                "kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                "]";
    }
}

