package io.ppatierno.formula1;

public class F1ConsumerAppConfig {

    private static final String KAFKA_BOOTSTRAP_SERVERS_ENV = "KAFKA_BOOTSTRAP_SERVERS";

    private static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";

    private final String kafkaBootstrapServers;

    private F1ConsumerAppConfig(String kafkaBootstrapServers) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
    }

    public static F1ConsumerAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        return new F1ConsumerAppConfig(kafkaBootstrapServers);
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    @Override
    public String toString() {
        return "F1ConsumerAppConfig[" +
                "kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                "]";
    }
}
