package dadVertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class WorkerVerticle extends AbstractVerticle{
	@SuppressWarnings("deprecation")
	@Override
	public void start (Future<Void> startFuture) {
		try {
			System.out.println("Antes de dormir");
			Thread.sleep(10000);
			System.out.println("Despues de dormir");
			startFuture.complete();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			startFuture.fail(e);
		}
	}
}
