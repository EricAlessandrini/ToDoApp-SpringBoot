package com.ega.to_do_app.tasks.infrastructure.persistences.dataAccess;

import com.ega.to_do_app.tasks.application.output.OutputPort;
import com.ega.to_do_app.tasks.domain.models.Task;
import com.ega.to_do_app.tasks.infrastructure.persistences.entities.TaskEntity;
import com.ega.to_do_app.tasks.infrastructure.persistences.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskDao implements OutputPort {

    private final TaskRepository taskRepository;

    @Override
    public void saveTask(Task task) {
        TaskEntity entity = TaskEntity.builder()
                .taskName(task.getTaskName())
                .taskDescription(task.getTaskDescription())
                .taskStartDate(task.getTaskStartDate())
                .taskDueDate(task.getTaskDueDate())
                .finished(task.isFinished())
                .build();

        taskRepository.save(entity);
    }

    @Override
    public Task findByTaskName(String taskName) {
        TaskEntity entity = taskRepository.findByTaskName(taskName)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return Task.builder()
                .taskName(entity.getTaskName())
                .taskDescription(entity.getTaskDescription())
                .taskStartDate(entity.getTaskStartDate())
                .taskDueDate(entity.getTaskDueDate())
                .finished(entity.isFinished())
                .build();
    }

    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(entity -> Task.builder()
                        .taskName(entity.getTaskName())
                        .taskDescription(entity.getTaskDescription())
                        .taskStartDate(entity.getTaskStartDate())
                        .taskDueDate(entity.getTaskDueDate())
                        .finished(entity.isFinished())
                        .build())
                .toList();
    }

    @Override
    public void updateTask(String taskName, Task task) {
        TaskEntity entity = taskRepository.findByTaskName(taskName)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        entity.setTaskName(task.getTaskName());
        entity.setTaskDescription(task.getTaskDescription());
        entity.setTaskStartDate(task.getTaskStartDate());
        entity.setTaskDueDate(task.getTaskDueDate());
        entity.setFinished(task.isFinished());

        taskRepository.save(entity);
    }

    @Override
    public void deleteTask(String taskName) {
        TaskEntity entity = taskRepository.findByTaskName(taskName)
                        .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(entity);
    }
}
