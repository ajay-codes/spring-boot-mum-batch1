package com.example.orderquery.dto;

import com.example.orderquery.entity.OrderReadModel;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String itemName;
    private Integer quantity;
    private Double price;
    private String status;
    private String createdAt;

    public static OrderResponse fromReadModel(OrderReadModel model) {
        return OrderResponse.builder()
                .id(model.getId())
                .customerId(model.getCustomerId())
                .customerName(model.getCustomerName())
                .itemName(model.getItemName())
                .quantity(model.getQuantity())
                .price(model.getPrice())
                .status(model.getStatus())
                .createdAt(model.getCreatedAt())
                .build();
    }
}
