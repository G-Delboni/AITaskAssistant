package com.app.aiassistant.repository;

import com.app.aiassistant.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>{
}
