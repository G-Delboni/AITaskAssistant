package com.app.aiassistant.ai;

public record AssistantCommand (
        AssistantIntent intent,
        String title,
        String description,
        SummaryPeriod summaryPeriod,
        Integer durationMinutes,
        Long taskId,
        Long GoalId
){
}
