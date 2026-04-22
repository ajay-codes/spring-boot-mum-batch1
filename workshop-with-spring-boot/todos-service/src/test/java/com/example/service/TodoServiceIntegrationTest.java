package com.example.service;

import org.junit.jupiter.api.Test;

import com.example.dto.CreateTodoRequestDto;
import com.example.dto.CreateTodoResponseDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class TodoServiceIntegrationTest {

    @Autowired
    private TodoService todoService;

    @Test
    @Transactional
    public void testCreateTodo() {
        // Given
        String title = "Test Todo";
        String description = "This is a test todo item.";

        CreateTodoRequestDto createTodoDto = new CreateTodoRequestDto();
        createTodoDto.setTitle(title);
        createTodoDto.setDescription(description);

        // When

        CreateTodoResponseDto createdTodo = todoService.createTodo(createTodoDto);

        // Then
        assertNotNull(createdTodo);
        assertEquals(title, createdTodo.getTitle());
        assertEquals(description, createdTodo.getDescription());
        assertEquals("OTHER", createdTodo.getCategory());

    }

}
