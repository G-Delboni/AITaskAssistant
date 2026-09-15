package com.app.aiassistant.service;

import com.app.aiassistant.entity.Goal;
import com.app.aiassistant.exception.ResourceNotFoundException;
import com.app.aiassistant.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class GoalService {
    @Autowired
    private GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository =  goalRepository;
    }

    public List<Goal> findAll() {
        return goalRepository.findAll();
    }

    public Goal findById(@PathVariable Long id){
        return goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found."));
    }

    public Goal insert(Goal goal){
        return goalRepository.save(goal);
    }

    public Goal update(Long id, Goal oldGoalInfo){
        Goal newGoalInfo = findById(id);
        newGoalInfo.setTitle(oldGoalInfo.getTitle());
        newGoalInfo.setDescription(oldGoalInfo.getDescription());
        newGoalInfo.setPeriod(oldGoalInfo.getPeriod());
        newGoalInfo.setTargetUnit(oldGoalInfo.getTargetUnit());
        newGoalInfo.setTargetValue(oldGoalInfo.getTargetValue());

        return goalRepository.save(newGoalInfo);
    }

    public void delete(Long id){
        Goal goal = findById(id);
        goalRepository.delete(goal);
    }
}
