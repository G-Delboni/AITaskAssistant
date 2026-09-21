package com.app.aiassistant.service;

import com.app.aiassistant.dto.ActivityRequestDTO;
import com.app.aiassistant.dto.ActivityResponseDTO;
import com.app.aiassistant.entity.Activity;
import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.entity.Task;
import com.app.aiassistant.exception.ResourceNotFoundException;
import com.app.aiassistant.repository.ActivityRepository;
import com.app.aiassistant.repository.GoalRepository;
import com.app.aiassistant.service.GoalService;
import com.app.aiassistant.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final GoalService goalService;

    private Goal findGoal(Long goalId) {
        if (goalId == null) {
            return null;
        }

        return goalRepository.findById(goalId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found."));
    }

    private Task findTask(Long taskId) {
        if (taskId == null) {
            return null;
        }

        return taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));
    }

    public ActivityService(ActivityRepository activityRepository, GoalRepository goalRepository, TaskRepository taskRepository, TaskService taskService, GoalService goalService) {
        this.activityRepository = activityRepository;
        this.goalRepository = goalRepository;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.goalService = goalService;
    }

    private ActivityResponseDTO toActivityDTO(Activity activity) {
        return new  ActivityResponseDTO(
                activity.getId(),
                activity.getDescription(),
                activity.getDurationMinutes(),
                activity.getDate(),
                activity.hasTask() ? activity.extractTaskId() : null,
                activity.hasGoal() ? activity.extractGoalId() : null
        );
    }

    public List<ActivityResponseDTO> findAll() {
        return activityRepository.findAll()
                .stream()
                .map(this::toActivityDTO)
                .toList();
    }

    public ActivityResponseDTO findById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found."));
        return toActivityDTO(activity);
    }

    public void updateStatsBasedOnActivity(Goal goal, Task task){
        if (goal != null) {
            goalService.updateGoalProgress(goal.getId());
        }
        if (task != null) {
            taskService.updateTaskProgress(task.getId());
        }
    }

    public ActivityResponseDTO insert(ActivityRequestDTO activityRequestDTO) {
        Goal goal = findGoal(activityRequestDTO.getGoalID());
        Task task = findTask(activityRequestDTO.getTaskID());


        Activity activity = new Activity(
                activityRequestDTO.getDescription(),
                activityRequestDTO.getDurationMinutes(),
                activityRequestDTO.getDate()
        );

        activity.linkTask(task);
        activity.linkGoal(goal);

        Activity savedActivity = activityRepository.save(activity);
        updateStatsBasedOnActivity(goal, task);
        return toActivityDTO(savedActivity);
    }

    public ActivityResponseDTO update(Long id, ActivityRequestDTO activityRequestDTO) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException("Activity not found."));

        Goal oldGoal = activity.getGoal();
        Task oldTask = activity.getTask();

        Goal newGoal = findGoal(activityRequestDTO.getGoalID());
        Task newTask = findTask(activityRequestDTO.getTaskID());


        activity.updateActivity(activityRequestDTO.getDescription(),
                activityRequestDTO.getDurationMinutes(),
                activityRequestDTO.getDate()
        );

        activity.linkTask(newTask);
        activity.linkGoal(newGoal);
        Activity saved = activityRepository.save(activity);

        updateStatsBasedOnActivity(oldGoal, oldTask);
        updateStatsBasedOnActivity(newGoal, newTask);
        return  toActivityDTO(saved);
    }

    public void delete(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Activity not found."));
        Goal goal = activity.getGoal();
        Task task = activity.getTask();

        activityRepository.delete(activity);

        updateStatsBasedOnActivity(goal, task);
    }
}
