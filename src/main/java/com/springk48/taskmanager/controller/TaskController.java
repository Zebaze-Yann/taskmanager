package com.springk48.taskmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springk48.taskmanager.model.Status;
import com.springk48.taskmanager.model.Task;
import com.springk48.taskmanager.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks(@RequestParam(required = false) Status status) {
        if (status != null) {
            return taskService.getTasksByStatus(status);
        }

        return taskService.getTasks();
    }

    @Operation(summary = "récupérer une tache")
    @ApiResponse(responseCode = "200", description = "tache trouvée")
    @ApiResponse(responseCode = "404", description = "tache introuvable", content = @Content (schema = @Schema (type="string")))
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }
 
    @Operation(summary = "créer une tache")
    @ApiResponse(responseCode = "201",description = "tache créée avec succès")
    @ApiResponse (responseCode = "400",description = "titre manquant", content =@Content(schema = @Schema (type="string")))
    @PostMapping
    public ResponseEntity<Task> saveTask(@Valid @RequestBody Task task) {
        Task newTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{id}")
    public Task updateDetailsTask(@PathVariable Long id, @RequestParam(required = false) String title,
            @RequestParam(required = false) Status status) {

        return taskService.updateTask(id, title, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.removeTask(id);

        return ResponseEntity.noContent().build();
    }

}
