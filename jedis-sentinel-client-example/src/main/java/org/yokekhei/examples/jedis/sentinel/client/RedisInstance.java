package org.yokekhei.examples.jedis.sentinel.client;

import java.util.ArrayList;
import java.util.List;

public class RedisInstance {
	Constants.REDIS_INSTANCE redisInstance;
	private String host;
	private JedisConfig jedisConfig;
	private JedisWrapper jedisWrapper;
	private JedisPubSubObserver jedisPubSubObserver;
	private List<String> jedisPubSubChannels;
	private String[] channels;
	
	public RedisInstance(Constants.REDIS_INSTANCE redisInstance, String host, JedisConfig jedisConfig,
			JedisPubSubObserver jedisPubSubObserver) {
		this.redisInstance = redisInstance;
		this.host = host;
		this.jedisConfig = jedisConfig;
		this.jedisPubSubObserver = jedisPubSubObserver;
		this.jedisPubSubChannels = new ArrayList<String>();
	}
	
	public void init() {
		initJedisWrapper();
		
		if (jedisPubSubChannels.size() > 0) {
			channels = new String[jedisPubSubChannels.size()];
			
			for (int i=0; i<jedisPubSubChannels.size(); i++) {
				channels[i] = jedisPubSubChannels.get(i);
			}
		}
	}
	
	public Constants.REDIS_INSTANCE getRedisInstance() {
		return this.redisInstance;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public MyJedisSentinelPool getJedisPool() {
		return getJedisWrapper().getJedisPool();
	}
	
	public JedisPubSubObserver getJedisPubSubObserver() {
		return jedisPubSubObserver;
	}
	
	public List<String> getJedisPubSubChannels() {
		return jedisPubSubChannels;
	}
	
	public String[] getChannels() {
		return channels;
	}
	
	public JedisConfig getJedisConfig() {
		return jedisConfig;
	}
	
	public void setJedisConfig(JedisConfig jedisConfig) {
		this.jedisConfig = jedisConfig;
	}
	
	public JedisWrapper getJedisWrapper() {
		if (jedisWrapper == null) {
			initJedisWrapper();
		}
		
		return jedisWrapper;
	}
	
	public void destroy() {
		if (jedisWrapper != null) {
			jedisWrapper.destroy();
			jedisWrapper = null;
		}
	}
	
	private synchronized void initJedisWrapper() {
		if (jedisWrapper != null && jedisWrapper.isInitSuccess()) {
			return;
		}
		
		jedisWrapper = new JedisWrapper(host, redisInstance.toString());
		
		int count = 0;
		while (!jedisWrapper.isInitSuccess()) {
			jedisWrapper.init(jedisConfig);
			
			if (!jedisWrapper.isInitSuccess()) {
				try {
					count++;
					Thread.sleep(60000); //1min retry
					System.out.println("Retry initJedisWrapper(" + host + ") - " + count);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
