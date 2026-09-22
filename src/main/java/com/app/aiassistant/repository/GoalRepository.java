package com.app.aiassistant.repository;

import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.enums.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByStatus(GoalStatus goalStatus);
}
