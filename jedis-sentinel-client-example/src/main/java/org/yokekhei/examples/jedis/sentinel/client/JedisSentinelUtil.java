package org.yokekhei.examples.jedis.sentinel.client;

import java.util.HashSet;
import java.util.Set;

public class JedisSentinelUtil {
	
	public static Set<String> addSentinel(String sentinelList) {
		Set<String> sentinels = new HashSet<String>();
		try {
			String[] tokens = sentinelList.split(";");
			for( int i = 0; i <= tokens.length - 1; i++)
				sentinels.add(tokens[i]);
		} catch (Exception ex) {
    		System.err.println("addSentinel error " + ex.getMessage());
    		return null;
    	}finally {
		}
		
		return sentinels;
	}
	
}
