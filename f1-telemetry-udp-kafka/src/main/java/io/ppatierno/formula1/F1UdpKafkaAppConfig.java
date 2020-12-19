/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

public class F1UdpKafkaAppConfig {

    private static final String KAFKA_BOOTSTRAP_SERVERS_ENV = "KAFKA_BOOTSTRAP_SERVERS";
    private static final String UDP_PORT_ENV = "UDP_PORT";
    private static final String F1_DRIVERS_TOPIC_ENV = "F1_DRIVERS_TOPIC";
    private static final String F1_EVENTS_TOPIC_ENV = "F1_EVENTS_TOPIC";
    private static final String F1_RAW_PACKETS_TOPIC_ENV = "F1_RAW_PACKETS_TOPIC";

    private static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final int DEFAULT_UDP_PORT = 20777;
    private static final String DEFAULT_F1_DRIVERS_TOPIC = "f1-telemetry-drivers";
    private static final String DEFAULT_F1_EVENTS_TOPIC = "f1-telemetry-events";
    private static final String DEFAULT_F1_RAW_PACKETS_TOPIC = "f1-telemetry-packets";

    private final int udpPort;
    private final String kafkaBootstrapServers;
    private final String f1DriversTopic;
    private final String f1EventsTopic;
    private final String f1RawPacketsTopic;

    private F1UdpKafkaAppConfig(String kafkaBootstrapServers, int udpPort, String f1DriversTopic, String f1EventsTopic, String f1RawPacketsTopic) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.udpPort = udpPort;
        this.f1DriversTopic = f1DriversTopic;
        this.f1EventsTopic = f1EventsTopic;
        this.f1RawPacketsTopic = f1RawPacketsTopic;
    }

    public static F1UdpKafkaAppConfig fromEnv() {
        String kafkaBootstrapServers = System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV) == null ? DEFAULT_KAFKA_BOOTSTRAP_SERVERS : System.getenv(KAFKA_BOOTSTRAP_SERVERS_ENV);
        int udpPort = System.getenv(UDP_PORT_ENV) == null ? DEFAULT_UDP_PORT : Integer.parseInt(System.getenv(UDP_PORT_ENV));
        String f1DriversTopic = System.getenv(F1_DRIVERS_TOPIC_ENV) == null ? DEFAULT_F1_DRIVERS_TOPIC : System.getenv(F1_DRIVERS_TOPIC_ENV);
        String f1EventsTopic = System.getenv(F1_EVENTS_TOPIC_ENV) == null ? DEFAULT_F1_EVENTS_TOPIC : System.getenv(F1_EVENTS_TOPIC_ENV);
        String f1RawPacketsTopic = System.getenv(F1_RAW_PACKETS_TOPIC_ENV) == null ? DEFAULT_F1_RAW_PACKETS_TOPIC : System.getenv(F1_RAW_PACKETS_TOPIC_ENV);
        return new F1UdpKafkaAppConfig(kafkaBootstrapServers, udpPort, f1DriversTopic, f1EventsTopic, f1RawPacketsTopic);
    }

    public int getUdpPort() {
        return udpPort;
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    public String getF1DriversTopic() {
        return f1DriversTopic;
    }

    public String getF1EventsTopic() {
        return f1EventsTopic;
    }

    public String getF1RawPacketsTopic() {
        return f1RawPacketsTopic;
    }

    @Override
    public String toString() {
        return "F1UdpKafkaAppConfig[" +
                "udpPort=" + this.udpPort +
                ",kafkaBootstrapServers=" + this.kafkaBootstrapServers +
                ", f1DriversTopic=" + this.f1DriversTopic +
                ", f1EventsTopic=" + this.f1EventsTopic +
                ", f1RawPacketsTopic=" + this.f1RawPacketsTopic +
                "]";
    }
}
