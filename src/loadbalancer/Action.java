package loadbalancer;

public class Action {
	private enum Type {
		ADDSERVER, REMOVESERVER, FEEDREQUEST, HANDLEREQUEST;
	}

	private int serverId;
	private Request request;

	private Action(int serverId) {
		this.serverId = serverId;
	}

	private Action() {
	}

	private Action(Request request) {
		this.request = request;
	}
}
