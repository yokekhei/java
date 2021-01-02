package org.yokekhei.examples.jedis.sentinel.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

	private Properties prop;
	private JedisConfig jedisConfig;
	
	public AppConfig() {
		this.prop = new Properties();
		this.jedisConfig = new JedisConfig();
	}
	
	public void load(String configFilePath) throws IOException {
		InputStream input = null;
		
		try {
			input = new FileInputStream(configFilePath);
			this.prop.load(input);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}
	
	public String getSentinel() {
		return this.prop.getProperty("sentinel", "127.0.0.1:26379");
	}
	
	public Integer getJedisSocketTimeout() {
		return Integer.parseInt(this.prop.getProperty("jedisSocketTimeout", "2000"));
	}
	
	public Integer getJedisMaxTotal() {
		return Integer.parseInt(this.prop.getProperty("jedisMaxTotal", "8"));
	}
	
	public Integer getJedisMaxIdle() {
		return Integer.parseInt(this.prop.getProperty("jedisMaxIdle", "8"));
	}
	
	public Integer getJedisMinIdle() {
		return Integer.parseInt(this.prop.getProperty("jedisMinIdle", "0"));
	}
	
	public Long getJedisMaxWaitMillis() {
		return Long.parseLong(this.prop.getProperty("jedisMaxWaitMillis", "-1"));
	}
	
	public Boolean isJedisTestOnBorrow() {
		return Boolean.parseBoolean(this.prop.getProperty("jedisTestOnBorrow"));
	}
	
	public Boolean isJedisTestOnReturn() {
		return Boolean.parseBoolean(this.prop.getProperty("jedisTestOnReturn"));
	}
	
	public Integer getJedisTimeBtwEvictionMs() {
		return Integer.parseInt(this.prop.getProperty("jedisTimeBtwEvictionMs", "-1"));
	}
	
	public Boolean isJedisAbandonedConfigApplied() {
		return Boolean.parseBoolean(this.prop.getProperty("jedisAbandonedConfigApplied"));
	}
	
	public Integer getAbandonedTimeout() {
		return Integer.parseInt(this.prop.getProperty("jedisAbandonedTimeout", "300"));
	}
	
	public Boolean isJedisAbandonedOnBorrow() {
		return Boolean.parseBoolean(this.prop.getProperty("jedisAbandonedOnBorrow"));
	}
	
	public Boolean isJedisAbandonedOnMaintenance() {
		return Boolean.parseBoolean(this.prop.getProperty("jedisAbandonedOnMaintenance"));
	}
	
	public Boolean isJedisLogAbandoned() {
		return Boolean.parseBoolean(this.prop.getProperty("jedisLogAbandoned"));
	}
	
	public JedisConfig getJedisConfig() {
		jedisConfig.setSocketTimeout(getJedisSocketTimeout());
		jedisConfig.setMaxTotal(getJedisMaxTotal());
		jedisConfig.setMaxIdle(getJedisMaxIdle());
		jedisConfig.setMinIdle(getJedisMinIdle());
		jedisConfig.setMaxWaitMillis(getJedisMaxWaitMillis());
		jedisConfig.setTestOnBorrow(isJedisTestOnBorrow());
		jedisConfig.setTestOnReturn(isJedisTestOnReturn());
		jedisConfig.setTimeBtwEvictionMs(getJedisTimeBtwEvictionMs());
		jedisConfig.setAbandonedConfigApplied(isJedisAbandonedConfigApplied());
		jedisConfig.setAbandonedTimeout(getAbandonedTimeout());
		jedisConfig.setAbandonedOnBorrow(isJedisAbandonedOnBorrow());
		jedisConfig.setAbandonedOnMaintenance(isJedisAbandonedOnMaintenance());
		jedisConfig.setLogAbandoned(isJedisLogAbandoned());
		
		return jedisConfig;
	}
	
}
