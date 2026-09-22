package com.app.aiassistant.ai;

import java.util.List;

public record PendingSummary(
        List<PendingTaskData> pendingTasks,
        List<PendingGoalData> pendingGoals
) {
}
