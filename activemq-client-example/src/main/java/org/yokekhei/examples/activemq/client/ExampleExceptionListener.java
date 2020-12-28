package org.yokekhei.examples.activemq.client;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleExceptionListener implements FailoverExceptionListener {
	private static final Logger logger = LoggerFactory.getLogger(ExampleExceptionListener.class);
	
	private final Client client;
	private final int retry;
	private final int retryTimeoutMs;
	private CountDownLatch failoverLatch;
	
	public ExampleExceptionListener(final Client client, final int retry, final int retryTimeoutMs) {
		this.client = client;
		this.retry = retry;
		this.retryTimeoutMs = retryTimeoutMs;
	}
	
	@Override
	public void onException(JMSException exception) {
		for (int i=0; i<this.retry; i++) {
			try {
				this.client.reCreateInstance();
				failoverLatch.countDown();
				return;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				
				try {
					Thread.sleep(this.retryTimeoutMs);
				} catch (InterruptedException e1) {
					// ignored
				}
			}
		}
		
		logger.error("tried {} times to reconnect, giving up", this.retry);
	}

	@Override
	public void setFailoverLatch(CountDownLatch latch) {
		this.failoverLatch = latch;
	}
	
}
