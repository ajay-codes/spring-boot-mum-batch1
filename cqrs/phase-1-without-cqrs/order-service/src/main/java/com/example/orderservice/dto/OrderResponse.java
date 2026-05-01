package com.example.orderservice.dto;

import com.example.orderservice.entity.Order;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String itemName;
    private Integer quantity;
    private Double price;
    private String status;
    private LocalDateTime createdAt;

    public static OrderResponse fromEntity(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .customerName(order.getCustomerName())
                .itemName(order.getItemName())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
