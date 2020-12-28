package org.yokekhei.examples.activemq.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.yokekhei.examples.activemq.AppConfig;

public class StompConsumer extends StompClient implements Consumer {
private static final Logger logger = LoggerFactory.getLogger(StompConsumer.class);
	
	private CountDownLatch failoverLatch;
	private MessageConsumer msgConsumer;
	private ExampleStompMessageListener messageListener;
	private ExceptionHandler exceptionHandler;
	private long rcvCounter;
	private int retry;
	
	public StompConsumer(String name, String queueName, AppConfig appConfig) {
		super(name, queueName, appConfig);
		
		this.failoverLatch = new CountDownLatch(1);
		this.rcvCounter = -1;
		this.retry = 0;
	}
	
	@Override
	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = (ExampleStompMessageListener) messageListener;
	}

	@Override
	protected void createInstance() throws Exception {
		super.createInstance();
		this.msgConsumer = this.getSession().createConsumer(this.getQueue());
		this.msgConsumer.setMessageListener(this.messageListener);
		
		if (this.getExceptionListener() != null) {
			this.getExceptionListener().setFailoverLatch(this.failoverLatch);
			this.exceptionHandler = new ExceptionHandler(this.getExceptionListener());
		}
	}
	
	@Override
	protected void destroyInstance() {
		try {
			if (this.msgConsumer != null) {
				this.msgConsumer.close();
				this.msgConsumer = null;
			}
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
		
		super.destroyInstance();
	}
	
	@Override
	protected void beginWork() throws Exception {
		super.beginWork();
		if (this.getExceptionListener() != null) {
			this.getExceptionListener().setFailoverLatch(this.failoverLatch);
		}
	}
	
	@Override
	protected void doWork() throws Exception {
		if (this.rcvCounter != this.messageListener.getRcvCounter()) {
			this.rcvCounter = this.messageListener.getRcvCounter();
			this.retry = 0;
		} else if (this.messageListener.getRcvCounter() == this.rcvCounter) {
			this.retry++;
			
			if (this.retry == this.appConfig.getAmqNumRetryConnect()) {
				// TOFIX::StompJmsConnectionFactory which implemented Stomp 1.1 does not throw out
				// exception to application when disconnect happened.
				// Workaround: reconnect session if no message received within timeout interval.
				if (this.getExceptionListener() != null) {
					this.exceptionHandler.setException((JMSException) null);
					new Thread(this.exceptionHandler).start();
					
					this.failoverLatch.await(
							this.appConfig.getAmqNumRetryConnect() * this.appConfig.getAmqMsRetryTimeout(),
							TimeUnit.MILLISECONDS);
				}
			}
		}
		
		Thread.sleep(this.appConfig.getAmqMsRetryTimeout());
	}
	
}
