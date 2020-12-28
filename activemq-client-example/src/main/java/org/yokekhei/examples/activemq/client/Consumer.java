package org.yokekhei.examples.activemq.client;

import javax.jms.MessageListener;

public interface Consumer extends ClientController {
	void setMessageListener(MessageListener messageListener);
}
