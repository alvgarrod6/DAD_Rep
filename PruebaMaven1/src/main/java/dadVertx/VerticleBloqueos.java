package dadVertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class VerticleBloqueos extends AbstractVerticle{
	
	public void start(Future<Void> startFuture) {
		/*vertx.executeBlocking(future ->{
			try {
				System.out.println("Antes de dormir");
				Thread.sleep(10000);
				System.out.println("Despues de dormir");
				future.complete("Ejecuci�n terminada");
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				future.complete("Ejecuci�n terminada con excepci�n");
			}
		}, res ->{			
			System.out.println("El resultado ha sido "+res.result().toString());
		});*/
		
		DeploymentOptions deploymentOptions = new DeploymentOptions().setWorker(true);
		vertx.deployVerticle(WorkerVerticle.class.getName(),deploymentOptions, res -> {
			System.out.println("El resultado del Worker ha sido: "+res.result());
		});
		
		
	}
	
}
