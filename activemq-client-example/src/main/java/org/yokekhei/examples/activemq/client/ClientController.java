package org.yokekhei.examples.activemq.client;

public interface ClientController {
	void start();
	
	void terminate();
	
	void setExceptionListener(FailoverExceptionListener exceptionListener);
}
