package com.example.service;

import org.junit.jupiter.api.Test;

import com.example.dto.CreateTodoDto;
import com.example.entity.Todo;
import com.example.repository.TodoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;

import org.mockito.*;
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

        CreateTodoDto createTodoDto = new CreateTodoDto();
        createTodoDto.setTitle(title);
        createTodoDto.setDescription(description);

        // When

        Todo createdTodo = todoService.createTodo(createTodoDto);

        // Then
        assertNotNull(createdTodo);
        assertEquals(title, createdTodo.getTitle());
        assertEquals(description, createdTodo.getDescription());

    }

}
