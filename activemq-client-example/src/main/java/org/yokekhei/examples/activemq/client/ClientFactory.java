package org.yokekhei.examples.activemq.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.yokekhei.examples.activemq.AppConfig;
import org.yokekhei.examples.activemq.Constants;

@Component
public class ClientFactory {
	@Autowired
	private AppConfig appConfig;
	
	public Producer getProducer() {
		Producer producer = null;
		
		if (this.appConfig.getAmqProtocol() == Constants.Protocol.OPENWIRE.toIntValue()) {
			producer = new OpenWireProducer(
					"OpenWire Producer",
					this.appConfig.getAmqQueueName(),
					this.appConfig);
		} else if (this.appConfig.getAmqProtocol() == Constants.Protocol.AMQP.toIntValue()) {
			producer = new AmqpProducer(
					"AMQP Producer",
					this.appConfig.getAmqQueueName(),
					this.appConfig);
		} else if (this.appConfig.getAmqProtocol() == Constants.Protocol.STOMP.toIntValue()) {
			producer = new StompProducer(
					"STOMP Producer",
					this.appConfig.getAmqQueueName(),
					this.appConfig);
		}
		
		if (producer != null) {
			producer.setExceptionListener(new ExampleExceptionListener((Client)producer,
					this.appConfig.getAmqNumRetryConnect(),
					this.appConfig.getAmqMsRetryTimeout()));
		}
		
		return producer;
	}
	
	public Consumer getConsumer() {
		Consumer consumer = null;
		
		if (this.appConfig.getAmqProtocol() == Constants.Protocol.OPENWIRE.toIntValue()) {
			consumer = new OpenWireConsumer(
					"OpenWire Consumer",
					this.appConfig.getAmqQueueName(),
					this.appConfig);
		} else if (this.appConfig.getAmqProtocol() == Constants.Protocol.AMQP.toIntValue()) {
			consumer = new AmqpConsumer(
					"AMQP Consumer",
					this.appConfig.getAmqQueueName(),
					this.appConfig);
		} else if (this.appConfig.getAmqProtocol() == Constants.Protocol.STOMP.toIntValue()) {
			consumer = new StompConsumer(
					"STOMP Consumer",
					this.appConfig.getAmqQueueName(),
					this.appConfig);
		}
		
		if (consumer != null) {
			consumer.setExceptionListener(new ExampleExceptionListener((Client)consumer,
					this.appConfig.getAmqNumRetryConnect(), this.appConfig.getAmqMsRetryTimeout()));
			
			if (this.appConfig.getAmqProtocol() == Constants.Protocol.STOMP.toIntValue()) {
				consumer.setMessageListener(new ExampleStompMessageListener());
			} else {
				consumer.setMessageListener(new ExampleMessageListener());
			}
		}
		
		return consumer;
	}
	
}
