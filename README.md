[![Build](https://github.com/ppatierno/formula1-telemetry-kafka/actions/workflows/build.yml/badge.svg)](https://github.com/ppatierno/formula1-telemetry-kafka/actions/workflows/build.yml)

# Formula 1 - Telemetry with Apache Kafka

This project aims to use Apache Kafka in order to ingest Formula 1 telemetry data from the F1 2020 game (by CodeMasters) running on Microsoft Xbox.
It uses different products, projects and technologies:

* **[F1 2020 game (by CodeMasters)](https://www.codemasters.com/game/f1-2020/)** for getting the telemetry data via UDP;
* **[Apache Camel](https://camel.apache.org/)** project for routing telemetry events from UDP to Apache Kafka and to InfluxDB;
* **[Apache Kafka](https://kafka.apache.org/)** as the core project for ingesting the telemetry events on different topics;
* **[Kubernetes](https://kubernetes.io/)** for deploying most of the components to run in the cloud;
* **[Strimzi](https://strimzi.io/)** for deploying easily the Apache Kafka on Kubernetes;
* **[InfluxDB](https://www.influxdata.com/)** for storing the telemetry time series as data source for dashboards;
* **[Grafana](https://grafana.com/)** for providing dashboards showing the real time telemetry data;  

![Logo](./images/f1-telemetry-kafka-logo.png)

## Overview

Following an overall picture of how these technologies are used together.
A couple of short introduction videos are available [here](https://youtu.be/Re9LOAYZi2A) and [here](https://youtu.be/2efOtyFAZ4s). 

![Overview](./images/overview.png)

### F1 2020 Xbox UDP to Kafka

In order to ingest the telemetry events into Apache Kafka, the Apache Camel project is used with:

* a route getting the raw UDP packets from the F1 2020 game (by CodeMasters) on Microsoft Xbox and dispatching these events to three more routes;
    * a route just forwarding the raw UDP packets to a corresponding Apache Kafka topic;
    * a route to filter only the `EVENT` type raw UDP packets and forwarding them to a corresponding Apache Kafka topic;
    * a route to aggregate the raw UDP packets data for producing drivers related data and forwarding them to a corresponding Apache Kafka topic;

![UDP to Kafka](./images/f1-telemetry-udp-kafka.png)

### Kafka Streams API

The Kafka Streams API based application shows an example of real-time analytics on the telemetry data.
The application processes the average speed in the last 5 seconds.

![Kafka Streams API](./images/f1-telemetry-streams-avg-speed.png)

It has a source node reading from the topic with drivers related messages, filtering the ones not containing valid telemetry data.
Extract and group the driver's speed by corresponding driver's id using in a tumbling window of 5 seconds.
Next, it sums speeds and counts them in order to process the corresponding average value.
Finally, the sink node writes to the destination topic.

![Kafka Streams API Topology](./images/f1-telemetry-streams-avg-speed-topology.png)

### Kafka to InfluxDB

In order to provide the telemetry data to Grafana dashboards, InfluxDB is used as data source and the telemetry events are stored through Apache Camel with:

* a route getting drivers related data for storing telemetry, motion and car status data;
* a route getting the `EVENT` type raw UDP packets for storing fastest lap and speedtrap events;

![Kafka to InfluxDB](./images/f1-telemetry-kafka-influxdb.png)

## Repository structure

Contains different components for ingesting and handling Formula 1 2020 game (by CodeMasters) telemetry data through Apache Kafka.

* **udp-kafka**: Apache Camel application bridging the telemetry packets got on UDP from the Formula 1 2020 game to Apache Kafka to different topics:
    * _f1-telemetry-packets_ contains the raw `Packet`(s);
    * _f1-telemetry-events_ contains only the raw `Packet`(s) of `EVENT` type;
    * _f1-telemetry-drivers_ contains the `Driver` messages as result of aggregating `Packet`(s) in the same frame with telemetry data for all drivers;
* **consumer**: Apache Kafka client application consuming `Driver` messages from Apache Kafka;
* **streams-avg-speed**: Apache Kafka Streams API based application getting raw `Driver`(s), processing them in real time to get average speed in a 5 seconds window and writing to Apache Kafka to the _f1-telemetry-streams-avg-speed_ topic; 
* **common**: common library providing model classes and related Apache Kafka serializer/deserializer;
* **kafka-influxdb**: Apache Camel application writing driver/car's telemetry data  to InfluxDB as time series; 
* **webui**: A sample Web application showing the race ranking in real time getting `Driver`(s) from Apache Kafka;
* **dashboard**: folder containing Grafana dashboard showing driver/car's telemetry data;
* **deployment**: folder containing Kubernetes deployments for all the provided applications;
* **documentation**: folder containing documentation about how to deploy the entire solution;

The Formula 1 2020 game UDP packets specification is [here](https://forums.codemasters.com/topic/50942-f1-2020-udp-specification/).
The library used for decoding the packets is [here](https://github.com/ppatierno/formula1-telemetry).

## Documentation

You can find more information about building, deploying and running the solution in the documentation [here](documentation/README.md)

## Dashboards

The telemetry dashboard shows information like speed, engine (rpm), throttle and brake.

![Telemetry](./images/01-telemetry.png)

The motion dashboard shows information about the car in motion like the G-force.

![Motion](./images/02-motion.png)

The car status dashboard shows information mostly related to the status of each car, for example wings damages and fuel in tank.

![Car Status](./images/03-carstatus.png)

The events dashboard shows race events like max speed trap and fastest lap.

![Events](./images/04-events.png)

The driver dashboard allows to get data for a single driver, selecting one through the corresponding drop down list. 
It has a top bar with information about current lap, position, distance and lap times.

![Driver](./images/05-drivers.png)

It also has some graphs showing specific driver data related to throttle/brake, engine/speed, gear/clutch, brakes and steering.

![Driver](./images/06-drivers.png)

![Driver](./images/07-drivers.png)

It also shows specific information about tyres like, for example, the compound, the age laps, wear, surface temperature and damage

![Driver](images/08-drivers.png)

![Driver](images/09-drivers.png)

The streams dashboard shows data about processed telemetry data through Kafka Streams application.
The first one is about the average speed during the last 5 seconds.

![Streams](images/10-streams.png)