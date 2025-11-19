package com.example.todo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.EmptyResultDataAccessException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {
    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Task> listAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        task.setId(null); // Ignore client-sent id
        Task saved = repository.save(task);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public Task update(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates // Partial update
    ) {
        Task task = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (updates.containsKey("title")) {
            Object title = updates.get("title");
            if (!(title instanceof String) || ((String) title).isBlank())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required if specified");
            task.setTitle((String) title);
        }
        if (updates.containsKey("description")) {
            Object desc = updates.get("description");
            if (desc != null && !(desc instanceof String))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description must be a string");
            task.setDescription((String) desc);
        }
        if (updates.containsKey("done")) {
            Object doneVal = updates.get("done");
            if (!(doneVal instanceof Boolean))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Done must be boolean");
            task.setDone((Boolean) doneVal);
        }
        return repository.save(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        // Only reveal safe field error messages
        String errMsg = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getDefaultMessage()).findFirst().orElse("Validation failed");
        return ResponseEntity.badRequest().body(Map.of("error", errMsg));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleStatus(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        // Hide details for privacy
        return ResponseEntity.status(500).body(Map.of("error", "An internal error occurred"));
    }
}
