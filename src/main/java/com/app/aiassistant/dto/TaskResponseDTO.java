package com.app.aiassistant.dto;

import com.app.aiassistant.enums.TaskStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueAt;
    private LocalDateTime completedAt;

    public TaskResponseDTO(Long id, String title, String description, TaskStatus status, LocalDateTime completedAt, LocalDateTime dueAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.completedAt = completedAt;
        this.dueAt = dueAt;
    }
}
