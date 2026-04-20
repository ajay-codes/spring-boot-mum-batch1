package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.PriceMatrix;

public interface PriceMatrixRepository extends JpaRepository<PriceMatrix, Long> {
    @Query("SELECT p.price FROM PriceMatrix p WHERE p.productId = :productId")
    double getPrice(int productId);
}
