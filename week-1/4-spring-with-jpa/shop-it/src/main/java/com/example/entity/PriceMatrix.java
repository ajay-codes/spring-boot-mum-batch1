package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "price_matrix")
@Data
public class PriceMatrix {

    @Id
    private String id;
    @Column(name = "product_id", nullable = false, unique = true)
    private int productId;
    private double price;
    
}
