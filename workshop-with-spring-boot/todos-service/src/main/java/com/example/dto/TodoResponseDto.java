package com.example.dto;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "TodoResponse", description = "Todo response payload")
public class TodoResponseDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Buy groceries")
    private String title;

    @Schema(example = "Milk, bread, eggs")
    private String description;

    @Schema(example = "OTHER")
    private String category;

    @Schema(example = "false")
    private boolean completed;

}
