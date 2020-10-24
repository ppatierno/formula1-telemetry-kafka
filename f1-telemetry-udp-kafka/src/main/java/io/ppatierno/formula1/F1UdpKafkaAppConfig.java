/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

public class F1UdpKafkaAppConfig {

    private static final String KAFKA_BOOTSTRAP_SERVERS_ENV = "KAFKA_BOOTSTRAP_SERVERS";
    private static final String UDP_PORT_ENV = "UDP_PORT";

    private static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final int DEFAULT_UDP_PORT = 20777;

    private final int udpPort;
    private final String kafkaBootstrapServers;

    private F1UdpKafkaAppConfig(String kafkaBootstrapServers, int udpPort) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.udpPort = udpPort;
    }

    public static F1UdpKafkaAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        int udpPort = System.getenv(UDP_PORT_ENV) == null ? DEFAULT_UDP_PORT : Integer.parseInt(System.getenv(UDP_PORT_ENV));
        return new F1UdpKafkaAppConfig(kafkaBootstrapServers, udpPort);
    }

    public int getUdpPort() {
        return udpPort;
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    @Override
    public String toString() {
        return "F1UdpKafkaAppConfig[" +
                "udpPort=" + this.udpPort +
                ",kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                "]";
    }
}
