/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.config.KafkaBaseConfig;

public class F1StreamsAppConfig extends KafkaBaseConfig {

    public F1StreamsAppConfig(String kafkaBootstrapServers) {
        super(kafkaBootstrapServers, null, null);
    }

    public static F1StreamsAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        return new F1StreamsAppConfig(kafkaBootstrapServers);
    }

    @Override
    public String toString() {
        return "F1StreamsAppConfig[" +
                "kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                "]";
    }
}
