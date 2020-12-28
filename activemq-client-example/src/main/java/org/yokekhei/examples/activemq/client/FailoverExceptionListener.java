package org.yokekhei.examples.activemq.client;

import java.util.concurrent.CountDownLatch;

import javax.jms.ExceptionListener;

public interface FailoverExceptionListener extends ExceptionListener {
	void setFailoverLatch(CountDownLatch latch);
}
