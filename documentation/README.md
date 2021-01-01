# Documentation

## Build

In order to build all the components, just run the following command.

```shell
mvn package
```

If you want to run the applications as containers even locally or on Kubernetes, a [Google Jib](https://github.com/GoogleContainerTools/jib) configuration is available for building the corresponding Docker images.

```shell
mvn package jib:dockerBuild
```

The above command will build the Docker images locally; you have to push them to the registry you prefer manually.

## Running

### Apache Kafka, InfluxDB and Grafana stack

TBD

### UDP to Apache Kafka

The UDP to Apache Kafka application has to run locally or anyway within the same network where the F1 2020 game is running (on your preferred console, i.e. Xbox).
In this way, it gets the raw telemetry packets sent by the game over UDP and bridges them to Apache Kafka topics.
The main parameters for the application can be set via the following environment variables:

* `KAFKA_BOOTSTRAP_SERVERS`: the bootstrap servers for connecting to the Apache Kafka cluster. Default is `localhost:9092`.
* `KAFKA_TRUSTSTORE_LOCATION`: the path to the truststore containing certificates to connect to the Apache Kafka cluster when TLS is enabled. Not set by default.
* `KAFKA_TRUSTSTORE_PASSWORD`: the password for the truststore containing certificates to connect to the Apache Kafka cluster when TLS is enabled. No set by default.

Other available environment variables are:

* `UDP_PORT`: the UDP port on which listening for the raw telemetry packets coming from the F1 2020 game. Default is `20777`.
* `F1_DRIVERS_TOPIC`: Apache Kafka topic to which `Driver` messages are sent. Default is `f1-telemetry-drivers`.
* `F1_EVENTS_TOPIC`: Apache Kafka topic to which `Event` messages are sent. Default is `f1-telemetry-events`.
* `F1_RAW_PACKETS_TOPIC`: Apache Kafka topic to which raw `Packet` messages are sent. Default is `f1-telemetry-packets`.

After setting the needed environment variables, you can start the application running the following command:

```shell
java -jar f1-telemetry-udp-kafka/target/f1-telemetry-udp-kafka-1.0-SNAPSHOT-jar-with-dependencies.jar
```

The application starts listening on UDP, connects to the Apache Kafka cluster, and the Apache Camel routes are activated for bridging the packets from UDP to topics.

### Apache Kafka to InfluxDB

The Apache Kafka to InfluxDB application can run locally or can be deployed on Kubernetes; it depends on where the overall stack is running.
The main parameters for the application can be set via the following environment variables:

* `KAFKA_BOOTSTRAP_SERVERS`: the bootstrap servers for connecting to the Apache Kafka cluster. Default is `localhost:9092`.
* `INFLUXDB_URL`: the URL of the InfluxDB HTTP REST API. Default is `http://localhost:8086`.

Other available environment variables are:

* `INFLUXDB_DB`: the InfluxDB database where measurements will be stored. Default is `formula1`.
* `F1_DRIVERS_TOPIC`: Apache Kafka topic from which `Driver` messages are read. Default is `f1-telemetry-drivers`. 
* `F1_EVENTS_TOPIC`: Apache Kafka topic from which `Event` messages are read. Default is `f1-telemetry-events`.

You can set the environment variables locally and then running the application with following command.

```shell
java -jar f1-telemetry-kafka-influxdb/target/f1-telemetry-kafka-influxdb-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Or you can even deploy the application to Kubernetes by customizing the environment variables in the `env` section of the Apache Kafka to InfluxDB `Deployment` then applying the resource.

```shell
kubectl apply -f deployment/f1-telemetry-kafka-influxdb.yaml
```