package com.app.aiassistant.entity;

import com.app.aiassistant.exception.InvalidDateException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name = "tb_activity")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Integer durationMinutes;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private Task task;
    private Goal goal;

    public Activity(String description, Integer durationMinutes, LocalDate date) {
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.date = date;
    }

    public Long extractTaskId() {
        return task.getId();
    }

    public Long extractGoalId() {
        return goal.getId();
    }

    public void updateActivity(String description, Integer durationMinutes, LocalDate date) {
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.date = date;
    }
}
