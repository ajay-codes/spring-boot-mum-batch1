package com.example.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.repository.PriceMatrix;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

// @Component("onlineOrderService")
@Service("onlineOrderService")
public class OnlineOrderService implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger("shop-it-service");

    // @Autowired
    // @Qualifier("sqlDatabasePriceMatrix")
    private PriceMatrix priceMatrix;

    @Autowired
    public OnlineOrderService(@Qualifier("sqlDatabasePriceMatrix") PriceMatrix priceMatrix) {
        this.priceMatrix = priceMatrix;
        logger.info("OnlineOrderService initialized with PriceMatrix.");
    }

    // @Autowired(required = false)
    // @Qualifier("nosqlDatabasePriceMatrix")
    // public void setPriceMatrix(PriceMatrix priceMatrix) {
    //     this.priceMatrix = priceMatrix;
    //     logger.info("PriceMatrix set for OnlineOrderService.");
    // }

    @PostConstruct
    public void init() {
        logger.info("OnlineOrderService init method called.");
    }

    @PreDestroy
    public void cleanup() {
        logger.info("OnlineOrderService cleanup method called.");
    }

    @Override
    public void createOrder(List<String> cart) {
        logger.info("Creating order for cart: " + cart);
        double totalPrice = 0.0;
        for (String item : cart) {
            // Simulate price retrieval for each item
            double price = priceMatrix.getPrice(item);
            totalPrice += price;
        }
        logger.info("Total price for the order: " + totalPrice);
        // create new order with the total price and cart items
        // persist the order to the database
        // publish order created event to message queue
        // if(totalPrice > 10) {
        //     throw new RuntimeException("Order total exceeds limit.");
        // }
        // send order confirmation email to the customer
        logger.info("Order created successfully.");
    }

}
