package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*

ACID

A - Atomicity: All operations within a transaction are treated as a single unit. Either all operations succeed, or none of them do. This ensures that the database remains in a consistent state even in the event of a failure.
C - Consistency: A transaction must bring the database from one valid state to another valid state. This means that any data written to the database must be valid according to all defined rules, including constraints, cascades, and triggers.
I - Isolation: Transactions are isolated from each other until they are completed. This means that the
D - Durability: Once a transaction has been committed, it will remain so, even in the event of a system failure. This is typically achieved through the use of transaction logs and backups.


------------
Atomicity 
-----------

before -> start transaction 
after-success -> commit transaction
after-failure -> rollback transaction
after -> close transaction resources ( e.g connection)
*/

@Component
@Aspect
@Order(2) // Set the order of this aspect if you have multiple aspects
public class TransactionAspect {

    private static final Logger logger = LoggerFactory.getLogger("shop-it-service");

    // --- Alternative approach: individual advice methods instead of @Around ---
    // Uncomment these (and comment out @Around below) to use separate advice types:
    //
    // @Before("execution(* com.example.service.OnlineOrderService.createOrder(..))")
    // public void startTxn() {
    //     logger.info(">>>>>>>Transaction started.<<<<<<<");
    // }
    //
    // @AfterReturning("execution(* com.example.service.OnlineOrderService.createOrder(..))")
    // public void commitTxn() {
    //     logger.info(">>>>>>>Transaction committed successfully.<<<<<<<");
    // }
    //
    // @AfterThrowing("execution(* com.example.service.OnlineOrderService.createOrder(..))")
    // public void rollbackTxn() {
    //     logger.info(">>>>>>>Transaction rolled back due to an error.<<<<<<<");
    // }
    //
    // @After("execution(* com.example.service.OnlineOrderService.createOrder(..))")
    // public void closeTxnResources() {
    //     logger.info(">>>>>>>Transaction resources closed.<<<<<<<");
    // }
    // --- End alternative approach ---

    @Around("execution(* com.example.service.OnlineOrderService.createOrder(..))")
    public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            logger.info(">>>>>>>Transaction started.<<<<<<<");
            result = joinPoint.proceed();
            logger.info(">>>>>>>Transaction committed successfully.<<<<<<<");
        } catch (Throwable ex) {
            logger.info(">>>>>>>Transaction rolled back due to an error.<<<<<<<");
            throw ex; // rethrow the exception after logging
        } finally {
            logger.info(">>>>>>>Transaction resources closed.<<<<<<<");
        }
        return result;
    }
}
