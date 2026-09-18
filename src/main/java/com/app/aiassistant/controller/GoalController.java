package com.app.aiassistant.controller;

import com.app.aiassistant.dto.GoalRequestDTO;
import com.app.aiassistant.dto.GoalResponseDTO;
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
    public List<GoalResponseDTO> findAll() {
        return goalService.findAll();
    }

    @GetMapping("/{id}")
    public GoalResponseDTO findById(@PathVariable Long id) {
        return goalService.findById(id);
    }

    @PostMapping
    public GoalResponseDTO insert(@RequestBody GoalRequestDTO goalRequestDTO) {
        return goalService.insert(goalRequestDTO);
    }

    @PutMapping("/{id}")
    public GoalResponseDTO update(@PathVariable Long id, @RequestBody GoalRequestDTO goalRequestDTO ) {
        return goalService.update(id, goalRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        goalService.delete(id);
    }
}
