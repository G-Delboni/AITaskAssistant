package com.app.aiassistant.ai;

import com.app.aiassistant.entity.Activity;
import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.entity.Task;
import com.app.aiassistant.enums.GoalPeriod;
import com.app.aiassistant.enums.GoalStatus;
import com.app.aiassistant.enums.TaskStatus;
import com.app.aiassistant.repository.ActivityRepository;
import com.app.aiassistant.repository.GoalRepository;
import com.app.aiassistant.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class SummaryService {
    private final ActivityRepository activityRepository;
    private final TaskRepository taskRepository;
    private final GoalRepository goalRepository;

    public SummaryService(
            ActivityRepository activityRepository,
            TaskRepository taskRepository,
            GoalRepository goalRepository) {

        this.activityRepository = activityRepository;
        this.taskRepository = taskRepository;
        this.goalRepository = goalRepository;
    }

    public SummaryData generateActivitySummary(SummaryPeriod period){
        int totalMinutes = 0;
        LocalDate today =  LocalDate.now();
        switch (period){
            case DAILY:
                List<Activity> todayActivities = activityRepository.findByDate(today);
                for (Activity activity : todayActivities){
                    totalMinutes += activity.getDurationMinutes();
                }
                return new SummaryData(period, todayActivities.size(), totalMinutes);
            case WEEKLY:
                LocalDate firstWeekDay = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                List<Activity> weekActivities = activityRepository.findByDateBetween(firstWeekDay, today);
                for (Activity activity : weekActivities){
                    totalMinutes += activity.getDurationMinutes();
                }
                return new SummaryData(period, weekActivities.size(), totalMinutes);
            case MONTHLY:
                LocalDate firstMonthDay = today.with(TemporalAdjusters.firstDayOfMonth());
                List<Activity> monthActivities = activityRepository.findByDateBetween(firstMonthDay, today);
                for (Activity activity : monthActivities){
                    totalMinutes += activity.getDurationMinutes();
                }
                return new SummaryData(period, monthActivities.size(), totalMinutes);
            default:
                throw new IllegalArgumentException(
                        "Unsupported summary period."
                );
        }
    }

    private List<Task> findPendingTasks(SummaryPeriod period) {
        LocalDateTime today = LocalDateTime.now();
        LocalDate currentDate = today.toLocalDate();

        LocalDateTime start;
        LocalDateTime end;
            switch(period){
                case DAILY:
                    start = currentDate.atStartOfDay();
                    end = today.plusDays(1);
                    return taskRepository
                    .findByStatus(TaskStatus.PENDING, TaskStatus.RUNNING)
                        .stream()
                        .filter(task ->
                                !task.getDueAt().isBefore(start) &&
                                        task.getDueAt().isBefore(end)
                        )
                        .toList();
                case WEEKLY:
                    start = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                            .toLocalDate()
                            .atStartOfDay();
                    end = today.plusDays(1);
                    return taskRepository.findByStatus(TaskStatus.PENDING, TaskStatus.RUNNING)
                            .stream()
                            .filter(task ->
                                    !task.getDueAt().isBefore(start) &&
                                    task.getDueAt().isBefore(end)
                            )
                            .toList();
                case MONTHLY:
                    start = today.with(TemporalAdjusters.firstDayOfMonth())
                            .toLocalDate()
                            .atStartOfDay();
                    end = today.plusDays(1);
                    return taskRepository.findByStatus(TaskStatus.PENDING, TaskStatus.RUNNING)
                            .stream()
                            .filter(task ->
                                    !task.getDueAt().isBefore(start) &&
                                    task.getDueAt().isBefore(end)
                            )
                            .toList();
                default:
                    throw new IllegalArgumentException(
                            "Unsupported summary period."
                    );
            }
    }

    private List<Goal> findPendingGoals(SummaryPeriod period) {
        switch (period) {
            case DAILY:
                return goalRepository
                        .findByStatus(GoalStatus.PENDING)
                        .stream()
                        .filter(goal -> goal.getPeriod() == GoalPeriod.DAILY)
                        .toList();

            case WEEKLY:
                return goalRepository
                        .findByStatus(GoalStatus.PENDING)
                        .stream()
                        .filter(goal -> goal.getPeriod() == GoalPeriod.WEEKLY)
                        .toList();

            case MONTHLY:
                return goalRepository
                        .findByStatus(GoalStatus.PENDING)
                        .stream()
                        .filter(goal ->
                                goal.getPeriod() == GoalPeriod.DAILY ||
                                        goal.getPeriod() == GoalPeriod.WEEKLY)
                        .toList();

            default:
                throw new IllegalArgumentException(
                        "Unsupported summary period."
                );
        }
    }

    private List<PendingTaskData> extractPendingTaskData(List<Task> tasks) {
        return tasks.stream()
                .map(task -> new PendingTaskData(
                        task.getId(),
                        task.getTitle(),
                        task.getDueAt()
                ))
                .toList();
    }

    private List<PendingGoalData> extractPendingGoalData(List<Goal> goals) {
        return goals.stream()
                .map(goal -> new PendingGoalData(
                        goal.getId(),
                        goal.getTitle(),
                        goal.getTargetValue(),
                        goal.getTargetUnit(),
                        goal.getStatus()
                ))
                .toList();
    }

    public PendingSummary generatePendingSummary(SummaryPeriod period){
        List<Task> pendingTasks = findPendingTasks(period);
        List<Goal> pendingGoals = findPendingGoals(period);
        List<PendingTaskData> pendingTaskData = extractPendingTaskData(pendingTasks);
        List<PendingGoalData> pendingGoalData = extractPendingGoalData(pendingGoals);

        return new PendingSummary(pendingTaskData, pendingGoalData);
    }


}