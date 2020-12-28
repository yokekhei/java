package org.yokekhei.examples.activemq.client;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.yokekhei.examples.activemq.AppConfig;

public class AmqpConsumer extends AmqpClient implements Consumer {
private static final Logger logger = LoggerFactory.getLogger(AmqpConsumer.class);
	
	private CountDownLatch failoverLatch;
	private MessageConsumer msgConsumer;
	private MessageListener messageListener;
	
	public AmqpConsumer(String name, String queueName, AppConfig appConfig) {
		super(name, queueName, appConfig);
		
		this.failoverLatch = new CountDownLatch(1);
	}
	
	@Override
	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	@Override
	protected void createInstance() throws Exception {
		super.createInstance();
		this.msgConsumer = this.getSession().createConsumer(this.getQueue());
		this.msgConsumer.setMessageListener(this.messageListener);
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
}
