package com.app.aiassistant.service;

import com.app.aiassistant.entity.Task;
import com.app.aiassistant.exception.ResourceNotFoundException;
import com.app.aiassistant.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public Task findById(@PathVariable Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    public Task insert(Task task){
        return taskRepository.save(task);
    }

    public Task update(Long id, Task oldTaskInfo){
        Task newTaskInfo = findById(id);
        newTaskInfo.setTitle(oldTaskInfo.getTitle());
        newTaskInfo.setDescription(oldTaskInfo.getDescription());
        newTaskInfo.setStatus(oldTaskInfo.getStatus());
        newTaskInfo.setDueAt(oldTaskInfo.getDueAt());
        newTaskInfo.setCompletedAt(oldTaskInfo.getCompletedAt());

        return taskRepository.save(newTaskInfo);
    }

    public void delete(Long id){
        Task task = findById(id);
        taskRepository.delete(task);
    }
}
