package com.example;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.example.config.DataSourceConfiguration;
import com.example.service.OnlineOrderService;
import com.example.service.OrderService;

@Configuration
@Import({
	DataSourceConfiguration.class
})
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example")
@EnableAspectJAutoProxy
// Main entry point: demonstrates Spring container lifecycle (init -> run -> shutdown)
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
		
		logger.info(onlineOrderService.getClass().getName());

		List<String> cart1= List.of("1", "2", "3");
		onlineOrderService.createOrder(cart1);
		logger.info("");
		onlineOrderService.createOrder(cart1);


		//---------------------------------------------
		// shutdown phase
		// ---------------------------------------------
		logger.info("-".repeat(40));
		context.close();
		logger.info("ShopItApplication shutting down...");
		logger.info("-".repeat(40));

		
	}

}
