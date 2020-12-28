package org.yokekhei.examples.activemq.client;

import javax.jms.JMSException;

public class ExceptionHandler implements Runnable {
	private FailoverExceptionListener listener;
	private JMSException exception;
	
	public ExceptionHandler(FailoverExceptionListener listener) {
		this.listener = listener;
	}
	
	public void setException(JMSException e) {
		this.exception = e;
	}

	@Override
	public void run() {
		this.listener.onException(this.exception);
	}

}
