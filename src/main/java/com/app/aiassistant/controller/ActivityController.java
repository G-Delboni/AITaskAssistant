package com.app.aiassistant.controller;

import com.app.aiassistant.entity.Activity;
import org.springframework.web.bind.annotation.*;
import com.app.aiassistant.service.ActivityService;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<Activity> findAll() {
        return activityService.findAll();
    }

    @GetMapping("/{id}")
    public Activity findById(@PathVariable Long id) {
        return activityService.findById(id);
    }

    @PostMapping
    public Activity insert(@RequestBody Activity activity) {
        return activityService.insert(activity);
    }

    @PutMapping("/{id}")
    public Activity update(@PathVariable Long id, @RequestBody Activity activity) {
        return activityService.update(id, activity);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        activityService.delete(id);
    }
}
