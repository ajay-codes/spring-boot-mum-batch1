package com.example.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.dto.CreateTodoRequestDto;
import com.example.dto.CreateTodoResponseDto;
import com.example.dto.TodoResponseDto;
import com.example.dto.UpdateTodoRequestDto;
import com.example.service.TodoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todos", description = "Todo management endpoints")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Create a todo", description = "Creates a new todo and infers its category using Spring AI.")
    @ApiResponse(responseCode = "200", description = "Todo created successfully", content = @Content(schema = @Schema(implementation = CreateTodoResponseDto.class)))
    @PostMapping(consumes = "application/json", produces = "application/json")
    public CreateTodoResponseDto createTodo(@RequestBody CreateTodoRequestDto createTodoDto) {
        return todoService.createTodo(createTodoDto);
    }

    @Operation(summary = "List todos", description = "Returns all todos.")
    @ApiResponse(responseCode = "200", description = "Todos fetched successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TodoResponseDto.class))))
    @GetMapping(produces = "application/json")
    public List<TodoResponseDto> getAllTodos() {
        return todoService.getAllTodos();
    }

    @Operation(summary = "Get todo by id", description = "Returns a single todo by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todo found", content = @Content(schema = @Schema(implementation = TodoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Todo not found", content = @Content)
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public TodoResponseDto getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @Operation(summary = "Update a todo", description = "Updates a todo by id. If category is omitted and text changes, category is inferred again.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todo updated successfully", content = @Content(schema = @Schema(implementation = TodoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid category", content = @Content),
            @ApiResponse(responseCode = "404", description = "Todo not found", content = @Content)
    })
    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public TodoResponseDto updateTodo(@PathVariable Long id, @RequestBody UpdateTodoRequestDto updateTodoRequestDto) {
        return todoService.updateTodo(id, updateTodoRequestDto);
    }

    @Operation(summary = "Delete a todo", description = "Deletes a todo by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Todo deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Todo not found", content = @Content)
    })
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }

}
