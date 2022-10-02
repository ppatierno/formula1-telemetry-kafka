# Running on OpenShift

This documentation explains how to run the entire F1 Telemetry pipeline on an OpenShift cluster.

## Installing Strimzi Operator

Use the Operator Hub to search for the Strimzi operator and install it.

## Create Formula-1 project

Create a dedicated project/namespace to deploy the applications.

```shell
kubectl create namespace f1-telemetry
```

## Deploy Apache Kafka cluster

In order to deploy the Apache Kafka cluster using the Strimzi operator, run the following command to create a `Kafka` resource.

```shell
kubectl -n f1-telemetry apply -f deployment/openshift/kafka.yaml
```

The Apache Kafka cluster is exposed outside the OpenShift cluster via OpenShift `Route`(s).
When the Apache Kafka cluster is up and running, apply the `KafkaTopic` resources for creating the needed topics.

```shell
kubectl -n f1-telemetry apply -f deployment/openshift/kafka-topics.yaml
```

## Deploy InfluxDB and Grafana

In order to deploy InfluxDB and Grafana, run the following command.

```shell
kubectl -n f1-telemetry apply -f deployment/influxdb.yaml
kubectl -n f1-telemetry apply -f deployment/grafana.yaml
kubectl -n f1-telemetry apply -f deployment/openshift/grafana-route.yaml
```

To make the Grafana dashboard accessible, an OpenShift `Route` is created as well.
Get the URL of the Grafana UI.

```shell
kubectl -n f1-telemetry get route grafana -o=jsonpath='{.spec.host}'
```

Use the Grafana UI to configure InfluxDB as datasource with `http://influxdb:8086` as URL and `formula1` as Database. 
Finally, import the dashboards from the `deployment/dashboard` folder.

## Deploy F1 Telemetry applications

The F1 Telemetry applications which need to run on the OpenShift cluster are the Apache Kafka to InfluxDB and Kafka Streams ones.
In order to deploy them, run the following command.

```shell
kubectl -n f1-telemetry apply -f deployment/f1-telemetry-kafka-influxdb.yaml
kubectl -n f1-telemetry apply -f deployment/f1-telemetry-streams-avg-speed.yaml
```

> NOTE: their configuration don't need any change if using the default `Kafka` resource provided by example.

## Run the F1 Telemetry UDP to Apache Kafka locally

The UDP to Apache Kafka application has to run locally or anyway within the same network where the F1 2020 game is running (on your preferred console, i.e. Xbox).
First, set the environment variables to configure the applications with the bootstrap servers address, and the TLS truststore for accessing the Apache Kafka cluster via TLS.

```shell
export KAFKA_BOOTSTRAP_SERVERS=$(kubectl -n f1-telemetry get kafka my-cluster -o=jsonpath='{.status.listeners[?(@.type=="external")].bootstrapServers}{"\n"}')
kubectl -n f1-telemetry get secret my-cluster-cluster-ca-cert -o jsonpath='{.data.ca\.p12}' | base64 -d > ca.p12
kubectl -n f1-telemetry get secret my-cluster-cluster-ca-cert -o jsonpath='{.data.ca\.password}' | base64 -d > ca.password
export KAFKA_TRUSTSTORE_LOCATION=$(pwd)/ca.p12
export KAFKA_TRUSTSTORE_PASSWORD=$(cat ca.password)
export KAFKA_TLS_ENABLED=true
```

Finally, run the application.

```shell
java -jar udp-kafka/target/f1-telemetry-udp-kafka-1.0-SNAPSHOT-jar-with-dependencies.jar
```