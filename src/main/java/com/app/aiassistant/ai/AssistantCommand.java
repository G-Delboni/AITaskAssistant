package com.app.aiassistant.ai;

public record AssistantCommand(
        AssistantIntent intent,
        String title,
        String description,
        Integer durationMinutes,
        Long taskId,
        Long goalId,
        SummaryType summaryType,
        SummaryPeriod period
) {}
