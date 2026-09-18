package com.app.aiassistant.controller;

import com.app.aiassistant.dto.ActivityRequestDTO;
import com.app.aiassistant.dto.ActivityResponseDTO;
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
    public List<ActivityResponseDTO> findAll() {
        return activityService.findAll();
    }

    @GetMapping("/{id}")
    public ActivityResponseDTO findById(@PathVariable Long id) {
        return activityService.findById(id);
    }

    @PostMapping
    public ActivityResponseDTO insert(@RequestBody ActivityRequestDTO activityRequestDTO) {
        return activityService.insert(activityRequestDTO);
    }

    @PutMapping("/{id}")
    public ActivityResponseDTO update(@PathVariable Long id, @RequestBody ActivityRequestDTO activityRequestDTO) {
        return activityService.update(id, activityRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        activityService.delete(id);
    }
}
