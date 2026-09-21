package com.app.aiassistant.dto;

import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.entity.Task;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ActivityResponseDTO {
    private Long id;
    private String description;
    private Integer durationMinutes;
    private LocalDate date;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long goalID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
