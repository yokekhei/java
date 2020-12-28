package org.yokekhei.examples.activemq.client;

import javax.jms.Session;

import org.apache.qpid.jms.JmsConnectionFactory;

import org.yokekhei.examples.activemq.AppConfig;

public class AmqpClient extends Client {
	private final String queueName;
	protected final AppConfig appConfig;
	
	public AmqpClient(final String name, final String queueName, final AppConfig appConfig) {
		super(name);
		
		this.queueName = queueName;
		this.appConfig = appConfig;
	}
	
	@Override
	protected void createInstance() throws Exception {
		if (this.appConfig.getAmqIsSSL()) {
			this.setUrl("amqps://" + this.appConfig.getAmqHost() + ":" + this.appConfig.getAmqAmqpPort() +
					"?transport.trustStoreLocation=" + this.appConfig.getAmqTrustStoreFilePath() +
					"&transport.trustStorePassword=" + this.appConfig.getAmqTrustStorePassword());
		} else {
			this.setUrl("amqp://" + this.appConfig.getAmqHost() + ":" + this.appConfig.getAmqAmqpPort());
		}
		
		this.setConnectionFactory(new JmsConnectionFactory(this.getUrl()));
		this.setConnection(this.getConnectionFactory().createConnection(
				this.appConfig.getAmqUser(), this.appConfig.getAmqPassword()));
		this.setSession(this.getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE));
		this.setQueue(this.getSession().createQueue(this.queueName));
		this.getConnection().setExceptionListener(this.getExceptionListener());
	}

}
