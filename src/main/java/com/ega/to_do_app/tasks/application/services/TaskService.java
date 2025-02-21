package com.ega.to_do_app.tasks.application.services;

import com.ega.to_do_app.tasks.application.input.InputPort;
import com.ega.to_do_app.tasks.application.output.OutputPort;
import com.ega.to_do_app.tasks.domain.exceptions.InvalidInputException;
import com.ega.to_do_app.tasks.domain.models.Task;
import com.ega.to_do_app.tasks.infrastructure.rest.dtos.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService implements InputPort {

    private final OutputPort outputPort;

    @Override
    public void saveTask(TaskDto task) {
        outputPort.saveTask(Task.builder()
                .taskName(task.getTaskName())
                .taskDescription(task.getTaskDescription())
                .taskStartDate(task.getTaskStartDate())
                .taskDueDate(task.getTaskDueDate())
                .finished(task.getFinished())
                .build());
    }

    @Override
    public TaskDto findByTaskName(String taskName) {
        if(taskName == null || taskName.trim().isEmpty()) {
            throw new InvalidInputException();
        }

        Task task = outputPort.findByTaskName(taskName);

        return TaskDto.builder()
                .taskName(task.getTaskName())
                .taskDescription(task.getTaskDescription())
                .taskStartDate(task.getTaskStartDate())
                .taskDueDate(task.getTaskDueDate())
                .finished(task.getFinished())
                .build();
    }

    @Override
    public List<TaskDto> findAllTasks() {
        return outputPort.findAllTasks().stream()
                .map(task -> TaskDto.builder()
                        .taskName(task.getTaskName())
                        .taskDescription(task.getTaskDescription())
                        .taskStartDate(task.getTaskStartDate())
                        .taskDueDate(task.getTaskDueDate())
                        .finished(task.getFinished())
                        .build())
                .toList();
    }

    @Override
    public void updateTask(String taskName, TaskDto task) {
        if(taskName == null || taskName.trim().isEmpty()) {
            throw new InvalidInputException();
        }

        Task model = Task.builder()
                .taskName(task.getTaskName())
                .taskDescription(task.getTaskDescription())
                .taskStartDate(task.getTaskStartDate())
                .taskDueDate(task.getTaskDueDate())
                .finished(task.getFinished())
                .build();

        outputPort.updateTask(taskName, model);
    }

    @Override
    public void deleteTask(String taskName) {
        if(taskName == null || taskName.trim().isEmpty()) {
            throw new InvalidInputException();
        }

        outputPort.deleteTask(taskName);
    }
}
