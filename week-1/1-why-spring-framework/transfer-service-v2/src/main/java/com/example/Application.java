package com.example;

import org.slf4j.Logger;

import com.example.repository.AccountRepository;
import com.example.repository.AccountRepositoryFactory;
import com.example.service.ImpsTransferService;
import com.example.service.TransferService;

/**
 * Main application class for the transfer service.
 * authors: team-3
 */

public class Application {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("txr-service");

    public static void main(String[] args) {

        logger.info("Starting the transfer service application...");

        // -----------------------------------
        // Init / boot phase
        // ------------------------------------
        logger.info("-".repeat(50));
        // creating components and wiring them together
        AccountRepository jdbcAccountRepository = AccountRepositoryFactory.createAccountRepository("jdbc");
        // AccountRepository jpaAccountRepository =
        // AccountRepositoryFactory.createAccountRepository("jpa");
        TransferService transferService = new ImpsTransferService(jdbcAccountRepository);

        logger.info("Transfer service initialized successfully.");
        logger.info("-".repeat(50));
        // -----------------------------------
        // Use case execution phase
        // ------------------------------------
        transferService.transfer("1234567890", "0987654321", 1000.00);
        logger.info("-".repeat(25));
        transferService.transfer("1234567890", "0987654321", 500.00);

        logger.info("-".repeat(50));
        // -----------------------------------
        // Shutdown phase
        // ------------------------------------
        // perform any cleanup if necessary (e.g., closing database connections)
        logger.info("Shutting down the transfer service application...");
        logger.info("-".repeat(50));
    }
}