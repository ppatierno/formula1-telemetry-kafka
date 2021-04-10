/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

public class KafkaEndpoint {

    private final String bootstrapServers;
    private final String clientId;
    private final String topic;
    private final String valueSerializer;
    private final String truststoreLocation;
    private final String truststorePassword;

    private KafkaEndpoint(String bootstrapServers, String clientId, String topic,
                          String valueSerializer, String truststoreLocation, String truststorePassword) {
        this.bootstrapServers = bootstrapServers;
        this.clientId = clientId;
        this.topic = topic;
        this.valueSerializer = valueSerializer;
        this.truststoreLocation = truststoreLocation;
        this.truststorePassword = truststorePassword;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("kafka:" + this.topic + "?");
        sb.append("brokers=" + this.bootstrapServers);
        sb.append("&clientId=" + this.clientId);
        if (this.valueSerializer != null) {
            sb.append("&valueSerializer=" + this.valueSerializer);
        }
        if (this.truststoreLocation != null && this.truststorePassword != null) {
            sb.append("&sslTruststoreLocation=" + this.truststoreLocation);
            sb.append("&sslTruststorePassword=" + this.truststorePassword);
            sb.append("&sslTruststoreType=PKCS12&securityProtocol=SSL");
        }
        return sb.toString();
    }

    public static class KafkaEndpointBuilder {
        private String bootstrapServers;
        private String clientId;
        private String topic;
        private String valueSerializer;
        private String truststoreLocation;
        private String truststorePassword;

        public KafkaEndpointBuilder withBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
            return this;
        }

        public KafkaEndpointBuilder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public KafkaEndpointBuilder withTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public KafkaEndpointBuilder withValueSerializer(String valueSerializer) {
            this.valueSerializer = valueSerializer;
            return this;
        }

        public KafkaEndpointBuilder withTruststoreLocation(String truststoreLocation) {
            this.truststoreLocation = truststoreLocation;
            return this;
        }

        public KafkaEndpointBuilder withTruststorePassword(String truststorePassword) {
            this.truststorePassword = truststorePassword;
            return this;
        }

        public KafkaEndpoint build() {
            return new KafkaEndpoint(this.bootstrapServers, this.clientId, this.topic,
                    this.valueSerializer, this.truststoreLocation, this.truststorePassword);
        }
    }
}
