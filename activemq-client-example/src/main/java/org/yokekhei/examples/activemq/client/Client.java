package org.yokekhei.examples.activemq.client;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Client extends Thread {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final AtomicBoolean isTerminated;
	
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Queue queue;
	private Session session;
	private FailoverExceptionListener exceptionListener;
	private String url;
	
	public Client(final String name) {
		this.isTerminated = new AtomicBoolean(false);
		this.setName(name);
	}

    @Override
    public void run() {
    	try {
    		this.createInstance();
    		
    		this.beginWork();
    		
    		while (!this.isTerminated()) {
    			this.doWork();
    		}
    	} catch (InterruptedException e) {
    		logger.error(e.getMessage(), e);
    		
    		if (!this.isTerminated.get()) {
    			logger.error("Client thread {} interrupted - {}", this.getName(), e.getMessage());
    		}
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		
    		if (!this.isTerminated.get()) {
    			logger.error("Client thread {} caught exception - {}", this.getName(), e.getMessage());
    		}
    	} finally {
    		this.endWork();
    		this.destroyInstance();
    	}
    }
    
    public void terminate() {
		this.isTerminated.set(true);
		logger.debug("Terminating client [{}]", this.getName());
	}
    
    public boolean isTerminated() {
    	return this.isTerminated.get();
    }
    
    public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}

	public FailoverExceptionListener getExceptionListener() {
		return exceptionListener;
	}
	
	public void setExceptionListener(FailoverExceptionListener exceptionListener) {
		this.exceptionListener = exceptionListener;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	} 
	
	protected void destroyInstance() {
		if (this.session != null) {
			this.session = null;
		}
		
		if (this.connection != null) {
			this.connection = null;
		}
	}
	
	protected void beginWork() throws Exception {
		if (this.connection != null) {
			this.getConnection().start();
		}
	}
	
	protected void endWork() {
		try {
			if (this.session != null) {
				this.session.close();
			}
			
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	protected void reCreateInstance() throws Exception {
		this.endWork();
		this.destroyInstance();
		this.createInstance();
		this.beginWork();
	}
	
	protected void doWork() throws Exception {
		Thread.sleep(1000);
	}
	
	protected abstract void createInstance() throws Exception;
}
