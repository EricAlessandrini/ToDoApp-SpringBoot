package com.ega.to_do_app.tasks.infrastructure.persistences.dataAccess;

import com.ega.to_do_app.tasks.application.output.OutputPort;
import com.ega.to_do_app.tasks.domain.exceptions.InvalidInputException;
import com.ega.to_do_app.tasks.domain.exceptions.MismatchedDatesException;
import com.ega.to_do_app.tasks.domain.exceptions.TaskNotFoundException;
import com.ega.to_do_app.tasks.domain.models.Task;
import com.ega.to_do_app.tasks.infrastructure.persistences.entities.TaskEntity;
import com.ega.to_do_app.tasks.infrastructure.persistences.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                .finished(task.getFinished())
                .build();

        taskRepository.save(entity);
    }

    @Override
    public Task findByTaskName(String taskName) {
        TaskEntity entity = taskRepository.findByTaskName(taskName)
                .orElseThrow(TaskNotFoundException::new);

        return Task.builder()
                .taskName(entity.getTaskName())
                .taskDescription(entity.getTaskDescription())
                .taskStartDate(entity.getTaskStartDate())
                .taskDueDate(entity.getTaskDueDate())
                .finished(entity.getFinished())
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
                        .finished(entity.getFinished())
                        .build())
                .toList();
    }

    @Override
    public void updateTask(String taskName, Task task) {
        TaskEntity entity = taskRepository.findByTaskName(taskName)
                .orElseThrow(TaskNotFoundException::new);

        entity.setTaskName(task.getTaskName());

        if(task.getTaskDescription() != null && !task.getTaskDescription().trim().isEmpty()) {
            entity.setTaskDescription(task.getTaskDescription());
        }

        if (task.getTaskStartDate() != null && !task.getTaskStartDate().equals(entity.getTaskStartDate())) {
            entity.setTaskStartDate(task.getTaskStartDate());
        }

        if (task.getTaskDueDate() != null && !task.getTaskDueDate().equals(entity.getTaskDueDate())) {
            entity.setTaskDueDate(task.getTaskDueDate());
        }

        if (task.getFinished() != null && !task.getFinished().equals(entity.getFinished())) {
            entity.setFinished(task.getFinished());
        }

        if (entity.getTaskStartDate().isAfter(entity.getTaskDueDate())) {
            throw new MismatchedDatesException();
        }

        taskRepository.save(entity);
    }

    @Override
    public void deleteTask(String taskName) {
        TaskEntity entity = taskRepository.findByTaskName(taskName)
                        .orElseThrow(TaskNotFoundException::new);

        taskRepository.delete(entity);
    }
}
