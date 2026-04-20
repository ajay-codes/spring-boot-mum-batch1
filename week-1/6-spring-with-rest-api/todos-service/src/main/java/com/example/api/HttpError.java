package com.example.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class HttpError {
    private String path;
    private String httpMethod;
    private String message;
}
