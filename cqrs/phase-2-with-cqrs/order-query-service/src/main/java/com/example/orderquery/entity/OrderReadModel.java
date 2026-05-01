package com.example.orderquery.entity;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderReadModel {
    @PrimaryKey
    private Long id;
    @Column("customer_id")
    private Long customerId;
    @Column("customer_name")
    private String customerName;
    @Column("item_name")
    private String itemName;
    private Integer quantity;
    private Double price;
    private String status;
    @Column("created_at")
    private String createdAt;
}
