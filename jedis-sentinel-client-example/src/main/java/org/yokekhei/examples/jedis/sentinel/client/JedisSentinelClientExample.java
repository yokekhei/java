package org.yokekhei.examples.jedis.sentinel.client;

import java.io.IOException;
import java.util.Scanner;

public class JedisSentinelClientExample implements JedisPubSubObserver {

	private RedisInstance ri;
	
	public static void main(String[] args) {
		JedisSentinelClientExample example = new JedisSentinelClientExample();
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter choice :");
		int choice = scanner.nextInt();
		 
		example.run(choice);
		
		example.close();
		scanner.close();
	}
	
	public JedisSentinelClientExample() {
		try {
			AppConfig appConfig = new AppConfig();
			appConfig.load("application.properties");
			
			ri = new RedisInstance(Constants.REDIS_INSTANCE.INSTANCE_0,
					appConfig.getSentinel(), appConfig.getJedisConfig(), this);
			ri.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onReceiveMessage(String channel, String message) {
		System.out.println("channel: " + channel + "; message: " + message);
	}
	
	public void run(int choice) {
		JedisCommandDemo demo = new JedisCommandDemo(ri);
		
		switch (choice) {
		case 1:
			demo.testGetAll();
			break;
		
		case 2:
			demo.testSubscribe();
			break;
			
		case 3:
			demo.testJedisSentinelPool();
			break;
			
		case 4:
			  Scanner scanner = new Scanner(System.in);
			  System.out.println("Enter host :");
			  String host = scanner.next();
			  System.out.println("Enter port: ");
			  int port = scanner.nextInt();
			  scanner.close();
			 
			demo.testKillClient(host, port);
			break;
			
		case 5:
			demo.testConcurrency();
			break;
			
		default:
			System.err.println("Invalid choice");
		}
	}
	
	public void close() {
		ri.destroy();
	}

}
