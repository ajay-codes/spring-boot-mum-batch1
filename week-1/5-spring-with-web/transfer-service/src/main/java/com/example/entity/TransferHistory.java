package com.example.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import lombok.Data;

@Data
@Entity
@Table(name = "transfer_history")
public class TransferHistory {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private Double amount;
    @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date timestamp;
}
