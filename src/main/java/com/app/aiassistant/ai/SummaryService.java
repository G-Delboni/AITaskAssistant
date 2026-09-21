package com.app.aiassistant.ai;

import com.app.aiassistant.entity.Activity;
import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.enums.GoalStatus;
import com.app.aiassistant.repository.ActivityRepository;
import com.app.aiassistant.repository.GoalRepository;
import com.app.aiassistant.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
                List<Activity> weekActivities = activityRepository.findByPeriod(firstWeekDay, today);
                for (Activity activity : weekActivities){
                    totalMinutes += activity.getDurationMinutes();
                }
                return new SummaryData(period, weekActivities.size(), totalMinutes);
            case MONTHLY:
                LocalDate firstMonthDay = today.with(TemporalAdjusters.firstDayOfMonth());
                List<Activity> monthActivities = activityRepository.findByPeriod(firstMonthDay, today);
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
// TO FINISH LATER
//    public SummaryData generateGoalSummary(SummaryPeriod period){
//        int totalMinutes = 0;
//        LocalDate today =  LocalDate.now();
//        List<Goal> goals = goalRepository.findByPending(GoalStatus.PENDING);
//        for (Goal goal : goals){
//            totalMinutes += goal.get
//        }
//    }
}