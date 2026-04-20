package com.example.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CreateTodoDto;
import com.example.exception.TodoNotFoundException;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Todo {
    private String id;
    private String title;
    private boolean completed;
}

@Repository
class TodoRepository {
    private List<Todo> todos = new ArrayList<>();

    @PostConstruct
    public void init() {
        todos.add(new Todo("1", "Buy groceries", false));
        todos.add(new Todo("2", "Walk the dog", true));
        todos.add(new Todo("3", "Read a book", false));
    }

    public List<Todo> findAll() {
        return todos;
    }

    public void save(Todo todo) {
        todos.add(todo);
    }

    public void deleteById(String id) {
        todos.removeIf(todo -> todo.getId().equals(id));
    }

    public Todo findById(String id) {
        return todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void toggleCompleted(String id) {
        Todo todo = findById(id);
        if (todo != null) {
            todo.setCompleted(!todo.isCompleted());
        }
    }

    public void editTitle(String id, String newTitle) {
        Todo todo = findById(id);
        if (todo != null) {
            todo.setTitle(newTitle);
        }
    }

    public void clearCompleted() {
        todos.removeIf(Todo::isCompleted);
    }

    public void toggleAll(boolean completed) {
        todos.forEach(todo -> todo.setCompleted(completed));
    }
}

// @Controller
@RestController
@RequiredArgsConstructor
public class TodosController {

    private final TodoRepository todoRepository;

    // --------------------------------
    // Read operations
    // --------------------------------

    @RequestMapping(method = RequestMethod.GET, value = "/todos", produces = "application/json")
    public List<Todo> getTodosByStatus(@RequestParam(required = false) String status) {
        if (status == null || status.isEmpty()) {
            status = "all";
        }
        List<Todo> todos = todoRepository.findAll();
        if ("active".equalsIgnoreCase(status)) {
            return todos.stream().filter(todo -> !todo.isCompleted()).toList();
        } else if ("completed".equalsIgnoreCase(status)) {
            return todos.stream().filter(Todo::isCompleted).toList();
        } else {
            return todos;
        }
    }

    // GET /todos/{id}
    @RequestMapping(method = RequestMethod.GET, value = "/todos/{id}", produces = "application/json")
    public ResponseEntity<Todo> getTodoById(@PathVariable String id) {
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            return ResponseEntity.ok(todo);
        } else {
            throw new TodoNotFoundException(id);
        }
    }

    // HEAD /todos/{id}
    @RequestMapping(method = RequestMethod.HEAD, value = "/todos/{id}")
    public ResponseEntity<Void> headTodoById(@PathVariable String id) {
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            return ResponseEntity.ok().build();
        } else {
            throw new TodoNotFoundException(id);
        }
    }

    // --------------------------------
    // Write operations
    // --------------------------------

    @RequestMapping(method = RequestMethod.POST, value = "/todos", consumes = "application/json")
    public ResponseEntity<Void> createTodo(@RequestBody CreateTodoDto createTodoDto) {
        String newId = String.valueOf(todoRepository.findAll().size() + 1);
        Todo newTodo = new Todo(newId, createTodoDto.getTitle(), false);
        todoRepository.save(newTodo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/todos/" + newId)
                .build();
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/todos/{id}/toggle")
    public ResponseEntity<Void> toggleTodoCompleted(@PathVariable String id) {
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            todoRepository.toggleCompleted(id);
            return ResponseEntity.ok().build();
        } else {
            throw new TodoNotFoundException(id);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/todos/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable String id) {
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            todoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new TodoNotFoundException(id);
        }
    }

}
