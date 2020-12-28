package org.yokekhei.examples.activemq.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MessageProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.yokekhei.examples.activemq.AppConfig;

public class AmqpProducer extends AmqpClient implements Producer {
private static final Logger logger = LoggerFactory.getLogger(AmqpProducer.class);
	
	private CountDownLatch failoverLatch;
	private MessageProducer msgProducer;
	private int count = 0;
	
	public AmqpProducer(String name, String queueName, AppConfig appConfig) {
		super(name, queueName, appConfig);
		
		this.failoverLatch = new CountDownLatch(1);
	}
	
	@Override
	public void send(String text) throws JMSException {
		this.msgProducer.send(this.getSession().createTextMessage(text));
	}
	
	@Override
	protected void createInstance() throws Exception {
		super.createInstance();
		this.msgProducer = this.getSession().createProducer(this.getQueue());
	}
	
	@Override
	protected void destroyInstance() {
		try {
			if (this.msgProducer != null) {
				this.msgProducer.close();
				this.msgProducer = null;
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
		try {
			this.send("ABC-" + count);
			count++;
		} catch (JMSException | NullPointerException e) {
			if (this.getExceptionListener() != null) {
				this.failoverLatch.await(
						this.appConfig.getAmqNumRetryConnect() * this.appConfig.getAmqMsRetryTimeout(), 
						TimeUnit.MILLISECONDS);
			}
		}
		
		Thread.sleep(1000);
	}
	
}
