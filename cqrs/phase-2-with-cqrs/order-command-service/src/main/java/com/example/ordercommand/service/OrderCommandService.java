package com.example.ordercommand.service;

import com.example.ordercommand.dto.CreateOrderRequest;
import com.example.ordercommand.dto.OrderResponse;
import com.example.ordercommand.dto.UpdateStatusRequest;
import com.example.ordercommand.entity.Order;
import com.example.ordercommand.entity.OrderStatus;
import com.example.ordercommand.event.OrderEvent;
import com.example.ordercommand.event.OrderEventProducer;
import com.example.ordercommand.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("[COMMAND] Creating order for customer: {}", request.getCustomerName());

        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .customerName(request.getCustomerName())
                .itemName(request.getItemName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();

        Order saved = orderRepository.save(order);

        OrderEvent event = OrderEvent.builder()
                .eventType("ORDER_CREATED")
                .orderId(saved.getId())
                .customerId(saved.getCustomerId())
                .customerName(saved.getCustomerName())
                .itemName(saved.getItemName())
                .quantity(saved.getQuantity())
                .price(saved.getPrice())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreatedAt().toString())
                .eventTimestamp(LocalDateTime.now().toString())
                .build();

        orderEventProducer.publish(event);

        return OrderResponse.fromEntity(saved);
    }

    public OrderResponse updateStatus(Long id, UpdateStatusRequest request) {
        log.info("[COMMAND] Updating order #{} status to {}", id, request.getStatus());

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + id));

        order.setStatus(OrderStatus.valueOf(request.getStatus()));
        Order saved = orderRepository.save(order);

        OrderEvent event = OrderEvent.builder()
                .eventType("ORDER_STATUS_UPDATED")
                .orderId(saved.getId())
                .customerId(saved.getCustomerId())
                .customerName(saved.getCustomerName())
                .itemName(saved.getItemName())
                .quantity(saved.getQuantity())
                .price(saved.getPrice())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreatedAt().toString())
                .eventTimestamp(LocalDateTime.now().toString())
                .build();

        orderEventProducer.publish(event);

        return OrderResponse.fromEntity(saved);
    }
}
