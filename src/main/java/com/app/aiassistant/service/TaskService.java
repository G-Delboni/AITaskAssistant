package com.app.aiassistant.service;

import com.app.aiassistant.dto.TaskRequestDTO;
import com.app.aiassistant.dto.TaskResponseDTO;
import com.app.aiassistant.entity.Activity;
import com.app.aiassistant.entity.Task;
import com.app.aiassistant.enums.TaskStatus;
import com.app.aiassistant.exception.InvalidDateException;
import com.app.aiassistant.exception.ResourceNotFoundException;
import com.app.aiassistant.repository.ActivityRepository;
import com.app.aiassistant.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ActivityRepository activityRepository;

    public TaskService(TaskRepository taskRepository, ActivityRepository activityRepository) {
        this.taskRepository = taskRepository;
        this.activityRepository = activityRepository;
    }

    public List<TaskResponseDTO> findAll(){
        return taskRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private TaskResponseDTO toResponseDTO(Task task) {
        List<Activity> activities = activityRepository.findByTaskId(task.getId());
        double completedValue = calculateTaskProgressValue(activities, task);
        double completedPercentage = calculateTaskProgressPercentage(completedValue, task);

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDurationMinutes(),
                completedValue,
                completedPercentage,
                task.getStatus(),
                task.getDueAt(),
                task.getCompletedAt()
        );
    }
    private double calculateTaskProgressPercentage(Double completedValue, Task task) {
        Double targetValue = task.getDurationMinutes();
        if (targetValue == 0){
            return 0;
        }
        return completedValue * 100 / targetValue;
    }

    private double calculateTaskProgressValue(List<Activity> activities, Task task){
        double targetValue = task.getDurationMinutes();
        double completedValue = activities.stream()
                .mapToDouble(Activity::getDurationMinutes)
                .sum();
        return Math.min(completedValue, targetValue);
    }

    public TaskResponseDTO cancelTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.cancelTask();
        taskRepository.save(task);
        return toResponseDTO(task);
    }

    public void updateTaskProgress(Long taskID) {
        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        List<Activity>activities = activityRepository.findByTaskId(taskID);

        double completedValue = calculateTaskProgressValue(activities, task);

        if (task.getStatus() == TaskStatus.CANCELED) {
            return;
        }

        if (completedValue >= task.getDurationMinutes()) {
            task.completeTask();
        }
        else if (completedValue > 0) {
            task.runTask();
        }
        else {
            task.pendingTask();
        }

        taskRepository.save(task);

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
                    "Invalid date: " + dateString + " | " + e.getMessage()
            );
        }
    }

    public TaskResponseDTO insert(TaskRequestDTO dto) {

        LocalDateTime dueAt = parseDate(dto.getDueAt());

        Task task = new Task(
                dto.getTitle(),
                dto.getDescription(),
                dto.getDurationMinutes(),
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
                dto.getDurationMinutes(),
                dueAt
        );
        updateTaskProgress(task.getId());
        return toResponseDTO(taskRepository.save(task));
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found.");
        }

        taskRepository.deleteById(id);
    }
}
