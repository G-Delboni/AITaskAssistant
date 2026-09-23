package com.app.aiassistant.ai;

import com.app.aiassistant.enums.GoalPeriod;
import com.app.aiassistant.enums.GoalTargetUnit;

import java.time.LocalDate;

public record AssistantCommand(
        AssistantIntent intent,
        String title,
        String description,
        Integer durationMinutes,
        String dueAt,
        Long taskId,
        Long goalId,
        GoalPeriod goalPeriod,
        LocalDate localDate,
        Double targetValue,
        GoalTargetUnit targetUnit,
        SummaryType summaryType,
        SummaryPeriod summaryPeriod
) {}
