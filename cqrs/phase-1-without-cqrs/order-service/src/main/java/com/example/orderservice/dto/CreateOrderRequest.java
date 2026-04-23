package com.example.orderservice.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateOrderRequest {
    private Long customerId;
    private String customerName;
    private String itemName;
    private Integer quantity;
    private Double price;
}
