package com.example.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.dto.CreateTodoRequestDto;
import com.example.dto.CreateTodoResponseDto;
import com.example.entity.Todo;
import com.example.repository.TodoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;

import org.mockito.*;

public class TodoServiceUnitTest {

    private TodoService todoService;
    TodoRepository mockTodoRepository;
    TodoCategoryInferenceService mockTodoCategoryInferenceService;

    @BeforeEach
    public void setUp() {
        mockTodoRepository = Mockito.mock(TodoRepository.class);
        mockTodoCategoryInferenceService = Mockito.mock(TodoCategoryInferenceService.class);
        todoService = new TodoService(mockTodoRepository, mockTodoCategoryInferenceService);
    }

    @Test
    public void testCreateTodo() {
        // Given
        String title = "Test Todo";
        String description = "This is a test todo item.";

        CreateTodoRequestDto createTodoDto = new CreateTodoRequestDto();
        createTodoDto.setTitle(title);
        createTodoDto.setDescription(description);

        // When

        Mockito.when(mockTodoRepository.save(Mockito.any(Todo.class))).thenAnswer(invocation -> {
            Todo todo = invocation.getArgument(0);
            todo.setId(1L); // Simulate database-generated ID
            return todo;
        });
        Mockito.when(mockTodoCategoryInferenceService.inferCategory(title, description))
                .thenReturn(com.example.entity.TodoCategory.OTHER);

        CreateTodoResponseDto createdTodo = todoService.createTodo(createTodoDto);

        // Then
        assertNotNull(createdTodo);
        assertEquals(title, createdTodo.getTitle());
        assertEquals(description, createdTodo.getDescription());
        assertEquals("OTHER", createdTodo.getCategory());
    }

}
