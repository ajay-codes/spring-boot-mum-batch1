package com.example.service;

import org.springframework.stereotype.Service;

import com.example.dto.CreateTodoDto;
import com.example.entity.Todo;
import com.example.entity.TodoCategory;
import com.example.repository.TodoRepository;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo createTodo(CreateTodoDto createTodoDto) {
        // Logic to create a new todo item
        // business logic
        Todo todo = new Todo();
        todo.setTitle(createTodoDto.getTitle());
        todo.setDescription(createTodoDto.getDescription());
        todo.setCategory(TodoCategory.OTHER);

        return todoRepository.save(todo);
    }

}
