package com.example.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

// Reads prices from an Excel file (stubbed for demo; returns a fixed price)
// @Component("excelPriceMatrix")
@Repository("excelPriceMatrix")
public class ExcelPriceMatrixRepository implements PriceMatrix {

    private static final Logger logger = LoggerFactory.getLogger("shop-it-service");

    public ExcelPriceMatrixRepository() {
        logger.info("ExcelPriceMatrixRepository initialized.");
    }

    @Override
    public double getPrice(String productId) {
        logger.info("Retrieving price for product ID: " + productId);
        // Placeholder for actual Excel reading logic
        // In a real implementation, this method would read from an Excel file
        // and return the price based on the product ID.
        // For demonstration purposes, let's return a fixed price.
        return 19.99;
    }

}
