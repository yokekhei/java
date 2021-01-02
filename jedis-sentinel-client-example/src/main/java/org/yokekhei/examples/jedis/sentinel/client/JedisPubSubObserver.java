package org.yokekhei.examples.jedis.sentinel.client;

public interface JedisPubSubObserver {
	
	public void onReceiveMessage(String channel, String message);
	
}
