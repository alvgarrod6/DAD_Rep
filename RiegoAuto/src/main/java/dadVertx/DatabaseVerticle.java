package dadVertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import types.*;

public class DatabaseVerticle extends AbstractVerticle{
	
	private MySQLPool mySQLPool;
	
	@Override
	public void start(Promise<Void> startPromise) {
		MySQLConnectOptions mySQLConnectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("riegoauto").setUser("root").setPassword("root");
		
		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
		mySQLPool = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions);
		
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		
		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if(result.succeeded()) {
				startPromise.complete();
			}else {
				startPromise.fail(result.cause());
			}
		});
		
		router.post("/api/user").handler(this::putUsuario);
		router.get("/api/user/:user/:pass").handler(this::getLogin);
		router.get("/api/user/:idusuario").handler(this::getUsuario);
		router.delete("/api/user/:idusuario").handler(this::deleteUsuario);
		router.put("/api/user/:idusuario").handler(this::updateUsuario);
		
		router.get("/api/device/:idusuario").handler(this::getDispositivo);		
		router.post("/api/device").handler(this::putDevice);
		router.delete("/api/device/:iddispositivo").handler(this::deleteDevice);
		
		router.post("/api/sensor").handler(this::putSensor);
		router.get("/api/sensor/:idsensor").handler(this::getSensor);
		router.put("/api/sensor/:idsensor").handler(this::updateSensor);
		router.delete("/api/sensor/:idsensor").handler(this::deleteSensor);
		
		router.get("/api/sensor/values/:idSensor").handler(this::getValueBySensor);
		router.post("/api/sensor/values").handler(this::putValueForSensor);
		
		router.get("/api/sensor/riego/:idsensor").handler(this::getRiego);
		router.post("/api/sensor/riego").handler(this::putRiego);		
		
	} 
	
	private void getRiego(RoutingContext routingContext) {
		mySQLPool.preparedQuery("SELECT * FROM riegoauto.riego WHERE idsensor = ?", 
				Tuple.of(routingContext.request().getParam("idsensor")), 
				res -> {
					if(res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El número de elementos obtenidos es: "+resultSet.size());
						JsonArray result = new JsonArray();
						for(Row row : resultSet) {
							result.add(JsonObject.mapFrom(new Riego(row.getInteger("idriego"),
									row.getLong("timestamp"),
									row.getInteger("humedadRiego"),
									row.getBoolean("autoManual"),
									row.getInteger("idsensor"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	private void putRiego(RoutingContext routingContext) {
		Riego riego = Json.decodeValue(routingContext.getBodyAsString(), Riego.class);
		mySQLPool.preparedQuery("INSERT INTO riegoauto.riego(idriego"
				+ ", timestamp, humedadRiego, autoManual, idsensor) "
				+ "VALUES (?,?,?,?,?)", Tuple.of(riego.getId(), 
						 riego.getTimestamp(), riego.getHumedad(), riego.isManualAuto(), riego.getIdsensor())
				, handler ->{
							if(handler.succeeded()) {
								System.out.println(handler.result().rowCount());							
								
								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(riego).encodePrettily());
							}else {
								System.out.println(handler.cause().toString());
								routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
							}
				});
	}
	
	private void deleteSensor(RoutingContext routingContext) {
		mySQLPool.preparedQuery("DELETE FROM riegoauto.sensor WHERE idsensor = ?", 
				Tuple.of(routingContext.request().getParam("idsensor")), 
				handler -> {
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());							
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end();
					}else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void updateSensor(RoutingContext routingContext) {
		Sensor sensor = Json.decodeValue(routingContext.getBodyAsString(), Sensor.class);
		mySQLPool.preparedQuery("UPDATE riegoauto.sensor SET planta = ?, umbral = ?, potencia = ?"
				+ " WHERE idsensor = ?", 
				Tuple.of(sensor.getPlanta(),sensor.getUmbral(), sensor.getPotencia(),
						 routingContext.request().getParam("idsensor")), 
				handler -> {
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());							
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(sensor).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void getSensor(RoutingContext routingContext) {
		mySQLPool.preparedQuery("SELECT * FROM riegoauto.sensor WHERE idsensor = ?", 
				Tuple.of(routingContext.request().getParam("idsensor")), 
				res -> {
					if(res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El número de elementos obtenidos es: "+resultSet.size());
						JsonArray result = new JsonArray();
						for(Row row : resultSet) {
							result.add(JsonObject.mapFrom(new Sensor(row.getInteger("idsensor"),
									row.getInteger("iddisp"),
									row.getString("planta"),
									row.getInteger("umbral"),
									row.getInteger("potencia"),
									row.getLong("initialTimestamp"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	private void putSensor(RoutingContext routingContext) {
		Sensor sensor = Json.decodeValue(routingContext.getBodyAsString(), Sensor.class);
		mySQLPool.preparedQuery("INSERT INTO riegoauto.sensor(idsensor"
				+ ", iddisp, planta, umbral, potencia, initialTimestamp) "
				+ "VALUES (?,?,?,?,?,?)", Tuple.of(sensor.getId(), 
						 sensor.getIddispositivo(), sensor.getPlanta(), sensor.getUmbral(), sensor.getPotencia(), sensor.getInitialTimestamp())
				, handler ->{
							if(handler.succeeded()) {
								System.out.println(handler.result().rowCount());							
								
								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(sensor).encodePrettily());
							}else {
								System.out.println(handler.cause().toString());
								routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
							}
				});
	}
	
	private void deleteDevice(RoutingContext routingContext) {
		mySQLPool.preparedQuery("DELETE FROM riegoauto.dispositivo WHERE iddispositivo = ?", 
				Tuple.of(routingContext.request().getParam("iddispositivo")), 
				handler -> {
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());							
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end();
					}else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void deleteUsuario(RoutingContext routingContext) {
		mySQLPool.preparedQuery("DELETE FROM riegoauto.usuario WHERE idusuario = ?", 
				Tuple.of(routingContext.request().getParam("idusuario")), 
				handler -> {
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());							
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end();
					}else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void updateUsuario(RoutingContext routingContext) {
		Usuario user = Json.decodeValue(routingContext.getBodyAsString(), Usuario.class);
		mySQLPool.preparedQuery("UPDATE riegoauto.usuario SET user = ?, pass = ?, name = ?, surname = ?, dni = ?"
				+ ", birthdate = ? WHERE idusuario = ?", 
				Tuple.of(user.getUser(),user.getPass(), user.getNombre(), user.getApellidos(), user.getDni(),
						user.getNacimiento(), routingContext.request().getParam("idusuario")), 
				handler -> {
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());							
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(user).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void putDevice(RoutingContext routingContext) {
		Dispositivo device = Json.decodeValue(routingContext.getBodyAsString(), Dispositivo.class);
		mySQLPool.preparedQuery("INSERT INTO riegoauto.dispositivo(iddispositivo"
				+ ", ip, idusuario, initialTimestamp) "
				+ "VALUES (?,?,?,?)", Tuple.of(device.getIdDispositivo(), 
						 device.getIp(), device.getIdUsuario(), device.getInitialTimestamp())
				, handler ->{
							if(handler.succeeded()) {
								System.out.println(handler.result().rowCount());							
								
								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(device).encodePrettily());
							}else {
								System.out.println(handler.cause().toString());
								routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
							}
				});
	}
	
	private void putUsuario(RoutingContext routingContext) {
		Usuario user = Json.decodeValue(routingContext.getBodyAsString(), Usuario.class);
		mySQLPool.preparedQuery("INSERT INTO riegoauto.usuario(user, pass, name, surname, dni, birthdate) "
				+ "VALUES (?,?,?,?,?,?)", Tuple.of(user.getUser(), user.getPass(), user.getNombre(), user.getApellidos(), 
						user.getDni(), user.getNacimiento()), handler ->{
							if(handler.succeeded()) {
								System.out.println(handler.result().rowCount());
								
								long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
								
								user.setId((int)id);
								
								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(user).encodePrettily());
							}else {
								System.out.println(handler.cause().toString());
								routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
							}
				});
	}
	
	private void getLogin(RoutingContext routingContext) {
		mySQLPool.preparedQuery("SELECT * FROM riegoauto.usuario WHERE user = ? AND pass = ?", 
				Tuple.of(routingContext.request().getParam("user"),	routingContext.request().getParam("pass")), 
				res -> {
					if(res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El número de elementos obtenidos es: "+resultSet.size());
						JsonArray result = new JsonArray();
						for(Row row : resultSet) {
							result.add(JsonObject.mapFrom(new Usuario(row.getInteger("idusuario"),
									row.getString("user"),
									row.getString("pass"),
									row.getString("name"),
									row.getString("surname"),
									row.getString("dni"),
									row.getLong("birthdate"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}

	private void getUsuario(RoutingContext routingContext) {
		mySQLPool.preparedQuery("SELECT * FROM riegoauto.usuario WHERE idusuario = ?", 
				Tuple.of(routingContext.request().getParam("user")), 
				res -> {
					if(res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El número de elementos obtenidos es: "+resultSet.size());
						JsonArray result = new JsonArray();
						for(Row row : resultSet) {
							result.add(JsonObject.mapFrom(new Usuario(row.getInteger("idusuario"),
									row.getString("user"),
									row.getString("pass"),
									row.getString("name"),
									row.getString("surname"),
									row.getString("dni"),
									row.getLong("birthdate"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	private void getDispositivo(RoutingContext routingContext) {
		mySQLPool.preparedQuery("SELECT * FROM riegoauto.dispositivo WHERE idusuario = ?", 
				Tuple.of(routingContext.request().getParam("idusuario")), 
				res -> {
					if(res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El número de elementos obtenidos es: "+resultSet.size());
						JsonArray result = new JsonArray();
						for(Row row : resultSet) {
							result.add(JsonObject.mapFrom(new Dispositivo(row.getInteger("iddispositivo"),
									row.getString("ip"),
									row.getInteger("idUsuario"),
									row.getLong("initialTimestamp"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	private void getValueBySensor(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM riegoauto.sensor_value WHERE idsensor ="
	+routingContext.request().getParam("idsensor"), res ->{
		if(res.succeeded()) {
			RowSet<Row> resultSet = res.result();
			System.out.println("El número de elementos obtenidos es: "+resultSet.size());
			JsonArray result = new JsonArray();
			for(Row row : resultSet) {
				result.add(JsonObject.mapFrom(new SensorValue(row.getInteger("idsensor_value"),
						row.getInteger("idsensor"),
						row.getFloat("value"),
						row.getFloat("accuracy"),
						row.getLong("timestamp"))));
			}
			
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
			.end(result.encodePrettily());
			}else {
			routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
		}
	});
	}
	
	private void putValueForSensor(RoutingContext routingContext) {
		SensorValue sensorValue = Json.decodeValue(routingContext.getBodyAsString(), SensorValue.class);
		mySQLPool.preparedQuery("INSERT INTO riegoauto.sensor_value(idsensor, value, accuracy, timestamp) "
				+ "VALUES (?,?,?,?)", Tuple.of(sensorValue.getIdsensor(), sensorValue.getValue(), sensorValue.getAccuracy(), sensorValue.getTimestamp())
				, handler -> {
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						
						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						
						sensorValue.setIdsensor_value((int)id);
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(sensorValue).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
}
