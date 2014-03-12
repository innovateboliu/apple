package loadbalancer;

import java.util.Set;


public abstract class InterServerChannel {
	private Set<LoadBalancer> subscribers;
	
	public void subscribe(LoadBalancer loadBalancer) {
		subscribers.add(loadBalancer);
	}
	
	public abstract void publish(Action action);
	
	private void broadcast(Action action) {
		for (LoadBalancer loadBalancer : subscribers) {
			loadBalancer.sync(action);
		}
	}
}
