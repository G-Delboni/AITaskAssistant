package com.app.aiassistant.ai;

import com.app.aiassistant.enums.GoalTargetUnit;

public record PendingGoalData(
        Long id,
        String title,
        Double targetValue,
        GoalTargetUnit targetUnit,
        com.app.aiassistant.enums.GoalStatus completedPercentage
) {
}
