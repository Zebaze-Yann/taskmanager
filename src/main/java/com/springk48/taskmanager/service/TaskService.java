package com.springk48.taskmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springk48.taskmanager.exception.TaskNotFoundException;
import com.springk48.taskmanager.model.Status;
import com.springk48.taskmanager.model.Task;
import com.springk48.taskmanager.repository.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("tache introuvable avec l'id:" + id));
        return task;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long id, String newTitle, Status newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("tache introuvable avec l'id:" + id));

        if (newTitle != null && !newTitle.isEmpty()) {
            task.setTitle(newTitle);
        }

        if (newStatus != null) {
            task.setStatus(newStatus);
        }
        return task;
    }

    public void removeTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("tache introuvable avec l'id:" + id);
        }
        taskRepository.deleteById(id);
    }

}
