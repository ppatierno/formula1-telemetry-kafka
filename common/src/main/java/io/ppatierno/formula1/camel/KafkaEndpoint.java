/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1.camel;

public class KafkaEndpoint {

    private final String bootstrapServers;
    private final String clientId;
    private final String groupId;
    private final String topic;
    private final String valueSerializer;
    private final String valueDeserializer;
    private final boolean tlsEnabled;
    private final String truststoreLocation;
    private final String truststorePassword;
    private final String saslMechanism;
    private final String saslJaasConfig;

    private KafkaEndpoint(String bootstrapServers, String clientId, String groupId, 
                            String topic, String valueSerializer, String valueDeserializer,
                            boolean tlsEnabled, String truststoreLocation, String truststorePassword,
                            String saslMechanism, String saslJaasConfig) {
        this.bootstrapServers = bootstrapServers;
        this.clientId = clientId;
        this.groupId = groupId;
        this.topic = topic;
        this.valueSerializer = valueSerializer;
        this.valueDeserializer = valueDeserializer;
        this.tlsEnabled = tlsEnabled;
        this.truststoreLocation = truststoreLocation;
        this.truststorePassword = truststorePassword;
        this.saslMechanism = saslMechanism;
        this.saslJaasConfig = saslJaasConfig;
    }

    @Override
    public String toString() {
        String securityProtocol = null;
        StringBuilder sb = new StringBuilder("kafka:" + this.topic + "?");
        sb.append("brokers=" + this.bootstrapServers);
        sb.append("&clientId=" + this.clientId);
        if (this.groupId != null) {
            sb.append("&groupId=" + this.groupId);
        }
        if (this.valueSerializer != null) {
            sb.append("&valueSerializer=" + this.valueSerializer);
        }
        if (this.valueDeserializer != null) {
            sb.append("&valueDeserializer=" + this.valueDeserializer);
        }
        if (this.tlsEnabled) {
            securityProtocol = "SSL";
        }
        if (this.truststoreLocation != null && this.truststorePassword != null) {
            sb.append("&sslTruststoreLocation=" + this.truststoreLocation);
            sb.append("&sslTruststorePassword=" + this.truststorePassword);
            sb.append("&sslTruststoreType=PKCS12");
        }
        if (this.saslMechanism != null && this.saslJaasConfig != null) {
            sb.append("&saslMechanism=" + this.saslMechanism);
            sb.append("&saslJaasConfig=" + this.saslJaasConfig);
            securityProtocol = "SSL".equals(securityProtocol) ? "SASL_SSL" : "SASL_PLAINTEXT";
        }
        if (securityProtocol != null) {
            sb.append("&securityProtocol=" + securityProtocol);
        }
        return sb.toString();
    }

    public static class KafkaEndpointBuilder {
        private String bootstrapServers;
        private String clientId;
        private String groupId;
        private String topic;
        private String valueSerializer;
        private String valueDeserializer;
        private boolean tlsEnabled;
        private String truststoreLocation;
        private String truststorePassword;
        private String saslMechanism;
        private String saslJaasConfig;

        public KafkaEndpointBuilder withBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
            return this;
        }

        public KafkaEndpointBuilder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public KafkaEndpointBuilder withGroupId(String groupId) {
            this.groupId = groupId;
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

        public KafkaEndpointBuilder withValueDeserializer(String valueDeserializer) {
            this.valueDeserializer = valueDeserializer;
            return this;
        }

        public KafkaEndpointBuilder withTlsEnabled(boolean tlsEnabled) {
            this.tlsEnabled = tlsEnabled;
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

        public KafkaEndpointBuilder withSaslMechanism(String saslMechanism) {
            this.saslMechanism = saslMechanism;
            return this;
        }

        public KafkaEndpointBuilder withSaslJassConfig(String saslJaasConfig) {
            this.saslJaasConfig = saslJaasConfig;
            return this;
        }

        public KafkaEndpoint build() {
            return new KafkaEndpoint(this.bootstrapServers, this.clientId, this.groupId, 
                    this.topic, this.valueSerializer, this.valueDeserializer,
                    this.tlsEnabled, this.truststoreLocation, this.truststorePassword,
                    this.saslMechanism, this.saslJaasConfig);
        }
    }
}
