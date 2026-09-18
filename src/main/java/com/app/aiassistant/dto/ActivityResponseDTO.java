package com.app.aiassistant.dto;

import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.entity.Task;

import java.time.LocalDate;

public class ActivityResponseDTO {
    private Long id;
    private String description;
    private Integer durationMinutes;
    private LocalDate date;
    private Long goalID;
    private Long taskID;

    public ActivityResponseDTO(Long id, String description, Integer durationMinutes, LocalDate date, Long taskID, Long goalID) {
        this.id = id;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.date = date;
        this.taskID = taskID;
        this.goalID = goalID;
    }
}
