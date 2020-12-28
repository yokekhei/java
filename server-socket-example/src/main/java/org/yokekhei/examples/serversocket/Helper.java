package org.yokekhei.examples.serversocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Helper implements Runnable {
	
	private final ServerSocket server;
	
	public Helper(ServerSocket server) {
		this.server = server;
	}
	
	public void run() {
		Socket socket = null;
		
		try {
			socket = server.accept();
			handle(socket);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handle(Socket socket) throws IOException {
		//...
	}
}

