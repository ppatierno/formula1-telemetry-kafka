/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class F1WebUIApp {

    private static Logger log = LoggerFactory.getLogger(F1WebUIApp.class);

    public static void main(String[] args) {
        log.info("Season {}", PacketConfig.getSeason());
        F1WebUIAppConfig config = F1WebUIAppConfig.fromEnv();
        log.info("Config: {}", config);

        Vertx vertx = Vertx.vertx();
        F1WebServer f1WebServer = new F1WebServer(config);
        vertx.deployVerticle(f1WebServer, done -> {
            if (!done.succeeded()) {
                System.exit(1);
            }
        });
    }
}
