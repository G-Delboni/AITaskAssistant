package com.app.aiassistant.dto;


import com.app.aiassistant.enums.GoalPeriod;
import com.app.aiassistant.enums.GoalTargetUnit;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GoalRequestDTO {
    private String title;
    private String description;
    private GoalPeriod period;
    private Double targetValue;
    private GoalTargetUnit targetUnit;
}
