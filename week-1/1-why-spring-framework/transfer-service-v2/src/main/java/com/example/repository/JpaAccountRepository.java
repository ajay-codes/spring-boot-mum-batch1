package com.example.repository;

import org.slf4j.Logger;

import com.example.model.Account;

public class JpaAccountRepository implements AccountRepository {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("txr-service");

    public JpaAccountRepository() {
        logger.info("JpaAccountRepository instance created.");
    }

    public Account findByNumber(String number) {
        logger.info("Finding account by number: {}", number);
        // JPA query to find account by number
        // For demonstration, returning a dummy account
        return new Account(number, "John Doe", 1000.0);
    }

    public void update(Account account) {
        logger.info("Updating account: {}", account);
        // JPA query to update account details
        // For demonstration, just logging
        logger.info("Account updated: {}", account);
    }

}
