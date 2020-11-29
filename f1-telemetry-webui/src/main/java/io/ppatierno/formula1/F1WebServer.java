/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class F1WebServer extends AbstractVerticle {

    private static Logger log = LoggerFactory.getLogger(F1WebServer.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        log.info("Starting F1 Telemetry Web Server");
        Router router = Router.router(vertx);
        router.route().handler(StaticHandler.create());

        // TODO: configure the SockJSHandler for bridging through the eventbus to JavaScript

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, done -> {
                    if (done.succeeded()) {
                        log.info("F1 Telemetry Web Server started successfully");
                        // TODO: start the Vert.x Kafka client consumer
                        startPromise.complete();
                    } else {
                        log.error("F1 Telemetry Web Server failed to start", done.cause());
                        startPromise.fail(done.cause());
                    }
                });
    }
}
