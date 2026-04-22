package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI todosServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todos Service API")
                        .description("REST API for creating, listing, updating, deleting, and AI-categorizing todos.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Todos Service")
                                .email("support@example.com")));
    }
}
