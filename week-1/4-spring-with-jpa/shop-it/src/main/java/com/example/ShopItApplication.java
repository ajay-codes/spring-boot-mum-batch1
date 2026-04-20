package com.example;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.example.config.DataSourceConfiguration;
import com.example.service.OnlineOrderService;
import com.example.service.OrderService;

/**
 * Main entry point for the Shop-It application (Module 4).
 * Demonstrates Spring with JPA: entity mapping, Spring Data repositories,
 * and transaction management using @Transactional.
 */
@Configuration
// NOTE: @Import of DataSourceConfiguration is redundant here because it is already
// a @Configuration class that would be picked up by @ComponentScan. We import it
// explicitly for teaching purposes -- to show how manual DataSource configuration
// can override Spring Boot's auto-configured DataSource.
@Import({
	DataSourceConfiguration.class
})
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example")
@EnableAspectJAutoProxy
@EntityScan(basePackages = "com.example.entity")
public class ShopItApplication {

	private static final Logger logger = LoggerFactory.getLogger("shop-it-service");

	public static void main(String[] args) {

		logger.info("Starting ShopItApplication...");
		//---------------------------------------------
		// init/ boot phase
		// ---------------------------------------------
		logger.info("-".repeat(40));
		ConfigurableApplicationContext context;
		context=SpringApplication.run(ShopItApplication.class, args);
		logger.info("-".repeat(40));
		//---------------------------------------------
		// run phase
		// ---------------------------------------------
		OrderService onlineOrderService = context.getBean(OnlineOrderService.class);

		List<String> cart1= List.of("1", "2", "3");
		onlineOrderService.createOrder(cart1);
		logger.info("");
		List<String> cart2= List.of("1", "2");
		onlineOrderService.createOrder(cart2);


		//---------------------------------------------
		// shutdown phase
		// ---------------------------------------------
		logger.info("-".repeat(40));
		context.close();
		logger.info("ShopItApplication shutting down...");
		logger.info("-".repeat(40));

		
	}

}
