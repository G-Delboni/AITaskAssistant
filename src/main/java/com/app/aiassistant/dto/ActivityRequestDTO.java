package com.app.aiassistant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActivityRequestDTO {
    private String description;
    private Integer durationMinutes;
    private LocalDate date;
}
