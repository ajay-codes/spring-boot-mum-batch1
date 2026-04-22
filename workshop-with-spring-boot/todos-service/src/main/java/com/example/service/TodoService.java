package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import com.example.dto.CreateTodoRequestDto;
import com.example.dto.CreateTodoResponseDto;
import com.example.dto.TodoResponseDto;
import com.example.dto.UpdateTodoRequestDto;
import com.example.entity.Todo;
import com.example.entity.TodoCategory;
import com.example.entity.User;
import com.example.repository.TodoRepository;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;

    public TodoService(TodoRepository todoRepository, UserService userService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    public CreateTodoResponseDto createTodo(CreateTodoRequestDto createTodoDto) {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String username = authentication.getName();

        User user = userService.getUserByUsername(username);

        Todo todo = new Todo();
        todo.setTitle(createTodoDto.getTitle());
        todo.setDescription(createTodoDto.getDescription());
        todo.setCategory(TodoCategory.OTHER);
        todo.setUser(user);

        Todo savedTodo = todoRepository.save(todo);

        CreateTodoResponseDto responseDto = new CreateTodoResponseDto();
        responseDto.setId(savedTodo.getId());
        responseDto.setTitle(savedTodo.getTitle());
        responseDto.setDescription(savedTodo.getDescription());
        responseDto.setCategory(savedTodo.getCategory().name());

        return responseDto;

    }

    public List<TodoResponseDto> getAllTodos() {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        return todoRepository.findByUserId(user.getId()).stream()
                .map(this::toTodoResponseDto)
                .toList();
    }

    public TodoResponseDto getTodoById(Long id) {
        Todo todo = findByIdOrThrow(id);
        return toTodoResponseDto(todo);
    }

    public TodoResponseDto updateTodo(Long id, UpdateTodoRequestDto updateTodoRequestDto) {
        Todo todo = findByIdOrThrow(id);

        if (updateTodoRequestDto.getTitle() != null) {
            todo.setTitle(updateTodoRequestDto.getTitle());
        }
        if (updateTodoRequestDto.getDescription() != null) {
            todo.setDescription(updateTodoRequestDto.getDescription());
        }
        if (updateTodoRequestDto.getCompleted() != null) {
            todo.setCompleted(updateTodoRequestDto.getCompleted());
        }

        if (StringUtils.hasText(updateTodoRequestDto.getCategory())) {
            todo.setCategory(parseCategoryOrThrow(updateTodoRequestDto.getCategory()));
        } else if (updateTodoRequestDto.getTitle() != null || updateTodoRequestDto.getDescription() != null) {
            todo.setCategory(TodoCategory.OTHER);
        }

        Todo savedTodo = todoRepository.save(todo);
        return toTodoResponseDto(savedTodo);
    }

    public void deleteTodo(Long id) {
        Todo todo = findByIdOrThrow(id);
        todoRepository.delete(todo);
    }

    private Todo findByIdOrThrow(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Todo not found with id: " + id));
    }

    private TodoCategory parseCategoryOrThrow(String categoryText) {
        try {
            return TodoCategory.valueOf(categoryText.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid category. Allowed values: WORK, PERSONAL, ENTERTAINMENT, HEALTH, OTHER");
        }
    }

    private TodoResponseDto toTodoResponseDto(Todo todo) {
        TodoResponseDto responseDto = new TodoResponseDto();
        responseDto.setId(todo.getId());
        responseDto.setTitle(todo.getTitle());
        responseDto.setDescription(todo.getDescription());
        responseDto.setCategory(todo.getCategory() != null ? todo.getCategory().name() : null);
        responseDto.setCompleted(todo.isCompleted());
        return responseDto;
    }

}
