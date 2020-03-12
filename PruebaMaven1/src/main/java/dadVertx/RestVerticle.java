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
import types.Dispositivo;
import types.Humedad;
import types.Riego;
import types.Usuario;

public class RestVerticle extends AbstractVerticle{
	
	private Map<Integer, Humedad> humedad = new LinkedHashMap<>();
	private Map<Integer, Dispositivo> dispositivo = new LinkedHashMap<>();
	private Map<Integer, Riego> riego = new LinkedHashMap<>();
	private Map<Integer, Usuario> usuario = new LinkedHashMap<>();
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
			router.delete("/api/humedad").handler(this::deleteOneH);
			
			router.route("/api/dispositivo*").handler(BodyHandler.create());
			router.get("/api/dispositivo").handler(this::getAllD);
			router.put("/api/dispositivo").handler(this::addOneD);
			router.post("/api/dispositivo/:elementid").handler(this::postOneD);
			router.delete("/api/dispositivo").handler(this::deleteOneD);
			
			router.route("/api/riego*").handler(BodyHandler.create());
			router.get("/api/riego").handler(this::getAllR);
			router.put("/api/riego").handler(this::addOneR);
			router.post("/api/riego/:elementid").handler(this::postOneR);
			router.delete("/api/riego").handler(this::deleteOneR);
			
			router.route("/api/usuario*").handler(BodyHandler.create());
			router.get("/api/usuario").handler(this::getAllU);
			router.put("/api/usuario").handler(this::addOneU);
			router.post("/api/usuario/:elementid").handler(this::postOneU);
			router.delete("/api/usuario").handler(this::deleteOneU);
		});
		
	}
	
	
	private void createSomeData() {
		Humedad hum1 = new Humedad(80.3f, Calendar.getInstance().getTimeInMillis());
		humedad.put(hum1.getId(), hum1);
		Humedad hum2 = new Humedad(75.1f, Calendar.getInstance().getTimeInMillis());
		humedad.put(hum2.getId(), hum2);
		Humedad hum3 = new Humedad(43.9f, Calendar.getInstance().getTimeInMillis());
		humedad.put(hum3.getId(), hum3);
	}
	
	private void getAllH(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(humedad.values()));
	}
	
	private void addOneH(RoutingContext routingContext) {
		final Humedad element = Json.decodeValue(routingContext.getBodyAsString(), Humedad.class);
		humedad.put(element.getId(), element);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void postOneH(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("elementid"));
		Humedad ds = humedad.get(id);
		final Humedad element = Json.decodeValue(routingContext.getBodyAsString(), Humedad.class);
		ds.setTimestamp(element.getTimestamp());
		ds.setValue(element.getValue());
		humedad.put(ds.getId(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(element));
	}
	
	private void deleteOneH(RoutingContext routingContext) {
		final Humedad element = Json.decodeValue(routingContext.getBodyAsString(), Humedad.class);
		humedad.remove(element.getId());
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void getAllD(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(humedad.values()));
	}
	
	private void addOneD(RoutingContext routingContext) {
		final Dispositivo element = Json.decodeValue(routingContext.getBodyAsString(), Dispositivo.class);
		dispositivo.put(element.getId(), element);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void postOneD(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("elementid"));
		Dispositivo ds = dispositivo.get(id);
		final Dispositivo element = Json.decodeValue(routingContext.getBodyAsString(), Dispositivo.class);
		ds.setPlanta(element.getPlanta());
		ds.setValue(element.getValue());
		dispositivo.put(ds.getId(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(element));
	}
	
	private void deleteOneD(RoutingContext routingContext) {
		final Dispositivo element = Json.decodeValue(routingContext.getBodyAsString(), Dispositivo.class);
		humedad.remove(element.getId());
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void getAllR(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(dispositivo.values()));
	}
	
	private void addOneR(RoutingContext routingContext) {
		final Riego element = Json.decodeValue(routingContext.getBodyAsString(), Riego.class);
		riego.put(element.getId(), element);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void postOneR(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("elementid"));
		Riego ds = riego.get(id);
		final Humedad element = Json.decodeValue(routingContext.getBodyAsString(), Humedad.class);
		ds.setTimestamp(element.getTimestamp());
		ds.setValue(element.getValue());
		riego.put(ds.getId(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(element));
	}
	
	private void deleteOneR(RoutingContext routingContext) {
		final Riego element = Json.decodeValue(routingContext.getBodyAsString(), Riego.class);
		riego.remove(element.getId());
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void getAllU(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(humedad.values()));
	}
	
	private void addOneU(RoutingContext routingContext) {
		final Usuario element = Json.decodeValue(routingContext.getBodyAsString(), Usuario.class);
		usuario.put(element.getId(), element);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	private void postOneU(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("elementid"));
		Usuario ds = usuario.get(id);
		final Usuario element = Json.decodeValue(routingContext.getBodyAsString(), Usuario.class);
		ds.setNombre(element.getNombre());
		ds.setApellidos(element.getApellidos());
		ds.setDni(element.getDni());
		ds.setTelefono(element.getTelefono());
		usuario.put(ds.getId(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encode(element));
	}
	
	private void deleteOneU(RoutingContext routingContext) {
		final Usuario element = Json.decodeValue(routingContext.getBodyAsString(), Usuario.class);
		usuario.remove(element.getId());
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(element));
	}
	
	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
}
