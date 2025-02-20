package com.ega.to_do_app.tasks.infrastructure.persistences.repositories;

import com.ega.to_do_app.tasks.infrastructure.persistences.entities.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<TaskEntity, String> {

    Optional<TaskEntity> findByTaskName(String taskName);
}
