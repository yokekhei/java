package org.yokekhei.examples.jedis.sentinel.client;

import java.util.Map;

import redis.clients.jedis.Jedis;

public class JedisCommandDemo {
	RedisInstance ri;
	
	public JedisCommandDemo(RedisInstance ri) {
		this.ri = ri;
	}
	
	public void testGetAll() {
		Jedis jedis = null;
		
		try {
			jedis = ri.getJedisWrapper().getJedis();
			Map<String, String> data = jedis.hgetAll("instrument:BME.MYN0179");
			System.out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					jedis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void testSubscribe() {
		JedisPubSubListener listener = new JedisPubSubListener(ri);
		String[] channels = new String[2];
		channels[0] = "BME";
		channels[1] = "NMS";
		SubscribeThread subscribeThread = new SubscribeThread(
				ri.getHost(), ri.getJedisConfig(), listener, channels);
		
		int count = 0;
		while (count < 10) {
			System.out.println("\n\n-------- SubscribeThread Count " + count + " -------------");
			
			subscribeThread.start();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			subscribeThread.stop();
			count++;
		}
		
		subscribeThread.terminate();
	}
	
	public void testJedisSentinelPool() {
		for (int i=0; i<10000; i++) {
			ri.destroy();
			ri.init();
			
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testKillClient(String host, int port) {
		Jedis jedis = null;
		
		try {
			jedis = ri.getJedisWrapper().getJedis();
			jedis.getClient().clientKill(String.format("%s:%s", host, port));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					jedis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void testConcurrency() {
		for (int i=0; i<100; i++) {
			Thread a = new Thread() {
				@Override
				public void run() {
					while (true) {
						testGetAll();
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			
			a.start();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
