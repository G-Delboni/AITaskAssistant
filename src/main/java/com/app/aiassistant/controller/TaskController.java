package com.app.aiassistant.controller;

import com.app.aiassistant.entity.Task;
import com.app.aiassistant.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    public Task insert(@RequestBody Task task) {
        return taskService.insert(task);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.update(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }
}
