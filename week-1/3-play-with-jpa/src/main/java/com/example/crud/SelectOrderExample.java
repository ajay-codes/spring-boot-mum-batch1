package com.example.crud;

import com.example.entity.Customer;
import com.example.entity.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class SelectOrderExample {

    public static void main(String[] args) {

        // em <-- emf <-- persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin(); // Start transaction

        // IN JPA, we can select the data 3 ways:
        // way-1: find() method -> it is used to find an entity by its primary key. It
        // returns the entity object if found, otherwise it returns null.
        // way-2: JPQL (Java Persistence Query Language) -> it is a query language that
        // is used to query the database. It is similar to SQL but it is
        // object-oriented.
        // way-3: Criteria API -> it is a type-safe query language that is used to query
        // the database. It is similar to JPQL but it is type-safe.

        Order order = em.find(Order.class, "ORD123");
        System.out.println(order);
        Customer customer = order.getCustomer();
        System.out.println(customer);
        customer.getAddressList().forEach(System.out::println);

        em.getTransaction().commit(); // Commit transaction
        em.close();
        emf.close();

    }

}
