package com.example.crud;

import com.example.entity.Customer;
import com.example.entity.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Demonstrates inserting an Order entity (with a ManyToOne reference to Customer) using JPA EntityManager.
public class InsertOrderExample {

    public static void main(String[] args) {

        Order order = new Order();
        order.setOrderId("ORD123");
        order.setAmount(1000.00);
        Customer customer = new Customer();
        customer.setId(1);
        order.setCustomer(customer);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin(); // Start transaction
        em.persist(order); // Insert into DB
        em.getTransaction().commit(); // Commit transaction
        em.close();
        emf.close();

    }

}
