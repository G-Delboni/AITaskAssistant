package com.app.aiassistant.dto;

import com.app.aiassistant.enums.TaskStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Double durationMinutes;
    private Double completedValue;
    private Double completedPercentage;
    private TaskStatus status;
    private LocalDateTime dueAt;
    private LocalDateTime completedAt;

    public TaskResponseDTO(Long id, String title, String description, Double durationMinutes, Double completedValue, Double completedPercentage, TaskStatus status, LocalDateTime dueAt, LocalDateTime completedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.completedValue = completedValue;
        this.completedPercentage = completedPercentage;
        this.status = status;
        this.dueAt = dueAt;
        this.completedAt = completedAt;
    }
}
