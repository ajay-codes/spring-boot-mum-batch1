package com.example.ordercommand.repository;

import com.example.ordercommand.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
