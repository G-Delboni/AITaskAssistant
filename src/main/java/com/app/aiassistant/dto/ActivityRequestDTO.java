package com.app.aiassistant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActivityRequestDTO {
    private String description;
    private Integer durationMinutes;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private Long goalID;
    private Long taskID;
}
