package org.yokekhei.examples.activemq.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.jms.Session;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;

import org.yokekhei.examples.activemq.AppConfig;

public class StompClient extends Client {
	private final String queueName;
	protected final AppConfig appConfig;
	
	private SSLContext sslContext;
	
	public StompClient(final String name, final String queueName, final AppConfig appConfig) {
		super(name);
		
		this.queueName = queueName;
		this.appConfig = appConfig;
	}
	
	@Override
	protected void createInstance() throws Exception {
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setDisconnectTimeout(this.appConfig.getAmqMsDisconnectTimeout());
		
		if (this.appConfig.getAmqIsSSL()) {
			this.setUrl("ssl://" + this.appConfig.getAmqHost() + ":" + this.appConfig.getAmqStompPort());
			initSSL();
			factory.setSslContext(this.sslContext);
		} else {
			this.setUrl("tcp://" + this.appConfig.getAmqHost() + ":" + this.appConfig.getAmqStompPort());
		}
		
		factory.setBrokerURI(this.getUrl());
		
		this.setConnectionFactory(factory);
		this.setConnection(this.getConnectionFactory().createConnection(
				this.appConfig.getAmqUser(), this.appConfig.getAmqPassword()));
		this.setSession(this.getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE));
		this.setQueue(this.getSession().createQueue(this.queueName));
		this.getConnection().setExceptionListener(this.getExceptionListener());
	}
	
	private void initSSL() throws NoSuchAlgorithmException, CertificateException, FileNotFoundException,
		IOException, KeyStoreException, KeyManagementException, UnrecoverableKeyException {
		String pwd = this.appConfig.getAmqTrustStorePassword();
		char [] password = pwd.toCharArray();
		
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore ts = KeyStore.getInstance(this.appConfig.getAmqKeyStoreType());
		ts.load(new FileInputStream(this.appConfig.getAmqTrustStoreFilePath()), password);
		tmf.init(ts);
		
		pwd = this.appConfig.getAmqKeyStorePassword();
		password = pwd.toCharArray();
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		KeyStore ks = KeyStore.getInstance(this.appConfig.getAmqKeyStoreType());
		ks.load(new FileInputStream(this.appConfig.getAmqKeyStoreFilePath()), password);
		kmf.init(ks, password);
		
		this.sslContext = SSLContext.getInstance("TLS");
		this.sslContext.init(kmf.getKeyManagers()/*null*/, tmf.getTrustManagers(), null);
    }
	
}
