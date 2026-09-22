package com.app.aiassistant.repository;

import com.app.aiassistant.entity.Task;
import com.app.aiassistant.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByStatus(TaskStatus pendingStatus, TaskStatus runningStatus);
}
