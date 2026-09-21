package com.app.aiassistant.repository;

import com.app.aiassistant.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findByGoalId(Long goalId);
    List<Activity> findByTaskId(Long taskId);
    List<Activity> findByDate(LocalDate date);
    List<Activity> findByPeriod(LocalDate initialDate, LocalDate finalDate);
}
