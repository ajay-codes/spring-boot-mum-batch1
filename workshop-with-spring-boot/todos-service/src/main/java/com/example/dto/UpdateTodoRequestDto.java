package com.example.dto;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "UpdateTodoRequest", description = "Request payload for updating a todo")
public class UpdateTodoRequestDto {

    @Schema(example = "Finish project report")
    private String title;

    @Schema(example = "Finalize sections and submit by 5 PM")
    private String description;

    @Schema(example = "WORK", description = "Allowed values: WORK, PERSONAL, ENTERTAINMENT, HEALTH, OTHER")
    private String category;

    @Schema(example = "true")
    private Boolean completed;

}
