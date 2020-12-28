package org.yokekhei.examples.activemq.client;

import javax.jms.JMSException;

public interface Producer extends ClientController {
	void send(String text) throws JMSException;
}
