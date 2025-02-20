package com.ega.to_do_app.tasks.infrastructure;

import com.ega.to_do_app.tasks.infrastructure.rest.dtos.TaskDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/tasks")
public interface TaskEndpoints {

    @PostMapping("/save")
    ResponseEntity<String> saveTask(@RequestBody @Valid TaskDto taskDto);

    @GetMapping("/find")
    ResponseEntity<TaskDto> findByTaskName(@RequestParam("taskName") String taskName);

    @GetMapping("/all")
    ResponseEntity<List<TaskDto>> findAllTasks();

    @PutMapping("/update")
    ResponseEntity<String> updateTask(@RequestParam("taskName") String taskName,
                                      @RequestBody @Valid TaskDto taskDto);

    @DeleteMapping("/delete")
    ResponseEntity<String> deleteTask(@RequestParam("taskName") String taskName);

}
