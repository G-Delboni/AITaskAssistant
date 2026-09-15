package com.app.aiassistant.entity;

import com.app.aiassistant.enums.TaskStatus;
import com.app.aiassistant.exception.InvalidDateException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private TaskStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dueAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime completedAt;

    public Task(Long id, String title, String description, LocalDateTime dueAt, LocalDateTime completedAt) {
        if (dueAt.isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("This task cannot be done before today.");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueAt = dueAt;
        this.completedAt = completedAt;
    }
}