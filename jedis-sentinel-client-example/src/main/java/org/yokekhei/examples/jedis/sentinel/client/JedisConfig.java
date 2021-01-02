package org.yokekhei.examples.jedis.sentinel.client;

public class JedisConfig {
	
	private Integer socketTimeout;
	private Integer maxTotal;
	private Integer maxIdle;
	private Integer minIdle;
	private Long maxWaitMillis;
	private boolean testOnBorrow;
	private boolean testOnReturn;
	private Integer timeBtwEvictionMs;
	public boolean abandonedConfigApplied;
	private Integer abandonedTimeout;
	private boolean abandonedOnBorrow;
	private boolean abandonedOnMaintenance;
	private boolean logAbandoned;
	
	public JedisConfig() {
		socketTimeout = 10000;
		maxTotal = 1000;
		maxIdle = 500;
		minIdle = 1;
		maxWaitMillis = 1000L;
		testOnBorrow = true;
		testOnReturn = true;
		timeBtwEvictionMs = 6000;
		abandonedConfigApplied = true;
		abandonedTimeout = 60;
		abandonedOnBorrow = true;
		abandonedOnMaintenance = true;
		logAbandoned = true;
	}

	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(Integer socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(Long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public Integer getTimeBtwEvictionMs() {
		return timeBtwEvictionMs;
	}

	public void setTimeBtwEvictionMs(Integer timeBtwEvictionMs) {
		this.timeBtwEvictionMs = timeBtwEvictionMs;
	}
	
	public boolean isAbandonedConfigApplied() {
		return abandonedConfigApplied;
	}

	public void setAbandonedConfigApplied(boolean abandonedConfigApplied) {
		this.abandonedConfigApplied = abandonedConfigApplied;
	}

	public Integer getAbandonedTimeout() {
		return abandonedTimeout;
	}

	public void setAbandonedTimeout(Integer abandonedTimeout) {
		this.abandonedTimeout = abandonedTimeout;
	}

	public boolean isAbandonedOnBorrow() {
		return abandonedOnBorrow;
	}

	public void setAbandonedOnBorrow(boolean abandonedOnBorrow) {
		this.abandonedOnBorrow = abandonedOnBorrow;
	}

	public boolean isAbandonedOnMaintenance() {
		return abandonedOnMaintenance;
	}

	public void setAbandonedOnMaintenance(boolean abandonedOnMaintenance) {
		this.abandonedOnMaintenance = abandonedOnMaintenance;
	}

	public boolean isLogAbandoned() {
		return logAbandoned;
	}

	public void setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}
	
}
