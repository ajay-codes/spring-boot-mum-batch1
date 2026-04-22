package com.example.dto;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "CreateTodoRequest", description = "Request payload for creating a todo")
public class CreateTodoRequestDto {

    @Schema(example = "Buy groceries")
    private String title;

    @Schema(example = "Milk, bread, eggs")
    private String description;

}
