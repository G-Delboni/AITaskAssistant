package com.app.aiassistant.service;

import com.app.aiassistant.dto.GoalRequestDTO;
import com.app.aiassistant.dto.GoalResponseDTO;
import com.app.aiassistant.entity.Activity;
import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.enums.GoalTargetUnit;
import com.app.aiassistant.exception.ResourceNotFoundException;
import com.app.aiassistant.repository.ActivityRepository;
import com.app.aiassistant.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final ActivityRepository activityRepository;
    /*
    TODO TOMORROW;; -> Make the relations between Activities, Goals and Tasks.
     */

    public GoalService(GoalRepository goalRepository, ActivityRepository activityRepository) {
        this.goalRepository =  goalRepository;
        this.activityRepository =  activityRepository;
    }

    public Double calculateCompletedValue(List<Activity> activities, GoalTargetUnit goalTargetUnit) {
        double completedValue = 0;
        switch (goalTargetUnit) {

            case MINUTES:
                return activities.stream()
                        .mapToDouble(Activity::getDurationMinutes)
                        .sum();

            case HOURS:
                double totalMinutes = activities.stream()
                        .mapToDouble(Activity::getDurationMinutes)
                        .sum();

                return totalMinutes / 60;

            case TIMES:
                return (double) activities.size();

            case DAYS:
                Set<LocalDate> days = new HashSet<>();

                for (Activity activity : activities) {
                    days.add(activity.getDate());
                }

                return (double) days.size();

            default:
                return completedValue;
        }
    }

    public double calculateCompletedPercentage(Double completedValue, Goal goal){
        Double targetValue = goal.getTargetValue();
        if (targetValue == 0){
            return 0;
        }
        return completedValue * 100 / targetValue;
    }

    public void updateGoalProgress(Long goalId) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found."));

        List<Activity> activities =
                activityRepository.findByGoalId(goalId);

        Double completedValue =
                calculateCompletedValue(
                        activities,
                        goal.getTargetUnit());

        if (completedValue >= goal.getTargetValue()) {
            goal.completeGoal();
        }

        goalRepository.save(goal);
    }

    private GoalResponseDTO toGoalResponseDTO(Goal goal) {
        GoalTargetUnit goalTargetUnit = goal.getTargetUnit();
        List<Activity> activities = activityRepository.findByGoalId(goal.getId());
        Double completedValue =  calculateCompletedValue(activities, goalTargetUnit);
        double completedPercentage =  calculateCompletedPercentage(completedValue, goal);

        return new GoalResponseDTO(
                goal.getId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getPeriod(),
                goal.getTargetValue(),
                goal.getTargetUnit(),
                completedValue,
                completedPercentage
        );
    }

    public List<GoalResponseDTO> findAll() {
        return goalRepository.findAll()
                .stream()
                .map(this::toGoalResponseDTO)
                .toList();
    }

    public GoalResponseDTO findById(Long id){
        Goal goal = goalRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Goal not found."));
        return toGoalResponseDTO(goal);
    }

    public GoalResponseDTO insert(GoalRequestDTO goalRequestDTO){
        Goal goal = new Goal(
                goalRequestDTO.getTitle(),
                goalRequestDTO.getDescription(),
                goalRequestDTO.getPeriod(),
                goalRequestDTO.getTargetValue(),
                goalRequestDTO.getTargetUnit()
        );
        Goal savedGoal = goalRepository.save(goal);
        return toGoalResponseDTO(savedGoal);
    }

    public GoalResponseDTO update(Long id, GoalRequestDTO goalRequestDTO){
        Goal goal = goalRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Goal not found."));
        goal.updateGoal(
                goalRequestDTO.getTitle(),
                goalRequestDTO.getDescription(),
                goalRequestDTO.getPeriod(),
                goalRequestDTO.getTargetValue(),
                goalRequestDTO.getTargetUnit()
        );

        return toGoalResponseDTO(goalRepository.save(goal));
    }

    public void delete(Long id){
        if (!goalRepository.existsById(id)){
            throw new ResourceNotFoundException("Goal not found.");
        }
        goalRepository.deleteById(id);
    }
}
