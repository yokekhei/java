package org.yokekhei.examples.serversocket;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ServerSocketExample {
	public static void main(String[] args) {
		final CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("ServerSocketExample-shutdown-hook") {
            @Override
            public void run() {
                latch.countDown();
            }
        });
        
		try {
			for (int i=0; i<1000; i++ ) {
				RequestHandler.getInstance().handleRequest();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Exit application");
	}
}