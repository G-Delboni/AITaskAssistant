package com.app.aiassistant.service;

import com.app.aiassistant.dto.TaskRequestDTO;
import com.app.aiassistant.dto.TaskResponseDTO;
import com.app.aiassistant.entity.Task;
import com.app.aiassistant.exception.InvalidDateException;
import com.app.aiassistant.exception.ResourceNotFoundException;
import com.app.aiassistant.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponseDTO> findAll(){
        return taskRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private TaskResponseDTO toResponseDTO(Task task) {

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueAt(),
                task.getCompletedAt()
        );
    }

    public TaskResponseDTO findById(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));

        return toResponseDTO(task);
    }

    private LocalDateTime parseDate(String dateString) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss")
                        .withResolverStyle(ResolverStyle.STRICT);

        try {
            return LocalDateTime.parse(dateString, formatter);

        } catch (DateTimeParseException e) {
            throw new InvalidDateException(
                    "Invalid date: " + dateString
            );
        }
    }

    public TaskResponseDTO insert(TaskRequestDTO dto) {

        LocalDateTime dueAt = parseDate(dto.getDueAt());

        Task task = new Task(
                dto.getTitle(),
                dto.getDescription(),
                dueAt
        );

        Task savedTask = taskRepository.save(task);

        return toResponseDTO(savedTask);
    }

    public TaskResponseDTO update(
            Long id,
            TaskRequestDTO dto) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));

        LocalDateTime dueAt = parseDate(dto.getDueAt());

        task.updateTask(
                dto.getTitle(),
                dto.getDescription(),
                dueAt
        );

        return toResponseDTO(taskRepository.save(task));
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found.");
        }

        taskRepository.deleteById(id);
    }
}
