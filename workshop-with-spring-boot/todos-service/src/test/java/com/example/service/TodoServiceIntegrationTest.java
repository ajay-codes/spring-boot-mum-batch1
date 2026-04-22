package com.example.service;

import org.junit.jupiter.api.Test;

import com.example.dto.CreateTodoRequestDto;
import com.example.dto.CreateTodoResponseDto;
import com.example.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class TodoServiceIntegrationTest {

    @Autowired
    private TodoService todoService;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setupAuth() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("john", null, java.util.List.of()));

        User currentUser = new User();
        currentUser.setId(10L);
        currentUser.setUsername("john");
        given(userService.getUserByUsername("john")).willReturn(currentUser);
    }

    @AfterEach
    void clearAuth() {
        SecurityContextHolder.clearContext();
    }

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
