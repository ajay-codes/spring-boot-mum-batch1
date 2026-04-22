package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
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
        User user = getCurrentUserOrThrow();

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
        Authentication authentication = getAuthenticationOrThrow();

        List<Todo> todos;
        if (hasRole(authentication, "ROLE_ADMIN")) {
            todos = todoRepository.findAll();
        } else {
            User user = getCurrentUserOrThrow();
            todos = todoRepository.findByUserId(user.getId());
        }

        return todos.stream()
                .map(this::toTodoResponseDto)
                .toList();
    }

    public TodoResponseDto getTodoById(Long id) {
        Todo todo = findAccessibleTodoOrThrow(id);
        return toTodoResponseDto(todo);
    }

    public TodoResponseDto updateTodo(Long id, UpdateTodoRequestDto updateTodoRequestDto) {
        Todo todo = findAccessibleTodoOrThrow(id);

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
        Todo todo = findAccessibleTodoOrThrow(id);
        todoRepository.delete(todo);
    }

    private Todo findAccessibleTodoOrThrow(Long id) {
        Authentication authentication = getAuthenticationOrThrow();
        if (hasRole(authentication, "ROLE_ADMIN")) {
            return todoRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Todo not found with id: " + id));
        }

        User currentUser = getCurrentUserOrThrow();
        return todoRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Todo not found with id: " + id));
    }

    private User getCurrentUserOrThrow() {
        Authentication authentication = getAuthenticationOrThrow();
        return userService.getUserByUsername(authentication.getName());
    }

    private Authentication getAuthenticationOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return authentication;
    }

    private boolean hasRole(Authentication authentication, String roleName) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(roleName::equals);
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
