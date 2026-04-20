package com.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class TransferRequest {
    private String fromAccountNumber;
    private String toAccountNumber;
    private double amount;

}
