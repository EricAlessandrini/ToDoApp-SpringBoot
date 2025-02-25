package com.ega.to_do_app.tasks.application.services;

import com.ega.to_do_app.tasks.TaskDataProvider;
import com.ega.to_do_app.tasks.application.output.OutputPort;
import com.ega.to_do_app.tasks.domain.exceptions.InvalidInputException;
import com.ega.to_do_app.tasks.domain.models.Task;
import com.ega.to_do_app.tasks.infrastructure.rest.dtos.TaskDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(value = MockitoExtension.class)
public class TestTaskService {

    @Mock
    private OutputPort outputPort;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testSaveTaskSuccessful() {
        // Given
        TaskDto dto = TaskDataProvider.returnDtoCompraSSD();

        // When
        taskService.saveTask(dto);

        // Then
        ArgumentCaptor<Task> captureTask = ArgumentCaptor.forClass(Task.class);
        verify(outputPort, times(1)).saveTask(captureTask.capture());

        Task task = captureTask.getValue();
        compareObjects(dto, task);
    }

    @Test
    void testFindByTaskNameSuccessful() {
        // Given
        String someName = "Comprar SSD";
        Task task = TaskDataProvider.returnTaskCompraSSD();
        Mockito.when(outputPort.findByTaskName(someName)).thenReturn(task);

        // When
        TaskDto dtoReceived = taskService.findByTaskName(someName);

        // Then
        verify(outputPort, times(1)).findByTaskName(someName);
        assertEquals(someName, dtoReceived.getTaskName());
        compareObjects(dtoReceived, task);
    }

    @Test
    void testFindByTaskName_ThrowsInvalidInputException() {
        // Given
        String emptyString = "";
        String onlySpaces = "   ";

        // Then
        assertThrows(InvalidInputException.class, () -> taskService.findByTaskName(emptyString));
        assertThrows(InvalidInputException.class, () -> taskService.findByTaskName(null));
        assertThrows(InvalidInputException.class, () -> taskService.findByTaskName(onlySpaces));

        verify(outputPort, never()).findByTaskName(anyString());
    }

    @Test
    void testFindAllTasksSuccessful() {
        // Given
        List<Task> tasks = List.of(TaskDataProvider.returnTaskCompraSSD());
        Mockito.when(outputPort.findAllTasks()).thenReturn(tasks);

        // When
        List<TaskDto> dtos = taskService.findAllTasks();

        // Then
        assertNotNull(dtos);
        assertThat(dtos).isNotEmpty();
        assertEquals(1, dtos.size());
        assertThat(dtos).hasOnlyElementsOfType(TaskDto.class);

        for(TaskDto dto : dtos) {
            compareObjects(dto, tasks.getFirst());
        }
    }

    @Test
    void testUpdateTaskSuccessful() {
        String name = "Compra SSD";
        TaskDto dto = TaskDataProvider.returnDtoCompraSSD();

        taskService.updateTask(name, dto);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        ArgumentCaptor<String> taskNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputPort, times(1))
                .updateTask(taskNameCaptor.capture(), taskCaptor.capture());

        String taskNameCaptured = taskNameCaptor.getValue();
        assertNotNull(taskNameCaptured);
        assertThat(taskNameCaptured.trim()).isNotEmpty();
        assertEquals(name, taskNameCaptured);

        Task task = taskCaptor.getValue();
        compareObjects(dto, task);
    }

    @Test
    void testUpdateTask_ThrowsInvalidInputException() {
        String empty = "";
        String onlySpaces = "  ";

        assertThrows(InvalidInputException.class, () -> taskService.updateTask(null, null));
        assertThrows(InvalidInputException.class, () -> taskService.updateTask(empty, null));
        assertThrows(InvalidInputException.class, () -> taskService.updateTask(onlySpaces, null));

        verify(outputPort, never()).updateTask(anyString(), any(Task.class));
    }

    @Test
    void testDeleteTaskSuccessful() {
        String name = "Comprar SSD";

        taskService.deleteTask(name);

        verify(outputPort, times(1)).deleteTask(name);
    }

    @Test
    void testDeleteTask_ThrowsInvalidInputException() {
        // Given
        String emptyString = "";
        String onlySpaces = "   ";

        // Then
        assertThrows(InvalidInputException.class, () -> taskService.deleteTask(emptyString));
        assertThrows(InvalidInputException.class, () -> taskService.deleteTask(null));
        assertThrows(InvalidInputException.class, () -> taskService.deleteTask(onlySpaces));

        verify(outputPort, never()).deleteTask(anyString());
    }

    public static void compareObjects(TaskDto expected, Task actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getTaskName(), actual.getTaskName());
        assertEquals(expected.getTaskDescription(), actual.getTaskDescription());
        assertEquals(expected.getTaskStartDate(), actual.getTaskStartDate());
        assertEquals(expected.getTaskDueDate(), actual.getTaskDueDate());
        assertEquals(expected.getFinished(), actual.getFinished());
    }
}
