package com.app.aiassistant.controller;

import com.app.aiassistant.dto.TaskRequestDTO;
import com.app.aiassistant.dto.TaskResponseDTO;
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
    public List<TaskResponseDTO> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public TaskResponseDTO findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    public TaskResponseDTO insert(
            @RequestBody TaskRequestDTO dto) {

        return taskService.insert(dto);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO update(
            @PathVariable Long id,
            @RequestBody TaskRequestDTO dto) {

        return taskService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }
}
