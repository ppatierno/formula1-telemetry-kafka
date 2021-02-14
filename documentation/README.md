# Documentation

* [Build](#build)
    * [Components](#components)
    * [Docker images](#docker-images)
* [Running](#running)
    * [Apache Kafka, InfluxDB and Grafana stack](#apache-kafka-influxdb-and-grafana-stack)
    * [UDP to Apache Kafka](#udp-to-apache-kafka)
    * [Apache Kafka to InfluxDB](#apache-kafka-to-influxdb)
    * [Apache Kafka Streams](#apache-kafka-streams)
* [Running on OpenShift](../deployment/openshift/README.md)

## Build

### Components

This project has as prerequisite the [formula1-telemetry](https://github.com/ppatierno/formula1-telemetry) library, for decoding the raw UDP telemetry packets, that has to be installed in the local Maven repository.
In order to do so, follow the instructions on the corresponding GitHub repository.

After installing the decoding library, to build all the components, just run the following command.

```shell
mvn package
```

### Docker images

If you want to run the applications as containers even locally or on Kubernetes, a [Google Jib](https://github.com/GoogleContainerTools/jib) configuration is available for building the corresponding Docker images.

```shell
mvn package jib:dockerBuild
```

The above command will build the Docker images locally; you have to push them to the registry you prefer manually.

## Running

### Apache Kafka, InfluxDB and Grafana stack

The overall Apache Kafka, InfluxDB and Grafana stack can be deployed locally or even on Kubernetes.
For deploying it locally, just download the latest releases from the corresponding repositories or websites and follow the official instructions to run them.

* Apache Kafka: the latest release can be downloaded from [here](https://kafka.apache.org/downloads) and the [quickstart](https://kafka.apache.org/quickstart) can walk you through the deployment.
* InfluxDB: the latest release can be downloaded from [here](https://portal.influxdata.com/downloads/) and the [get started](https://docs.influxdata.com/influxdb/v2.0/get-started/) guide helps to run it.
* Grafana:  the latest release can be downloaded from [here](https://grafana.com/grafana/download) and the [installation](https://grafana.com/docs/grafana/latest/installation/) guide helps to install it.

For deploying it on Kubernetes, you can apply the Grafana and InfluxDB `Deployment` resources first.

```shell
kubectl apply -f deployment/influxdb.yaml
kubectl apply -f deployment/grafana.yaml
```

To access Grafana from outside the Kubernetes cluster you can create a corresponding `Ingress` resource (or even a `Route` is using OpenShift).
The dashboards can be imported from the `deployment/dashboard` folder. 

Regarding Apache Kafka, the simpler way is to use the [Strimzi](https://strimzi.io/) project, and you can find all the information on the official [documentation](https://strimzi.io/documentation/).
The quick start guide shows how to [install the operator](https://strimzi.io/docs/operators/latest/quickstart.html#proc-install-product-str) using the YAML files from the latest release.
Another way is to use the OperatorHub.io website where the latest operator release is available [here](https://operatorhub.io/operator/strimzi-kafka-operator) with all the instructions to install it.  

After installing the operator, you have to create your own `Kafka` custom resource for deploying the Apache Kafka cluster through the operator itself.
Some `Kafka` custom resource examples are available at the official Strimzi repository [here](https://github.com/strimzi/strimzi-kafka-operator/tree/master/examples/kafka).

The Apache Kafka cluster needs to be accessible from outside the Kubernetes cluster in order to allow the telemetry data coming from the UDP related application (not running on Kubernetes).
In order to do so, it is important to configure one "external" listener (with TLS enabled if you prefer).
More information are available in the [configuring external listeners](https://strimzi.io/docs/operators/latest/using.html#assembly-configuring-external-listeners-str) chapter.
If the TLS protocol is enabled on the external listener, you need to get the cluster CA certificate, and the corresponding password for allowing the UDP to Kafka application to connect.
More information are available in the [configuring external clients to trust the cluster CA](https://strimzi.io/docs/operators/latest/using.html#configuring-external-clients-to-trust-cluster-ca-str) chapter.

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

### Apache Kafka Streams

The Apache Kafka Streams application can run locally or can be deployed on Kubernetes; it depends on where the overall stack is running.
The main parameters for the application can be set via the following environment variables:

* `KAFKA_BOOTSTRAP_SERVERS`: the bootstrap servers for connecting to the Apache Kafka cluster. Default is `localhost:9092`.
* `F1_STREAMS_INPUT_TOPIC`: Apache Kafka topic from which `Driver` messages are read. Default is `f1-telemetry-drivers`.
* `F1_STREAMS_OUTPUT_TOPIC`: Apache Kafka topic to which messages with processed average speed are sent. Default is `f1-telemetry-drivers-avg-speed`.

You can set the environment variables locally and then running the application with following command.

```shell
java -jar f1-telemetry-streams/target/f1-telemetry-streams-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Or you can even deploy the application to Kubernetes by customizing the environment variables in the `env` section of the Apache Kafka Streams `Deployment` then applying the resource.

```shell
kubectl apply -f deployment/f1-telemetry-streams.yaml
```