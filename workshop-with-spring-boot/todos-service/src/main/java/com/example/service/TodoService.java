package com.example.service;

import org.springframework.stereotype.Service;

import com.example.dto.CreateTodoRequestDto;
import com.example.dto.CreateTodoResponseDto;
import com.example.entity.Todo;
import com.example.entity.TodoCategory;
import com.example.repository.TodoRepository;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public CreateTodoResponseDto createTodo(CreateTodoRequestDto createTodoDto) {

        // Logic to create a new todo item

        // business logic

        Todo todo = new Todo();
        todo.setTitle(createTodoDto.getTitle());
        todo.setDescription(createTodoDto.getDescription());
        todo.setCategory(TodoCategory.OTHER);

        Todo savedTodo = todoRepository.save(todo);

        CreateTodoResponseDto responseDto = new CreateTodoResponseDto();
        responseDto.setId(savedTodo.getId());
        responseDto.setTitle(savedTodo.getTitle());
        responseDto.setDescription(savedTodo.getDescription());
        responseDto.setCategory(savedTodo.getCategory().name());

        return responseDto;

    }

}
