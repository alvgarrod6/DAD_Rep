package dadVertx;

import io.vertx.core.Promise;
import io.vertx.core.AbstractVerticle;

public class DespliegueRest extends AbstractVerticle{
	
	@Override
	public void start(Promise<Void> startPromise) {	
		
		vertx.deployVerticle(DatabaseVerticle.class.getName());	
		
	}
	
}
