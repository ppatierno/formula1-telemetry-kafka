/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class F1WebServer extends AbstractVerticle {

    private static Logger log = LoggerFactory.getLogger(F1WebServer.class);

    private F1WebUIAppConfig config;

    public F1WebServer(F1WebUIAppConfig config) {
        this.config = config;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        log.info("Starting F1 Telemetry Web Server");

        Router router = Router.router(vertx);

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        SockJSBridgeOptions options = new SockJSBridgeOptions();
        options.addOutboundPermitted(new PermittedOptions().setAddress("f1-race-ranking"));
        router.mountSubRouter("/eventbus", sockJSHandler.bridge(options));

        router.route().handler(StaticHandler.create());

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, done -> {
                    if (done.succeeded()) {
                        log.info("F1 Telemetry Web Server started successfully");
                        // create and start the consumer for Drivers
                        F1WebConsumer consumer = new F1WebConsumer(vertx, config);
                        consumer.start();
                        startPromise.complete();
                    } else {
                        log.error("F1 Telemetry Web Server failed to start", done.cause());
                        startPromise.fail(done.cause());
                    }
                });
    }
}
