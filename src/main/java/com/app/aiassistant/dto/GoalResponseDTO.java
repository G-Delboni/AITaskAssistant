package com.app.aiassistant.dto;

import com.app.aiassistant.enums.GoalPeriod;
import com.app.aiassistant.enums.GoalStatus;
import com.app.aiassistant.enums.GoalTargetUnit;


public class GoalResponseDTO {
    private Long id;
    private String title;
    private String description;
    private GoalPeriod period;
    private Double targetValue;
    private GoalTargetUnit targetUnit;
    private Integer completedValue;
    private Double completedPercentage;
    private GoalStatus status;

    public GoalResponseDTO(Long id, String title, String description, GoalPeriod period, Double targetValue, GoalTargetUnit targetUnit, Integer completedValue, Double completedPercentage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.period = period;
        this.targetValue = targetValue;
        this.targetUnit = targetUnit;
        this.completedValue = completedValue;
        this.completedPercentage = completedPercentage;
        if (completedPercentage == 100) {
            this.status = GoalStatus.COMPLETED;
        }
        else {
            this.status = GoalStatus.PENDING;
        }
    }
}
