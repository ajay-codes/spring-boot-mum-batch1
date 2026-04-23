package com.example.orderquery.service;

import com.example.orderquery.dto.OrderResponse;
import com.example.orderquery.repository.OrderReadModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderReadModelRepository repository;

    public List<OrderResponse> getAllOrders() {
        return repository.findAll().stream()
                .map(OrderResponse::fromReadModel)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {
        return repository.findById(id)
                .map(OrderResponse::fromReadModel)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + id));
    }
}
