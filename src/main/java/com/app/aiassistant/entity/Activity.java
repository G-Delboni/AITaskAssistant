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

    public Activity(Long id, String description, Integer durationMinutes, LocalDate date, Task task, Goal goal) {
        if(date.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Invalid date");
        }
        this.id = id;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.date = date;
        this.task = task;
        this.goal = goal;
    }
}
