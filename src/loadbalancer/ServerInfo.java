package loadbalancer;

public class ServerInfo {
	private int serverId;
	private int requestLoad;
	
	public ServerInfo(int serverId) {
		this.serverId = serverId;
		this.requestLoad = 0;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getRequestLoad() {
		return requestLoad;
	}
	public void setRequestLoad(int requestLoad) {
		this.requestLoad = requestLoad;
	}
}
