package com.example.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dto.CreateTodoRequestDto;
import com.example.dto.CreateTodoResponseDto;
import com.example.security.JwtAuthFilter;
import com.example.service.TodoService;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc(addFilters = false)
class TodoControllerRestAssuredTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @AfterEach
    void tearDown() {
        RestAssuredMockMvc.reset();
    }

    @Test
    void createTodo_returnsTodoResponse() {
        CreateTodoResponseDto responseDto = new CreateTodoResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Learn REST Assured");
        responseDto.setDescription("Write create todo API tests");
        responseDto.setCategory("OTHER");

        willReturn(responseDto).given(todoService).createTodo(any(CreateTodoRequestDto.class));

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
                .post("/api/todos")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("title", equalTo("Learn REST Assured"))
                .body("description", equalTo("Write create todo API tests"))
                .body("category", equalTo("OTHER"));
    }
}
