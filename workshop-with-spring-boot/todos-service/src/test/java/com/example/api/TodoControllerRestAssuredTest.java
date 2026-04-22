package com.example.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import com.example.dto.CreateTodoResponseDto;

import io.restassured.http.ContentType;

class TodoControllerRestAssuredTest {

    @Test
    void createTodo_returnsTodoResponse() {

        String requestBody = """
                {
                  "title": "Learn REST Assured",
                  "description": "Write create todo API tests"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post("http://localhost:8080/api/todos")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("title", equalTo("Learn REST Assured"))
                .body("description", equalTo("Write create todo API tests"))
                .body("category", equalTo("OTHER"));
    }
}
