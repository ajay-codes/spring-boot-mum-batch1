package com.example.dto;

import lombok.Data;

@Data
public class CreateTodoResponseDto {

    private Long id;
    private String title;
    private String description;
    private String category;

}
