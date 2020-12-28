package org.yokekhei.examples.activemq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="org.yokekhei.examples.activemq")
public class AppConfig {
	
	@Value("${amq.user}")
	private String amqUser;
	
	@Value("${amq.password}")
	private String amqPassword;
	
	@Value("${amq.isSSL}")
	private Boolean amqIsSSL;
	
	@Value("${amq.num.retry.connect}")
	private Integer amqNumRetryConnect;
	
	@Value("${amq.ms.retry.timeout}")
	private Integer amqMsRetryTimeout;
	
	@Value("${amq.ms.disconnect.timeout}")
	private Integer amqMsDisconnectTimeout;
	
	@Value("${amq.host}")
	private String amqHost;
	
	@Value("${amq.protocol}")
	private Integer amqProtocol;
	
	@Value("${amq.openwire.port}")
	private Integer amqOpenWirePort;
	
	@Value("${amq.amqp.port}")
	private Integer amqAmqpPort;
	
	@Value("${amq.stomp.port}")
	private Integer amqStompPort;
	
	@Value("${amq.truststore.file}")
	private String amqTrustStoreFilePath;
	
	@Value("${amq.truststore.password}")
	private String amqTrustStorePassword;
	
	@Value("${amq.keystore.file}")
	private String amqKeyStoreFilePath;
	
	@Value("${amq.keystore.password}")
	private String amqKeyStorePassword;
	
	@Value("${amq.keystore.type}")
	private String amqKeyStoreType;
	
	@Value("${amq.queue.name}")
	private String amqQueueName;
	
	public String getAmqUser() {	
		return amqUser;
	}
	
	public String getAmqPassword() {
		return amqPassword;
	}

	public Boolean getAmqIsSSL() {
		return amqIsSSL;
	}

	public Integer getAmqNumRetryConnect() {
		return amqNumRetryConnect;
	}

	public Integer getAmqMsRetryTimeout() {
		return amqMsRetryTimeout;
	}
	
	public Integer getAmqMsDisconnectTimeout() {
		return amqMsDisconnectTimeout;
	}

	public String getAmqHost() {
		return amqHost;
	}

	public Integer getAmqProtocol() {
		return amqProtocol;
	}

	public Integer getAmqOpenWirePort() {
		return amqOpenWirePort;
	}

	public Integer getAmqAmqpPort() {
		return amqAmqpPort;
	}

	public Integer getAmqStompPort() {
		return amqStompPort;
	}

	public String getAmqTrustStoreFilePath() {
		return amqTrustStoreFilePath;
	}

	public String getAmqTrustStorePassword() {
		return amqTrustStorePassword;
	}

	public String getAmqKeyStoreFilePath() {
		return amqKeyStoreFilePath;
	}

	public String getAmqKeyStorePassword() {
		return amqKeyStorePassword;
	}

	public String getAmqKeyStoreType() {
		if (amqKeyStoreType == null) return "JKS";
		
		return amqKeyStoreType;
	}

	public String getAmqQueueName() {
		return amqQueueName;
	}
	
}
