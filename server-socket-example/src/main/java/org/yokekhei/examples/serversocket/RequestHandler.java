package org.yokekhei.examples.serversocket;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class RequestHandler implements Closeable {
	
	private static RequestHandler singleInstance = null;
	
	// limit to 50 threads running concurrently
    private final ExecutorService threadPool = Executors.newFixedThreadPool(50);
    private final ServerSocket server;
    private final Object closeLock = new Object();
    
    private volatile boolean closed = false;
	
	private RequestHandler(int port) throws IOException {
		server = new ServerSocket(port);
	}
	
	public static RequestHandler getInstance() throws IOException {
		if (singleInstance == null) {
			singleInstance = new RequestHandler(0);
			// Select next available port
		}
		
		return singleInstance;
	}
	
	public void handleRequest() {
		threadPool.execute(new Helper(server));
	}

	public void close() throws IOException {
		synchronized (closeLock) {
			if (closed) {
				return;
			}
			
			threadPool.shutdown();
			
			if (server != null) {
				server.close();
			}
			
			closed = true;
		}
	}
}

