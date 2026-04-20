package com.example;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.config.DatasourceConfiguration;
import com.example.service.TransferService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Main application class for the transfer service.
 * authors: team-3
 */

@Configuration
@Import(DatasourceConfiguration.class)
@ComponentScan(basePackages = "com.example")
public class Application {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("txr-service");

    public static void main(String[] args) {

        logger.info("Starting the transfer service application...");

        // -----------------------------------
        // Init / boot phase
        // ------------------------------------
        logger.info("-".repeat(50));
        // create & wire components
        ConfigurableApplicationContext context = null;
        context = new AnnotationConfigApplicationContext(Application.class);

        logger.info("Transfer service initialized successfully.");
        logger.info("-".repeat(50));
        // -----------------------------------
        // Use case execution phase
        // ------------------------------------
        TransferService transferService = context.getBean("impsTransferService", TransferService.class);
        transferService.transfer("1234567890", "0987654321", 10.00);
        // logger.info("-".repeat(25));
        // transferService.transfer("1234567890", "0987654321", 500.00);

        logger.info("-".repeat(50));
        // -----------------------------------
        // Shutdown phase
        // ------------------------------------
        // perform any cleanup if necessary (e.g., closing database connections)
        logger.info("Shutting down the transfer service application...");
        context.close();
        logger.info("-".repeat(50));
    }
}
