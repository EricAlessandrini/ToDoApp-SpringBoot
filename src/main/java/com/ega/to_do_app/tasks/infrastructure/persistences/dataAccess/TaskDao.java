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
import java.util.Optional;

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

        updateEntityFields(entity, task);

        validateDates(entity);

        taskRepository.save(entity);
    }

    private void updateEntityFields(TaskEntity entity, Task task) {
        Optional.ofNullable(task.getTaskName())
                .filter(name -> !name.trim().isEmpty())
                .ifPresent(entity::setTaskName);
        Optional.ofNullable(task.getTaskDescription())
                .filter(desc -> !desc.trim().isEmpty())
                .ifPresent(entity::setTaskDescription);
        Optional.ofNullable(task.getTaskStartDate()).ifPresent(entity::setTaskStartDate);
        Optional.ofNullable(task.getTaskDueDate()).ifPresent(entity::setTaskDueDate);
        Optional.ofNullable(task.getFinished()).ifPresent(entity::setFinished);
    }

    private void validateDates(TaskEntity entity) {
        if (entity.getTaskStartDate().isAfter(entity.getTaskDueDate())) {
            throw new MismatchedDatesException();
        }
    }

    @Override
    public void deleteTask(String taskName) {
        TaskEntity entity = taskRepository.findByTaskName(taskName)
                        .orElseThrow(TaskNotFoundException::new);

        taskRepository.delete(entity);
    }

}
