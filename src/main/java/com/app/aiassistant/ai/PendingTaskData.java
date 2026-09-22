package com.app.aiassistant.ai;

import java.time.LocalDateTime;

public record PendingTaskData(
        Long id,
        String title,
        LocalDateTime dueAt
) {
}
