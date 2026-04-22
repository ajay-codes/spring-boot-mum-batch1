package com.example.service;

import org.junit.jupiter.api.Test;

import com.example.dto.CreateTodoRequestDto;
import com.example.dto.CreateTodoResponseDto;
import com.example.entity.Todo;
import com.example.entity.User;
import com.example.repository.TodoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.mockito.*;

public class TodoServiceUnitTest {

    private TodoService todoService;
    TodoRepository mockTodoRepository;
    UserService mockUserService;

    @BeforeEach
    public void setUp() {
        mockTodoRepository = Mockito.mock(TodoRepository.class);
        mockUserService = Mockito.mock(UserService.class);
        todoService = new TodoService(mockTodoRepository, mockUserService);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("john", null, java.util.List.of()));

        User currentUser = new User();
        currentUser.setId(10L);
        currentUser.setUsername("john");
        Mockito.when(mockUserService.getUserByUsername("john")).thenReturn(currentUser);
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

        CreateTodoResponseDto createdTodo = todoService.createTodo(createTodoDto);

        // Then
        assertNotNull(createdTodo);
        assertEquals(title, createdTodo.getTitle());
        assertEquals(description, createdTodo.getDescription());
        assertEquals("OTHER", createdTodo.getCategory());
    }

    @org.junit.jupiter.api.AfterEach
    public void cleanUp() {
        SecurityContextHolder.clearContext();
    }

}
