/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.ppatierno.formula1.camel.KafkaEndpoint;
import io.ppatierno.formula1.enums.Wheel;
import io.ppatierno.formula1.model.Driver;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Route getting Driver message from Apache Kafka and writing them to InfluxDB as data point
 */
public class DriversPointRouteBuilder extends RouteBuilder {

    private final F1KafkaInfluxDBAppConfig config;
    private final KafkaEndpoint kafkaEndpoint;

    public DriversPointRouteBuilder(F1KafkaInfluxDBAppConfig config) {
        this.config = config;
        KafkaEndpoint.KafkaEndpointBuilder kafkaEndpointBuilder = new KafkaEndpoint.KafkaEndpointBuilder()
                .withBootstrapServers(this.config.getCommon().getKafkaBootstrapServers())
                .withTopic(this.config.getF1DriversTopic())
                .withClientId("kafka-influxdb-drivers")
                .withGroupId("f1-kafka-influxdb-drivers-group")
                .withValueDeserializer("io.ppatierno.formula1.serializers.DriverDeserializer")
                .withTlsEnabled(this.config.getCommon().isKafkaTlsEnabled())
                .withTruststoreLocation(this.config.getCommon().getKafkaTruststoreLocation())
                .withTruststorePassword(this.config.getCommon().getKafkaTruststorePassword())
                .withSaslMechanism(this.config.getCommon().getKafkaSaslMechanism());

        if ("PLAIN".equals(this.config.getCommon().getKafkaSaslMechanism()) &&
                this.config.getCommon().getKafkaSaslUsername() != null && this.config.getCommon().getKafkaSaslPassword() != null) {
            String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + this.config.getCommon().getKafkaSaslUsername() + "\" password=\"" + this.config.getCommon().getKafkaSaslPassword() + "\";";
            kafkaEndpointBuilder.withSaslJassConfig(saslJaasConfig);
        }

        this.kafkaEndpoint = kafkaEndpointBuilder.build();
        log.info("KafkaEndpoint = {}", this.kafkaEndpoint);
    }

