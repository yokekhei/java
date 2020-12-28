package org.yokekhei.examples.activemq;

import java.util.concurrent.CountDownLatch;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.yokekhei.examples.activemq.client.ClientFactory;
import org.yokekhei.examples.activemq.client.Consumer;
import org.yokekhei.examples.activemq.client.Producer;

@Service
public class ActiveMQClientExampleService implements DisposableBean {
	@Autowired
	private ClientFactory clientFactory;
	
	private Producer producer;
	private Consumer consumer;
	
	public ActiveMQClientExampleService() {
	}
	
	public void start() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		
		this.consumer = this.clientFactory.getConsumer();
		if (this.consumer != null) this.consumer.start();
		
		// Wait time to start consumer listener before producer sending data
		Thread.sleep(3000);
		
		this.producer = this.clientFactory.getProducer();
		if (this.producer != null) this.producer.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread("ActiveMQClientExampleService-shutdown-hook") {
			@Override
            public void run() {
            	latch.countDown();
            }
        });
		
		latch.await();
	}
	
	@Override
	public void destroy() throws Exception {
		this.producer.terminate();
		this.consumer.terminate();
	}

}
