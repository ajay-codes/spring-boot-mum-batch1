package com.example.dto;

import lombok.Data;

@Data
public class CreateUserResponseDto {
    private String username;
    private String name;
    private String email;
}
