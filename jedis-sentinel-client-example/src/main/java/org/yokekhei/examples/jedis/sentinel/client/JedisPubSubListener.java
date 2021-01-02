package org.yokekhei.examples.jedis.sentinel.client;

import java.util.Date;

import redis.clients.jedis.JedisPubSub;

public class JedisPubSubListener extends JedisPubSub {
	
	private String host;
	private String subscriberName;
	private boolean isClosed;
	private JedisPubSubObserver jedisPubSubObserver;
	
	public JedisPubSubListener(RedisInstance redisInstance) {
		this.host = redisInstance.getHost();
		this.subscriberName = "";
		this.isClosed = false;
		this.jedisPubSubObserver = redisInstance.getJedisPubSubObserver();
	}
	
	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
	
	public void setSubscriberName(String name) {
		subscriberName = name;
	}
	
	public void onMessage(String channel, String message) {
		if (isClosed) {
			unsubscribe();
		}
		
		jedisPubSubObserver.onReceiveMessage(channel, message);
	}
	
	public void onSubscribe(String channel, int subscribedChannels) {
		System.out.println(new Date() + " [REDIS " + host + "] " + subscriberName + " onSubscribe()=" + channel + ", int=" + subscribedChannels);
		
		if (isClosed) {
			unsubscribe();
		}
	}
	
	public void onUnsubscribe(String channel, int subscribedChannels) {
		System.out.println(new Date() + " [REDIS " + host + "] " + subscriberName + " onUnsubscribe()=" + channel + ", int=" + subscribedChannels);
	}
	
	public void onPSubscribe(String pattern, int subscribedChannels) {
	}
	
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
	}
	
	public void onPMessage(String pattern, String channel, String message) {
	}
	
}
