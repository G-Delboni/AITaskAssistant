package com.app.aiassistant.entity;

import com.app.aiassistant.enums.TaskStatus;
import com.app.aiassistant.exception.InvalidDateException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Double durationMinutes;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dueAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime completedAt;

    public Task(String title, String description, Double durationMinutes, LocalDateTime dueAt) {
        if (dueAt.isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("This task cannot be done before today.");
        }
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.dueAt = dueAt;
        this.status = TaskStatus.PENDING;
    }

    public void completeTask(){
        this.status = TaskStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void cancelTask() {
        this.status = TaskStatus.CANCELED;
    }

    public void pendingTask() {
        this.status = TaskStatus.PENDING;
        this.completedAt = null;
    }

    public void runTask() {
        this.status = TaskStatus.RUNNING;
        this.completedAt = null;
    }

    public void updateTask(
            String title,
            String description,
            Double durationMinutes,
            LocalDateTime dueAt) {

        if (dueAt.isBefore(LocalDateTime.now())) {
            throw new InvalidDateException(
                    "This task cannot be scheduled in the past."
            );
        }

        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.dueAt = dueAt;
    }
}