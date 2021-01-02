package org.yokekhei.examples.jedis.sentinel.client;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Set;

import org.apache.commons.pool2.impl.AbandonedConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

public class JedisWrapper {
	
	private String host;
	private String source;
	private boolean isInitSuccess;
	private MyJedisSentinelPool jedisPool;
	private AbandonedConfig abandonedConfig;
	
	public JedisWrapper(String host, String source) {
		this.host = host;
		this.source = source;
		this.isInitSuccess = false;
	}
	
	public void init(JedisConfig jedisConfig) {
		try {
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
			
			// Maximum active connections to Redis instance; Default value = 8
			jedisPoolConfig.setMaxTotal(jedisConfig.getMaxTotal());
			
			// Tests whether connection is dead when connection retrieval method is called; Default value = false
			jedisPoolConfig.setTestOnBorrow(jedisConfig.isTestOnBorrow());
			
			// Tests whether connection is dead when returning a connection to the pool; Default value = false
			jedisPoolConfig.setTestOnReturn(jedisConfig.isTestOnReturn());
			
			// Number of connections to Redis that just sit there and do nothing; Default value = 8
			jedisPoolConfig.setMaxIdle(jedisConfig.getMaxIdle());
			
			// Minimum number of idle connections to Redis (can be seen as always open and ready to serve); Default value = 0
			jedisPoolConfig.setMinIdle(jedisConfig.getMinIdle());
			
			// Idle connection checking period; Default value = 30000
			jedisPoolConfig.setTimeBetweenEvictionRunsMillis(jedisConfig.getTimeBtwEvictionMs());
			
			// The maximum number of milliseconds that the client needs to wait when no connection is available.
			jedisPoolConfig.setMaxWaitMillis(jedisConfig.getMaxWaitMillis());
			
			Set<String> sentinels = JedisSentinelUtil.addSentinel(host);
			jedisPool = new MyJedisSentinelPool(Constants.MASTER_NAME, sentinels, jedisPoolConfig, jedisConfig.getSocketTimeout());
			
			printJedisPoolConfig(jedisPoolConfig, host, source);
			
			//to avoid pool exhausted incident
			abandonedConfig = new AbandonedConfig();
			abandonedConfig.setLogAbandoned(jedisConfig.isLogAbandoned());
			abandonedConfig.setLogWriter(new PrintWriter(System.out, true));
			abandonedConfig.setRemoveAbandonedOnBorrow(jedisConfig.isAbandonedOnBorrow());
			abandonedConfig.setRemoveAbandonedOnMaintenance(jedisConfig.isAbandonedOnMaintenance());
			abandonedConfig.setRemoveAbandonedTimeout(jedisConfig.getAbandonedTimeout()); // in seconds
			
			if (jedisConfig.isAbandonedConfigApplied()) {
				jedisPool.getInternalPool().setAbandonedConfig(abandonedConfig);
				printAbandonedConfig(abandonedConfig, host, source);
			}
			
			isInitSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error trying to initJedisWrapper("+ host + "):" + e.getMessage());
			isInitSuccess = false;
		}
	}
	
	public void init(JedisPoolConfig jedisPoolConfig, Integer socketTimeout, String source) {
		Set<String> sentinels = JedisSentinelUtil.addSentinel(host);
		jedisPool = new MyJedisSentinelPool(Constants.MASTER_NAME, sentinels, jedisPoolConfig, socketTimeout);
		
		isInitSuccess = true;
		
		printJedisPoolConfig(jedisPoolConfig, host, source);
	}
	
	public boolean isInitSuccess() {
		return isInitSuccess;
	}
	
	public MyJedisSentinelPool getJedisPool() {
		return jedisPool;
	}
	
	public Jedis getJedis() {
		if (jedisPool != null) {
			return jedisPool.getResource();
		}
		
		return null;
	}
	
	public void destroy() {
		isInitSuccess = false;
		
		if (jedisPool != null) {
			jedisPool.destroy();
		}
	}
	
	private void printJedisPoolConfig(JedisPoolConfig jedisPoolConfig, String host, String source) {
		System.out.println(" -------------- JEDIS POOL CONFIG [REDIS " + host + "][" + source + "] -------------- ");
		System.out.println("---- >> jedisPoolConfig.getMaxTotal() = " + jedisPoolConfig.getMaxTotal());
		System.out.println("---- >> jedisPoolConfig.getTestOnBorrow() = " + jedisPoolConfig.getTestOnBorrow());
		System.out.println("---- >> jedisPoolConfig.getTestOnReturn() = " + jedisPoolConfig.getTestOnReturn());
		System.out.println("---- >> jedisPoolConfig.getMaxIdle() = " + jedisPoolConfig.getMaxIdle());
		System.out.println("---- >> jedisPoolConfig.getMinIdle() = " + jedisPoolConfig.getMinIdle());
		System.out.println("---- >> jedisPoolConfig.getTimeBetweenEvictionRunsMillis() = " + jedisPoolConfig.getTimeBetweenEvictionRunsMillis());
		System.out.println("---- >> jedisPoolConfig.getMaxWaitMillis() = " + jedisPoolConfig.getMaxWaitMillis());
		System.out.println("---- >> jedisPoolConfig.getBlockWhenExhausted() = " + jedisPoolConfig.getBlockWhenExhausted());
		System.out.println("---- >> jedisPoolConfig.getEvictionPolicyClassName() = " + jedisPoolConfig.getEvictionPolicyClassName());
		System.out.println("---- >> jedisPoolConfig.getLifo() = " + jedisPoolConfig.getLifo());
		System.out.println("---- >> jedisPoolConfig.getMinEvictableIdleTimeMillis() = " + jedisPoolConfig.getMinEvictableIdleTimeMillis());
		System.out.println("---- >> jedisPoolConfig.getNumTestsPerEvictionRun() = " + jedisPoolConfig.getNumTestsPerEvictionRun());
		System.out.println("---- >> jedisPoolConfig.getSoftMinEvictableIdleTimeMillis() = " + jedisPoolConfig.getSoftMinEvictableIdleTimeMillis());
		System.out.println("---- >> jedisPoolConfig.getTestWhileIdle() = " + jedisPoolConfig.getTestWhileIdle());
		System.out.println(" -------------- " + new Date() + " -------------- ");
	}
	
	void printAbandonedConfig(AbandonedConfig ac, String host, String source) {
		System.out.println(" -------------- ABANDONED CONFIG [REDIS " + host + "][" + source + "] -------------- ");
		System.out.println("---- >> abandonedConfig.getLogAbandoned() = " + ac.getLogAbandoned());
		System.out.println("---- >> abandonedConfig.getRemoveAbandonedTimeout() = " + ac.getRemoveAbandonedTimeout());
		System.out.println("---- >> abandonedConfig.getRemoveAbandonedOnBorrow = " + ac.getRemoveAbandonedOnBorrow());
		System.out.println("---- >> abandonedConfig.getRemoveAbandonedOnMaintenance() = " + ac.getRemoveAbandonedOnMaintenance());
		System.out.println(" -------------- " + new Date() + " -------------- ");
	}
	
}
