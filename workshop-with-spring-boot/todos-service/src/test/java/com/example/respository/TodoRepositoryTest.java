package com.example.respository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Todo;
import com.example.repository.TodoRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TodoRepositoryTest {
    /*
     * AAA - Arrange, Act, Assert
     */
    @Autowired
    private TodoRepository todoRepository;

    @Test
    @Transactional
    public void addTodo() {
        // Arrange
        Todo todo = new Todo();
        todo.setTitle("Learn Spring Boot");
        todo.setDescription("Learn how to use Spring Boot for building RESTful APIs");
        todo.setCompleted(false);
        // Act
        Todo savedTodo = todoRepository.save(todo);
        // Assert
        assertNotNull(savedTodo.getId());
    }

}
