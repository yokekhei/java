package org.yokekhei.examples.activemq.client;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMessageListener implements MessageListener {
	private static final Logger logger = LoggerFactory.getLogger(ExampleMessageListener.class);
	
	@Override
	public void onMessage(Message message) {
		try {
			logger.info(((TextMessage) message).getText());
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
