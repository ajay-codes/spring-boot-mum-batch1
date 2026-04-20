package com.example.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Order;
import com.example.entity.OrderStatus;
import com.example.repository.OrderRepository;
import com.example.repository.PriceMatrixRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service("onlineOrderService")
public class OnlineOrderService implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger("shop-it-service");

    private PriceMatrixRepository priceMatrixRepository;
    private OrderRepository orderRepository;

    @Autowired
    public OnlineOrderService(@Qualifier("priceMatrixRepository") PriceMatrixRepository priceMatrixRepository,
                              @Qualifier("orderRepository") OrderRepository orderRepository) {
        this.priceMatrixRepository = priceMatrixRepository;
        this.orderRepository = orderRepository;
        logger.info("OnlineOrderService initialized with PriceMatrixRepository and OrderRepository.");
    }


    @PostConstruct
    public void init() {
        logger.info("OnlineOrderService init method called.");
    }

    @PreDestroy
    public void cleanup() {
        logger.info("OnlineOrderService cleanup method called.");
    }

    @Transactional(
        rollbackFor = RuntimeException.class,
        noRollbackFor = IllegalArgumentException.class,
        isolation = org.springframework.transaction.annotation.Isolation.READ_COMMITTED,
        timeout = 30,
        propagation = org.springframework.transaction.annotation.Propagation.REQUIRED
    )
    @Override
    public void createOrder(List<String> cart) {
        logger.info("Creating order for cart: " + cart);
        double amount = 0.0;
        for (String item : cart) {
            // Simulate price retrieval for each item
            double price = priceMatrixRepository.getPrice(Integer.parseInt(item)); // db-call
            amount += price;
        }
        logger.info("Total price for the order: " + amount);
        // create new order with the total price and cart items
        Order order = new Order();
        order.setAmount(amount);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        // persist the order to the database
        orderRepository.save(order); // db-call
        // send order confirmation email to the customer
        logger.info("Order created successfully.");

    }

}
