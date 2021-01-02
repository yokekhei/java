package org.yokekhei.examples.jedis.sentinel.client;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import redis.clients.jedis.Jedis;

public class SubscribeThread implements Runnable {
	private static final Integer JEDIS_MAX_TOTAL = 8;
	private static final Integer JEDIS_MAX_IDLE = 4;
	private static final Integer JEDIS_MIN_IDLE = 1;
	
	private Thread thread;
	private JedisPubSubListener listener;
	private String[] channels;
	private String threadName;
	private boolean isError;
	private AtomicBoolean running = new AtomicBoolean(false);
	private AtomicBoolean terminated = new AtomicBoolean(false);
	
	private JedisWrapper jedisWrapper;
	private Jedis jedis;
	private String host = "";
	private JedisConfig jedisConfig;
	
	public SubscribeThread(String sentinelHost, JedisConfig jedisConfig, JedisPubSubListener listener, String[] channels) {
		this.host = sentinelHost;
		this.jedisConfig = jedisConfig;
		this.listener = listener;
		this.channels = channels;
		
		this.threadName = "";
		this.isError = false;
	}
	
	public void run() {
		running.set(true);
		
		while (!terminated.get()) {
			if (running.get()) {
				System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + " is running");
				
				try {
					System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + ": getting resource from pool ----");
					jedis = jedisWrapper.getJedis();
					
					System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + ": subscribing channel list ----");
					jedis.subscribe(listener, channels);
					System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + ": quit from jedis subscribe ----");
				} catch (Exception e) {
					System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + " error: subscribe channel list [" + e.getMessage() + "] ----");
					e.printStackTrace();
					isError = true;
					running.set(false);
				} catch (OutOfMemoryError e) {
					System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + " error: subscribe channel list [" + e.getMessage() + "] ----");
					e.printStackTrace();
					isError = true;
					running.set(false);
				} finally {
					System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + " closing jedis [isError=" + isError + "]");
					
					if (jedis != null) {
						try {
							disconnect();
							jedis.close();
							jedis = null;
							System.out.println(new Date() + " ----### REDIS[" + host + "] " +
									thread.getName() + " - jedis closed");
						} catch (Exception e) {
							e.printStackTrace();
							isError = true;
							running.set(false);
						}
					}
				}
			} else {
				System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + " is suspended");
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + " terminated");
		thread = null;
	}
	
	public void stop() {
		System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + " stop()");
		
		running.set(false);
		disconnect();
	}
	
	public void terminate() {
		running.set(false);
		terminated.set(true);
		disconnect();
		closeJedisWrapper();
	}
	
	public void start() {
		isError = false;
		running.set(true);
		terminated.set(false);
		
		if (jedisWrapper == null) {
			initJedisWrapper();
		}
		
		if (thread == null) {
			thread = new Thread(this);
			thread.setName("SubscribeThread-" + thread.getId());
			threadName = thread.getName();
			listener.setSubscriberName(threadName);
			thread.start();
		}
		
		System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + " started");
		monitor();
	}
	
	public boolean isError() {
		return isError;
	}
	
	public boolean isRunning() {
		return running.get();
	}
	
	public void monitor() {
		if (jedisWrapper != null) {
			System.out.println(new Date() + " --- ----### REDIS[" + host + "] " + threadName +
					" total active::" + jedisWrapper.getJedisPool().getInternalPool().getNumActive());
			System.out.println(new Date() + " --- ----### REDIS[" + host + "] " + threadName +
					" total waiter::" + jedisWrapper.getJedisPool().getInternalPool().getNumWaiters());
		}
	}
	
	public void unsubscribe() {
		try {
			if (thread != null && listener.isSubscribed()) {
				System.out.println(new Date() + " ----### REDIS[" + host + "] " + threadName + " unsubscribe()");
				listener.unsubscribe(channels);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void disconnect() {
		if (jedis != null && jedis.isConnected()) {
			try {
				jedis.getClient().setSoTimeout(1);
				
				String remoteHost = jedis.getClient().getSocket().getInetAddress().toString();
				int localPort = jedis.getClient().getSocket().getLocalPort();
				
				System.out.println(new Date() + " ----### REDIS[" + host + "] " +
						threadName + " jedis disconnecting from " + remoteHost + ":" + localPort);
				
				jedis.disconnect();
				
				if (!jedis.isConnected()) {
					System.out.println(new Date() + " ----### REDIS[" + host + "] " +
							threadName + " jedis disconnected from " + remoteHost + ":" + localPort);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		monitor();
	}
	
	private void initJedisWrapper() {
		closeJedisWrapper();
		
		JedisConfig jedisPoolConfig = new JedisConfig();
		jedisPoolConfig.setMaxTotal(JEDIS_MAX_TOTAL);
		jedisPoolConfig.setMaxIdle(JEDIS_MAX_IDLE);
		jedisPoolConfig.setMinIdle(JEDIS_MIN_IDLE);
		jedisPoolConfig.setMaxWaitMillis(jedisConfig.getMaxWaitMillis());
		jedisPoolConfig.setTimeBtwEvictionMs(jedisConfig.getTimeBtwEvictionMs());
		jedisPoolConfig.setSocketTimeout(jedisConfig.getSocketTimeout());
		jedisPoolConfig.setTestOnBorrow(jedisConfig.isTestOnBorrow());
		jedisPoolConfig.setTestOnReturn(jedisConfig.isTestOnReturn());
		jedisPoolConfig.setAbandonedConfigApplied(false);
		
		jedisWrapper = new JedisWrapper(host, "SubscribeThread");
		jedisWrapper.init(jedisPoolConfig);
	}
	
	private void closeJedisWrapper() {
		if (jedisWrapper != null && jedisWrapper.isInitSuccess()) {
			jedisWrapper.destroy();
			jedisWrapper = null;
		}
	}

}
