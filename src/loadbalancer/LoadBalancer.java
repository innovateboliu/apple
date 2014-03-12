package loadbalancer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LoadBalancer {
	private ExecutorService executor;
	private boolean isPrimary;
	private InterServerChannel interServerChannel;

	private final ReentrantReadWriteLock lock;
	private final Lock readLock;
	private final Lock writeLock;

	private Scheduler scheduler;
	private Map<Integer, ServerInfo> serverInfoMap;
	private BlockingQueue<Request> requestQueue;

	public LoadBalancer(Scheduler scheduler, List<Integer> serverIds,
			boolean isPrimary) {
		this.executor = Executors.newCachedThreadPool();
		this.requestQueue = new LinkedBlockingDeque<Request>();
		this.lock = new ReentrantReadWriteLock();
		this.readLock = lock.readLock();
		this.writeLock = lock.writeLock();
		this.scheduler = scheduler;
		this.serverInfoMap = new HashMap<Integer, ServerInfo>();

		for (Integer serverId : serverIds) {
			serverInfoMap.put(serverId, new ServerInfo(serverId));
		}

		this.isPrimary = isPrimary;
	}



	public void feedRequest(Request req) {
		try {
			requestQueue.put(req);
			interServerChannel.publish(Action.FEEDREQUEST(req));
		} catch (InterruptedException e) {
			// TODO
		}
	}

	public void handleRequest() {
		readLock.lock();
		try {
			Request req = requestQueue.poll(2000, TimeUnit.MILLISECONDS);
			if (req == null) {
				return;
			}
			int serverId = scheduler.getServerId();

			sendRequestToServer(req, serverId);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		} finally {
			readLock.unlock();
		}
	}

	public void removeServer(int serverId) {
		writeLock.lock();
		try {
			serverInfoMap.remove(serverId);
		} finally {
			writeLock.unlock();
		}
	}

	public void addServer(int serverId) {
		writeLock.lock();
		try {
			serverInfoMap.put(serverId, new ServerInfo(serverId));
		} finally {
			writeLock.unlock();
		}
	}

	private void sendRequestToServer(Request req, int serverId) {

	}
}
