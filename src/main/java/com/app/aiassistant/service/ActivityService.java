package com.app.aiassistant.service;

import com.app.aiassistant.dto.ActivityRequestDTO;
import com.app.aiassistant.dto.ActivityResponseDTO;
import com.app.aiassistant.entity.Activity;
import com.app.aiassistant.exception.ResourceNotFoundException;
import com.app.aiassistant.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    private ActivityResponseDTO toActivityDTO(Activity activity) {
        return new  ActivityResponseDTO(
                activity.getId(),
                activity.getDescription(),
                activity.getDurationMinutes(),
                activity.getDate(),
                activity.extractGoalId(),
                activity.extractTaskId()
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

    public ActivityResponseDTO insert(ActivityRequestDTO activityRequestDTO) {
        Activity activity = new Activity(
                activityRequestDTO.getDescription(),
                activityRequestDTO.getDurationMinutes(),
                activityRequestDTO.getDate()
        );
        Activity savedActivity = activityRepository.save(activity);
        return toActivityDTO(savedActivity);
    }

    public ActivityResponseDTO update(Long id, ActivityRequestDTO activityRequestDTO) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException("Activity not found."));
        activity.updateActivity(activityRequestDTO.getDescription(),
                activityRequestDTO.getDurationMinutes(),
                activityRequestDTO.getDate());

        return  toActivityDTO(activityRepository.save(activity));
    }

    public void delete(Long id) {
        if(!activityRepository.existsById(id)) {
            throw new  ResourceNotFoundException("Activity not found.");
        }
        activityRepository.deleteById(id);
    }
}
