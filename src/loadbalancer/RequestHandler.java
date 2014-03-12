package loadbalancer;

public class RequestHandler implements Runnable{
	private LoadBalancer loadBalancer;
	
	public RequestHandler(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
	
	@Override
	public void run() {
		loadBalancer.handleRequest();
	}

}
