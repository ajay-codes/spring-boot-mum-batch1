package com.example.dto;

import java.time.LocalDateTime;

public record HttpError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path) {
}
