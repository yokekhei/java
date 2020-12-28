package org.yokekhei.examples.activemq.client;

import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;

import org.yokekhei.examples.activemq.AppConfig;

public class OpenWireClient extends Client {
	private final String queueName;
	protected final AppConfig appConfig;

	public OpenWireClient(final String name, final String queueName, final AppConfig appConfig) {
		super(name);
		
		this.queueName = queueName;
		this.appConfig = appConfig;
	}

	@Override
	protected void createInstance() throws Exception {
		if (this.appConfig.getAmqIsSSL()) {
			this.setUrl("ssl://" + this.appConfig.getAmqHost() + ":" + this.appConfig.getAmqOpenWirePort());
			ActiveMQSslConnectionFactory cf = new ActiveMQSslConnectionFactory(this.getUrl());
			cf.setTrustStore(this.appConfig.getAmqTrustStoreFilePath());
			cf.setTrustStorePassword(this.appConfig.getAmqTrustStorePassword());
			this.setConnectionFactory(cf);
		} else {
			this.setUrl("tcp://" + this.appConfig.getAmqHost() + ":" + this.appConfig.getAmqOpenWirePort());
			this.setConnectionFactory(new ActiveMQConnectionFactory(this.getUrl()));
		}
  	  
       this.setConnection(this.getConnectionFactory().createConnection(
    		   this.appConfig.getAmqUser(), this.appConfig.getAmqPassword()));
       this.setSession(this.getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE));
       this.setQueue(this.getSession().createQueue(this.queueName));
       this.getConnection().setExceptionListener(this.getExceptionListener());
	}

}