    @Override
    public void configure() throws Exception {
        from(this.kafkaEndpoint.toString())
        .filter(exchange -> {
            Driver driver = (Driver) exchange.getIn().getBody();
            // we are interested in Driver packets with telemetry data only (some have just participant data)
            return driver.hasValidTelemetry();
        })
        .process(exchange -> {

            Driver driver = (Driver) exchange.getIn().getBody();

            BatchPoints batchPoints = BatchPoints
                    .database(this.config.getInfluxDbDatabase())
                    .tag("driverid", driver.getParticipantData().getDriverId().name())
                    .tag("driverhashtag", driver.getHashtag())
                    .build();

            Point telemetryPoint = Point.measurement("telemetry")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("driverid", driver.getParticipantData().getDriverId().name())
                    .tag("driverhashtag", driver.getHashtag())
                    .addField("enginerpm", driver.getCarTelemetryData().getEngineRPM())
                    .addField("speed", driver.getCarTelemetryData().getSpeed())
                    .addField("throttle", driver.getCarTelemetryData().getThrottle())
                    .addField("steer", driver.getCarTelemetryData().getSteer())
                    .addField("brake", driver.getCarTelemetryData().getBrake())
                    .addField("gear", driver.getCarTelemetryData().getGear())
                    .addField("clutch", driver.getCarTelemetryData().getClutch())
                    .addField("rltyresurfacetemperature", driver.getCarTelemetryData().getTyresSurfaceTemperature()[Wheel.REAR_LEFT.getValue()])
                    .addField("rrtyresurfacetemperature", driver.getCarTelemetryData().getTyresSurfaceTemperature()[Wheel.REAR_RIGHT.getValue()])
                    .addField("fltyresurfacetemperature", driver.getCarTelemetryData().getTyresSurfaceTemperature()[Wheel.FRONT_LEFT.getValue()])
                    .addField("frtyresurfacetemperature", driver.getCarTelemetryData().getTyresSurfaceTemperature()[Wheel.FRONT_RIGHT.getValue()])
                    .addField("rlbrakestemperature", driver.getCarTelemetryData().getBrakesTemperature()[Wheel.REAR_LEFT.getValue()])
                    .addField("rrbrakestemperature", driver.getCarTelemetryData().getBrakesTemperature()[Wheel.REAR_RIGHT.getValue()])
                    .addField("flbrakestemperature", driver.getCarTelemetryData().getBrakesTemperature()[Wheel.FRONT_LEFT.getValue()])
                    .addField("frbrakestemperature", driver.getCarTelemetryData().getBrakesTemperature()[Wheel.FRONT_RIGHT.getValue()])
                    .build();

            Point motionPoint = Point.measurement("motion")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("driverid", driver.getParticipantData().getDriverId().name())
                    .tag("driverhashtag", driver.getHashtag())
                    .addField("glateral", driver.getCarMotionData().getgForceLateral())
                    .addField("glongitudinal", driver.getCarMotionData().getgForceLongitudinal())
                    .addField("gvertical", driver.getCarMotionData().getgForceVertical())
                    .build();

            Point carStatusPoint =  Point.measurement("carstatus")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("driverid", driver.getParticipantData().getDriverId().name())
                    .tag("driverhashtag", driver.getHashtag())
                    .addField("fuelintank", driver.getCarStatusData().getFuelInTank())
                    .addField("flwingdamage", driver.getCarStatusData().getFrontLeftWingDamage())
                    .addField("frwingdamage", driver.getCarStatusData().getFrontRightWingDamage())
                    .addField("rwingdamage", driver.getCarStatusData().getRearWingDamage())
                    .addField("rltyredamage", driver.getCarStatusData().getTyresDamage()[Wheel.REAR_LEFT.getValue()])
                    .addField("rrtyredamage", driver.getCarStatusData().getTyresDamage()[Wheel.REAR_RIGHT.getValue()])
                    .addField("fltyredamage", driver.getCarStatusData().getTyresDamage()[Wheel.FRONT_LEFT.getValue()])
                    .addField("frtyredamage", driver.getCarStatusData().getTyresDamage()[Wheel.FRONT_RIGHT.getValue()])
                    .addField("rltyrewear", driver.getCarStatusData().getTyresWear()[Wheel.REAR_LEFT.getValue()])
                    .addField("rrtyrewear", driver.getCarStatusData().getTyresWear()[Wheel.REAR_RIGHT.getValue()])
                    .addField("fltyrewear", driver.getCarStatusData().getTyresWear()[Wheel.FRONT_LEFT.getValue()])
                    .addField("frtyrewear", driver.getCarStatusData().getTyresWear()[Wheel.FRONT_RIGHT.getValue()])
                    .addField("tyrecompound", driver.getCarStatusData().getVisualTyreCompound().name())
                    .addField("tyresagelaps", driver.getCarStatusData().getTyresAgeLaps())
                    .build();

            Date currentLapTime = new Date((long)(driver.getLapData().getCurrentLapTime() * 1000));
            Date lastLapTime = new Date((long)(driver.getLapData().getLastLapTime() * 1000));
            Date bestLapTime = new Date((long)(driver.getLapData().getBestLapTime() * 1000));
            Point lapDataPoint = Point.measurement("lapdata")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("driverid", driver.getParticipantData().getDriverId().name())
                    .tag("driverhashtag", driver.getHashtag())
                    .addField("gridposition", driver.getLapData().getGridPosition())
                    .addField("carposition", driver.getLapData().getCarPosition())
                    .addField("currentlapnum", driver.getLapData().getCurrentLapNum())
                    .addField("lapdistance", driver.getLapData().getLapDistance())
                    .addField("currentlaptime", new SimpleDateFormat("m:ss.SSS").format(currentLapTime))
                    .addField("lastlaptime", new SimpleDateFormat("m:ss.SSS").format(lastLapTime))
                    .addField("bestlaptime", new SimpleDateFormat("m:ss.SSS").format(bestLapTime))
                    .addField("driverstatus", driver.getLapData().getDriverStatus().name())
                    .build();

            batchPoints.point(telemetryPoint);
            batchPoints.point(motionPoint);
            batchPoints.point(carStatusPoint);
            batchPoints.point(lapDataPoint);

            exchange.getIn().setBody(batchPoints);
        })
        .to("influxdb://connectionBean?databaseName=" + this.config.getInfluxDbDatabase() + "&retentionPolicy=autogen&batch=true")
        .routeId("kafka-influxdb-drivers")
        .log(LoggingLevel.TRACE, "${body}")
        .log(LoggingLevel.DEBUG, "Driver[${body.tags}]");
    }
}
