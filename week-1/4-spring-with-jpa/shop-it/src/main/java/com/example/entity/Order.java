package com.example.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private Double amount;
    @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date orderDate;
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private OrderStatus status;
}
