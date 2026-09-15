package com.app.aiassistant.service;

import com.app.aiassistant.entity.Activity;
import com.app.aiassistant.exception.ResourceNotFoundException;
import com.app.aiassistant.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> findAll() {
        List<Activity> activities = activityRepository.findAll();
        activities.sort(Comparator.comparing(Activity::getId));
        return activities;
    }

    public Activity findById(@PathVariable Long id) {
        return  activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found."));
    }

    public Activity insert(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity update(Long id, Activity oldActivityInfo) {
        Activity newActivityInfo = findById(id);
        newActivityInfo.setDescription(oldActivityInfo.getDescription());
        newActivityInfo.setDurationMinutes(oldActivityInfo.getDurationMinutes());
        newActivityInfo.setDate(oldActivityInfo.getDate());
        newActivityInfo.setTask(oldActivityInfo.getTask());
        newActivityInfo.setGoal(oldActivityInfo.getGoal());

        return activityRepository.save(newActivityInfo);
    }

    public void delete(Long id) {
        Activity activity = findById(id);
        activityRepository.delete(activity);
    }
}
