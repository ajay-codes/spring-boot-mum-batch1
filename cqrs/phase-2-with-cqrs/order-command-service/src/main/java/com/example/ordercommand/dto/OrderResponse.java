package com.example.ordercommand.dto;

import com.example.ordercommand.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getCustomerName(),
                order.getItemName(),
                order.getQuantity(),
                order.getPrice(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
}
