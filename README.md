# formula1-telemetry-kafka

Contains different components for ingesting and handling Formula 1 2020 game (by CodeMasters) telemetry data through Apache Kafka.

* f1-telemetry-udp-kafka: Apache Camel application bridging the telemetry packets got on UDP from the Formula 1 2020 game to Apache Kafka in the raw format packet and as a `Driver` instance;
* f1-telemetry-consumer: Apache Kafka client application consuming `Driver` messages from Apache Kafka;
* f1-telemetry-common: common library providing model classes and related Apache Kafka serializer/deserializer;