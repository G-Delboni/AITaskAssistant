package com.app.aiassistant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name = "tb_activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Integer durationMinutes;
    private LocalDate date;
    private Task task;
    private Goal goal;

    public Activity(){
    }

    public Activity(Long id, String description, Integer durationMinutes, LocalDate date, Task task, Goal goal) {
        this.id = id;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.date = date;
        this.task = task;
        this.goal = goal;
    }
}
