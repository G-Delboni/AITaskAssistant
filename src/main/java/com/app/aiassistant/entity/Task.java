package com.app.aiassistant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private enum status;
    private LocalDate dueAt;
    private LocalDate completedAt;

    public Task(){
    }

    public Task(Long id, String title, String description, LocalDate dueAt, LocalDate completedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueAt = dueAt;
        this.completedAt = completedAt;
    }
}