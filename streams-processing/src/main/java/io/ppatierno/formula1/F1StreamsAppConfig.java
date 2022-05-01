/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.config.KafkaStreamsCommonConfig;

import java.util.Properties;

public class F1StreamsAppConfig {

    private static final String DEFAULT_F1_STREAMS_INPUT_TOPIC = "f1-telemetry-drivers";
    private static final String DEFAULT_F1_STREAMS_OUTPUT_TOPIC = "f1-telemetry-drivers-telemetry";

    private final KafkaStreamsCommonConfig streamsCommon;

    private F1StreamsAppConfig(KafkaStreamsCommonConfig streamsCommon) {
        this.streamsCommon = streamsCommon;
    }

    public static F1StreamsAppConfig fromEnv() {
        KafkaStreamsCommonConfig streamsCommon = KafkaStreamsCommonConfig.fromEnv(DEFAULT_F1_STREAMS_INPUT_TOPIC, DEFAULT_F1_STREAMS_OUTPUT_TOPIC);
        return new F1StreamsAppConfig(streamsCommon);
    }

    public static Properties getProperties(F1StreamsAppConfig config) {
        return KafkaStreamsCommonConfig.getProperties(config.streamsCommon);
    }

    public KafkaStreamsCommonConfig getStreamsCommon() {
        return streamsCommon;
    }

    @Override
    public String toString() {
        return "F1StreamsAppConfig[" +
                "streamsCommon=" + this.streamsCommon +
                "]";
    }
}
