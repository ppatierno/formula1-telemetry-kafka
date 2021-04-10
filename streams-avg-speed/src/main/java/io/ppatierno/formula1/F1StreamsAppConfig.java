/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.config.KafkaBaseConfig;

public class F1StreamsAppConfig extends KafkaBaseConfig {

    private static final String F1_STREAMS_INPUT_TOPIC_ENV = "F1_STREAMS_INPUT_TOPIC";
    private static final String F1_STREAMS_OUTPUT_TOPIC_ENV = "F1_STREAMS_OUTPUT_TOPIC";

    private static final String DEFAULT_F1_STREAMS_INPUT_TOPIC = "f1-telemetry-drivers";
    private static final String DEFAULT_F1_STREAMS_OUTPUT_TOPIC = "f1-telemetry-drivers-avg-speed";

    private final String f1StreamsInputTopic;
    private final String f1StreamsOutputTopic;

    private F1StreamsAppConfig(String kafkaBootstrapServers, String f1StreamsInputTopic, String f1StreamsOutputTopic) {
        super(kafkaBootstrapServers, null, null);
        this.f1StreamsInputTopic = f1StreamsInputTopic;
        this.f1StreamsOutputTopic = f1StreamsOutputTopic;
    }

    public static F1StreamsAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        String f1StreamsInputTopic = System.getenv(F1_STREAMS_INPUT_TOPIC_ENV) == null ? DEFAULT_F1_STREAMS_INPUT_TOPIC : System.getenv(F1_STREAMS_INPUT_TOPIC_ENV);
        String f1StreamsOutputTopic = System.getenv(F1_STREAMS_OUTPUT_TOPIC_ENV) == null ? DEFAULT_F1_STREAMS_OUTPUT_TOPIC : System.getenv(F1_STREAMS_OUTPUT_TOPIC_ENV);
        return new F1StreamsAppConfig(kafkaBootstrapServers, f1StreamsInputTopic, f1StreamsOutputTopic);
    }

    public String getF1StreamsInputTopic() {
        return f1StreamsInputTopic;
    }

    public String getF1StreamsOutputTopic() {
        return f1StreamsOutputTopic;
    }

    @Override
    public String toString() {
        return "F1StreamsAppConfig[" +
                "kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                ", f1StreamsInputTopic=" + this.f1StreamsInputTopic +
                ", f1StreamsOutputTopic=" + this.f1StreamsOutputTopic +
                "]";
    }
}
