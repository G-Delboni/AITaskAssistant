package com.app.aiassistant.service;

import com.app.aiassistant.dto.GoalRequestDTO;
import com.app.aiassistant.dto.GoalResponseDTO;
import com.app.aiassistant.entity.Activity;
import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.enums.GoalTargetUnit;
import com.app.aiassistant.exception.ResourceNotFoundException;
import com.app.aiassistant.repository.ActivityRepository;
import com.app.aiassistant.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GoalService {
    @Autowired
    private GoalRepository goalRepository;
    private ActivityRepository activityRepository;
    /*
    TODO TOMORROW;; -> Make the relations between Activities, Goals and Tasks.
     */

    public GoalService(GoalRepository goalRepository, ActivityRepository activityRepository) {
        this.goalRepository =  goalRepository;
        this.activityRepository =  activityRepository;
    }

    private Integer calculateCompletedValue(List<Activity> activities, GoalTargetUnit goalTargetUnit) {
        int completedValue = 0;
        switch (goalTargetUnit) {

            case MINUTES:
                return activities.stream()
                        .mapToInt(Activity::getDurationMinutes)
                        .sum();

            case HOURS:
                int totalMinutes = activities.stream()
                        .mapToInt(Activity::getDurationMinutes)
                        .sum();

                return totalMinutes / 60;

            case TIMES:
                return activities.size();

            case DAYS:
                Set<LocalDate> days = new HashSet<>();

                for (Activity activity : activities) {
                    days.add(activity.getDate());
                }

                return days.size();

            default:
                return 0;
        }
    }

    private double calculateCompletedPercentage(Integer completedValue, Goal goal){
        Double targetValue = goal.getTargetValue();
        if (targetValue == 0){
            return 0;
        }
        return targetValue * 100 / completedValue;
    }

    private GoalResponseDTO toGoalResponseDTO(Goal goal) {
        GoalTargetUnit goalTargetUnit = goal.getTargetUnit();
        List<Activity> activities = activityRepository.findbyGoalId(goal.getId());
        Integer completedValue =  calculateCompletedValue(activities, goalTargetUnit);
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
