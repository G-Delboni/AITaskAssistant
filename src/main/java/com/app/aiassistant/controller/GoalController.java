package com.app.aiassistant.controller;

import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.service.GoalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {
    private final GoalService goalService;

    public  GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public List<Goal> findAll() {
        return goalService.findAll();
    }

    @GetMapping("/{id}")
    public Goal findById(@PathVariable Long id) {
        return goalService.findById(id);
    }

    @PostMapping
    public Goal insert(@RequestBody Goal goal) {
        return goalService.insert(goal);
    }

    @PutMapping("/{id}")
    public Goal update(@PathVariable Long id, @RequestBody Goal goal) {
        return goalService.update(id, goal);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        goalService.delete(id);
    }
}
