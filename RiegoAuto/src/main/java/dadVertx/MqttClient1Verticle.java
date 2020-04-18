package dadVertx;

import java.util.Random;

import types.SensorValue;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.impl.MqttClientImpl;

public class MqttClient1Verticle extends AbstractVerticle {
	private static Boolean sync = new Boolean(true);
	private String classInstanceId;

	public void start(Promise<Void> promise) {
		classInstanceId = this.hashCode() + "";
		MqttClientOptions options = new MqttClientOptions();
		options.setAutoKeepAlive(true);
		options.setAutoGeneratedClientId(false);
		options.setClientId(classInstanceId);
		options.setConnectTimeout(5000);
		options.setCleanSession(true);
		options.setKeepAliveTimeSeconds(10000);
		options.setReconnectAttempts(10);
		options.setReconnectInterval(5000);
		options.setUsername("mqttbroker");
		options.setPassword("mqttbrokerpass");
		MqttClient mqttClient = new MqttClientImpl(vertx, options);

		mqttClient.publishHandler(messageReceivedHandler -> {
			MqttMessageFormat mqttMessageFormat = new MqttMessageFormat(classInstanceId, messageReceivedHandler);
			synchronized (sync) {
				System.out.println("------------------ Begin message received ------------------");
				System.out.println(Json.encodePrettily(mqttMessageFormat));
				System.out.println("------------------ End message received ------------------");
			}

		});

		Random randomTimeSeconds = new Random();

		mqttClient.connect(1885, "localhost", handler -> {
			if (handler.result().code() == MqttConnectReturnCode.CONNECTION_ACCEPTED) {
				mqttClient.subscribe(MqttServerVerticle.TOPIC_RIEGO, MqttQoS.AT_LEAST_ONCE.value(),
						handlerSubscribe -> {
							if (handlerSubscribe.succeeded()) {
								System.out.println(classInstanceId + " subscribed to " + MqttServerVerticle.TOPIC_RIEGO
										+ " channel");

								vertx.setPeriodic((5 + randomTimeSeconds.nextInt(5)) * 1000, handlerPeriodic -> {
									mqttClient.publish(MqttServerVerticle.TOPIC_RIEGO,
											Buffer.buffer(classInstanceId + " says LIGHTS.ON"), MqttQoS.AT_LEAST_ONCE,
											false, true);
								});
							} else {
								System.out.println(
										classInstanceId + " NOT subscribed to " + MqttServerVerticle.TOPIC_RIEGO
												+ " channel " + handlerSubscribe.cause().toString());
							}
						});

				mqttClient.subscribe(MqttServerVerticle.TOPIC_INFO, MqttQoS.AT_LEAST_ONCE.value(), handlerSubscribe -> {
					if (handlerSubscribe.succeeded()) {
						System.out.println(
								classInstanceId + " subscribed to " + MqttServerVerticle.TOPIC_INFO + " channel");

						vertx.setPeriodic(10000, handlerPeriodic -> {
							mqttClient.publish(MqttServerVerticle.TOPIC_INFO,
									Buffer.buffer(classInstanceId + " says here I'm"), MqttQoS.AT_LEAST_ONCE, false,
									true);
						});

					} else {
						System.out.println(classInstanceId + " NOT subscribed to " + MqttServerVerticle.TOPIC_INFO
								+ " channel " + handlerSubscribe.cause().toString());
					}
				});

				mqttClient.subscribe(MqttServerVerticle.TOPIC_SENSOR, MqttQoS.AT_LEAST_ONCE.value(), handlerSubscribe -> {
					if (handlerSubscribe.succeeded()) {
						System.out.println(
								classInstanceId + " subscribed to " + MqttServerVerticle.TOPIC_SENSOR + " channel");
						vertx.setPeriodic((7 + randomTimeSeconds.nextInt(5)) * 1000, handlerPeriodic -> {
							SensorValue sensor_info = new SensorValue();

							mqttClient.publish(MqttServerVerticle.TOPIC_SENSOR,
									Buffer.buffer(Json.encodePrettily(sensor_info)), MqttQoS.AT_LEAST_ONCE, false, true);
						});

					} else {
						System.out.println(classInstanceId + " NOT subscribed to " + MqttServerVerticle.TOPIC_SENSOR
								+ " channel " + handlerSubscribe.cause().toString());
					}
				});
			} else {
				System.out.println("Error: " + handler.result().code().toString());
			}
		});
	}

}
