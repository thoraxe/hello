package com.redhat.developers.msa.reactive.hello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.circuitbreaker.CircuitBreaker;
import io.vertx.ext.circuitbreaker.CircuitBreakerOptions;


/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HelloVerticle extends AbstractVerticle {

  private CircuitBreaker breaker;

  @Override
  public void start() throws Exception {
    breaker = CircuitBreaker.create("hello", vertx,
        new CircuitBreakerOptions(config().getJsonObject("breaker"))
    );

    vertx.eventBus().<String>consumer("hello").handler(this::reply);
    vertx.eventBus().<String>consumer("hello/chain").handler(this::chain);
    vertx.eventBus().<String>consumer("hello/health").handler(this::health);
  }


  private void reply(Message<String> message) {
    String name = message.body();
    message.reply(response(name, null));
  }

  private void chain(Message<String> message) {
    String name = message.body();

    breaker.executeWithFallback(
        future ->
            vertx.eventBus().send("ola/chain", message.body(), ar -> {
              if (ar.failed()) {
                future.fail(ar.cause());
              } else {
                message.reply(response(name, (JsonObject) ar.result().body()));
                future.complete();
              }
            }),
        v -> message.reply(response(name, null))
    );
  }

  private Object response(String name, JsonObject response) {
    return (response == null ? new JsonObject().put("ola", "failed!") : response)
        .put("hello", "Hello " + name);
  }

  private void health(Message<String> message) {
    message.reply("I'm ok");
  }
}
