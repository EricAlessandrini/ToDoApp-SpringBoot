package com.ega.to_do_app.tasks.infrastructure.rest;

import com.ega.to_do_app.tasks.application.input.InputPort;
import com.ega.to_do_app.tasks.infrastructure.TaskEndpoints;
import com.ega.to_do_app.tasks.infrastructure.rest.dtos.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskRestController implements TaskEndpoints {

    private final InputPort inputPort;

    @Override
    public ResponseEntity<String> saveTask(TaskDto taskDto) {
        inputPort.saveTask(taskDto);
        return new ResponseEntity<>("Saved", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TaskDto> findByTaskName(String taskName) {
        TaskDto task = inputPort.findByTaskName(taskName);
        return new ResponseEntity<>(task, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<List<TaskDto>> findAllTasks() {
        List<TaskDto> tasks = inputPort.findAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<String> updateTask(String taskName, TaskDto taskDto) {
        inputPort.updateTask(taskName, taskDto);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteTask(String taskName) {
        inputPort.deleteTask(taskName);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
