package dadVertx;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import types.Humidity;
import types.Temperature;

public class RestVerticle extends AbstractVerticle{
	
	private Map<Integer, Humidity> humedad = new LinkedHashMap<>();
	private Map<Integer, Temperature> temperatura = new LinkedHashMap<>();
	@SuppressWarnings("deprecation")
	@Override
	public void start(Future<Void> startFuture) {
		createSomeData();
		Router router = Router.router(vertx);
		vertx.createHttpServer().requestHandler(router::accept).listen(8090, result ->{
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
			
			router.route("/api/humedad*").handler(BodyHandler.create());
			router.get("/api/humedad").handler(this::getAllH);
			router.put("/api/humedad").handler(this::addOneH);
			router.post("/api/humedad/:elementid").handler(this::postOneH);
			
			router.route("/api/temperatura*").handler(BodyHandler.create());
			router.get("/api/temperatura").handler(this::getAllT);
			router.put("/api/temperatura").handler(this::addOneT);
			router.post("/api/temperatura/:elementid").handler(this::postOneT);
		});
		
	}
	
	private void createSomeData() {
		Humidity hum1 = new Humidity(80.3f, Calendar.getInstance().getTimeInMillis(), "salon", 8);
		humedad.put(hum1.getId(), hum1);
		Humidity hum2 = new Humidity(75.1f, Calendar.getInstance().getTimeInMillis(), "cocina", 3);
		humedad.put(hum2.getId(), hum2);
		Humidity hum3 = new Humidity(43.9f, Calendar.getInstance().getTimeInMillis(), "dormitorio", 1);
		humedad.put(hum3.getId(), hum3);
		
		Temperature tmp1 = new Temperature(29.3f, Calendar.getInstance().getTimeInMillis(), "salon", 1);
		temperatura.put(tmp1.getId(), tmp1);
		Temperature tmp2 = new Temperature(25.7f, Calendar.getInstance().getTimeInMillis(), "cocina", 2);
		temperatura.put(tmp2.getId(), tmp2);
		Temperature tmp3 = new Temperature(30.1f, Calendar.getInstance().getTimeInMillis(), "dormitorio", 3);
		temperatura.put(tmp3.getId(), tmp3);
	}
	
	private void getAllH(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(humedad.values()));
	}
	
	private void addOneH(RoutingContext routingContext) {
		final Humidity element = Json.decodeValue(routingContext.getBodyAsString(), Humidity.class);
		humedad.put(element.getId(), element);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void postOneH(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("elementid"));
		Humidity ds = humedad.get(id);
		final Humidity element = Json.decodeValue(routingContext.getBodyAsString(), Humidity.class);
		ds.setAccuracy(element.getAccuracy());
		ds.setLocation(element.getLocation());
		ds.setTimestamp(element.getTimestamp());
		ds.setValue(element.getValue());
		humedad.put(ds.getId(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(element));
	}
	
	private void getAllT(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(temperatura.values()));
	}
	
	private void addOneT(RoutingContext routingContext) {
		final Temperature element = Json.decodeValue(routingContext.getBodyAsString(), Temperature.class);
		temperatura.put(element.getId(), element);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void postOneT(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("elementid"));
		Temperature ds = temperatura.get(id);
		final Temperature element = Json.decodeValue(routingContext.getBodyAsString(), Temperature.class);
		ds.setAccuracy(element.getAccuracy());
		ds.setLocation(element.getLocation());
		ds.setTimestamp(element.getTimestamp());
		ds.setValue(element.getValue());
		temperatura.put(ds.getId(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(element));
	}
	
	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
}
