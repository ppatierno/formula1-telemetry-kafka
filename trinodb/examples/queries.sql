-- query catalogs, shows "kafka" catalog
SHOW CATALOGS;

-- query schemas in "kafka" catalog, shows "formula1" schema
SHOW SCHEMAS FROM kafka;

-- query tables from "kafka.formula1" schema, shows "drivers_telemetry" table
SHOW TABLES FROM kafka.formula1;

-- describe columns
DESCRIBE kafka.formula1.drivers_telemetry;

SELECT COUNT(*) FROM kafka.formula1.drivers_telemetry;

SELECT * FROM kafka.formula1.drivers_telemetry LIMIT 10;

-- avg speed using the columns mapped by Trino from the JSON configuration
SELECT drivershortname, round(avg(speed),2) AS avgspeed
FROM kafka.formula1.drivers_telemetry
GROUP BY drivershortname ORDER BY avgspeed DESC;

-- avg speed extracting data from the JSON message in the builtin _message column
SELECT drivershortname, round(avg(speed),2) AS avgspeed
FROM (
    SELECT json_query(_message, 'lax $.driverShortName') AS drivershortname, cast(json_query(_message, 'lax $.speed') AS DOUBLE) AS speed
    FROM kafka.formula1.drivers_telemetry
    )
GROUP BY drivershortname ORDER BY avgspeed DESC;

-- max speed
SELECT drivershortname, round(max(speed),2) AS maxspeed
FROM kafka.formula1.drivers_telemetry
GROUP BY drivershortname ORDER BY maxspeed DESC;

-- drivers order (top 10) per lap processing the grouping per driver with the max distance
-- also generate a new column as position based on the order of the max distance
SELECT drivershortname, maxdistance, row_number() OVER (ORDER BY maxdistance DESC) AS position
FROM (
    SELECT drivershortname, max(distance) AS maxdistance
    FROM kafka.formula1.drivers_telemetry WHERE lap = 1
    GROUP BY drivershortname
    )
ORDER BY position ASC LIMIT 10;

-- drivers order (top 10) processing the grouping per driver with the max total distance
-- also generate a new column as position based on the order of the max total distance
SELECT drivershortname, maxtotaldistance, row_number() OVER (ORDER BY maxtotaldistance DESC) AS position
FROM (
    SELECT drivershortname, max(totaldistance) AS maxtotaldistance
    FROM kafka.formula1.drivers_telemetry
    GROUP BY drivershortname
    )
ORDER BY position ASC LIMIT 10;