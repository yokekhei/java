package org.yokekhei.examples.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ActiveMQClientExampleApplication implements ExitCodeGenerator {
	private static final Logger logger = LoggerFactory.getLogger(ActiveMQClientExampleApplication.class);
	
	private static int exitCode = 0;
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ActiveMQClientExampleApplication.class, args);
		ActiveMQClientExampleService service = applicationContext.getBean(ActiveMQClientExampleService.class);
		
		try {
			service.start();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			
			if (e.getCause() instanceof InterruptedException) {
				exitCode = 0;
			}
			else {
				exitCode = 1;
			}
			
			logger.error("Application exit code: " + exitCode);
		}
		
		System.exit(SpringApplication.exit(applicationContext));
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}
	
}
