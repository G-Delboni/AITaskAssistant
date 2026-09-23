package com.app.aiassistant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequestDTO {
    private String title;
    private String description;
    private Double durationMinutes;
    private LocalDateTime dueAt;
}
