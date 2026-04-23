package com.example.orderquery.event;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class OrderEvent {
    private String eventType;
    private Long orderId;
    private Long customerId;
    private String customerName;
    private String itemName;
    private Integer quantity;
    private Double price;
    private String status;
    private String createdAt;
    private String eventTimestamp;
}
