package org.yokekhei.examples.activemq.client;

import javax.jms.Message;

public class ExampleStompMessageListener extends ExampleMessageListener {
	private long rcvCounter;
	
	public ExampleStompMessageListener() {
		this.rcvCounter = 0;
	}
	
	@Override
	public void onMessage(Message message) {
		super.onMessage(message);
		this.rcvCounter++;
		
		// Reset counter
		if (this.rcvCounter == 1000) {
			this.rcvCounter = 0;
		}
	}
	
	public long getRcvCounter() {
		return this.rcvCounter;
	}
	
}
