package dadVertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MyThirdVerticle extends AbstractVerticle{
	
	@Override
	public void start(Future<Void> startFuture) {
		vertx.eventBus().consumer("mensaje_p2p", message -> {
			String stringMessage = (String)message.body();
			System.out.println("Mensaje recibido por 2: "+ stringMessage);
			message.reply("Sí, he recibido el mensaje");
		});
		
		vertx.eventBus().consumer("mensaje_broadcast", message -> {
			String stringMessage = (String)message.body();
			System.out.println("Mensaje broadcast 2: "+ stringMessage);
		});
	}

}
