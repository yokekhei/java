package org.yokekhei.examples.jedis.sentinel.client;

import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class MyJedisSentinelPool extends JedisSentinelPool {

	public MyJedisSentinelPool(String masterName, Set<String> sentinels,
			GenericObjectPoolConfig poolConfig, int timeout, String password,
			int database) {
		super(masterName, sentinels, poolConfig, timeout, password, database);
		// TODO Auto-generated constructor stub
	}

	public MyJedisSentinelPool(String masterName, Set<String> sentinels,
			GenericObjectPoolConfig poolConfig, int timeout, String password) {
		super(masterName, sentinels, poolConfig, timeout, password);
		// TODO Auto-generated constructor stub
	}

	public MyJedisSentinelPool(String masterName, Set<String> sentinels,
			GenericObjectPoolConfig poolConfig, int timeout) {
		super(masterName, sentinels, poolConfig, timeout);
		// TODO Auto-generated constructor stub
	}

	public MyJedisSentinelPool(String masterName, Set<String> sentinels,
			GenericObjectPoolConfig poolConfig, String password) {
		super(masterName, sentinels, poolConfig, password);
		// TODO Auto-generated constructor stub
	}

	public MyJedisSentinelPool(String masterName, Set<String> sentinels,
			GenericObjectPoolConfig poolConfig) {
		super(masterName, sentinels, poolConfig);
		// TODO Auto-generated constructor stub
	}

	public MyJedisSentinelPool(String masterName, Set<String> sentinels, String password) {
		super(masterName, sentinels, password);
		// TODO Auto-generated constructor stub
	}

	public MyJedisSentinelPool(String masterName, Set<String> sentinels) {
		super(masterName, sentinels);
		// TODO Auto-generated constructor stub
	}

	public GenericObjectPool<Jedis> getInternalPool() {
		return internalPool;
	}

}
