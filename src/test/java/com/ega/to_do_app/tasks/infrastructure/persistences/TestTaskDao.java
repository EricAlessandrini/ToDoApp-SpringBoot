package com.ega.to_do_app.tasks.infrastructure.persistences;

import com.ega.to_do_app.tasks.TaskDataProvider;
import com.ega.to_do_app.tasks.domain.exceptions.MismatchedDatesException;
import com.ega.to_do_app.tasks.domain.exceptions.TaskNotFoundException;
import com.ega.to_do_app.tasks.domain.models.Task;
import com.ega.to_do_app.tasks.infrastructure.persistences.dataAccess.TaskDao;
import com.ega.to_do_app.tasks.infrastructure.persistences.entities.TaskEntity;
import com.ega.to_do_app.tasks.infrastructure.persistences.repositories.TaskRepository;

import com.ega.to_do_app.tasks.infrastructure.rest.dtos.TaskDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestTaskDao {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskDao taskDao;

    @Test
    void saveTaskSuccessful() {
        Task task = TaskDataProvider.returnTaskCompraSSD();

        taskDao.saveTask(task);

        ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository, times(1)).save(taskEntityArgumentCaptor.capture());

        TaskEntity entity = taskEntityArgumentCaptor.getValue();
        compareObjectsAndAssert(entity, task);
    }

    @Test
    void findByTaskNameSuccessful() {
        String name = "Comprar SSD";
        TaskEntity entity = TaskDataProvider.returnEntityCompraSSD();
        when(taskRepository.findByTaskName(name)).thenReturn(Optional.of(entity));

        Task task = taskDao.findByTaskName(name);

        assertEquals(name, task.getTaskName());
        compareObjectsAndAssert(entity, task);
    }

    @Test
    void findByTaskName_ThrowsTaskNotFoundException() {
        String name = "Task name";
        when(taskRepository.findByTaskName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskDao.findByTaskName(name))
                .isInstanceOf(TaskNotFoundException.class)
                        .hasMessage("Task was not found");
    }

    @Test
    void findAllTasksSuccessful() {
        List<TaskEntity> entities = List.of(TaskDataProvider.returnEntityCompraSSD());
        when(taskRepository.findAll()).thenReturn(entities);

        List<Task> tasks = taskDao.findAllTasks();

        assertThat(tasks).isNotEmpty();
        assertEquals(1, tasks.size());
        assertThat(tasks).hasOnlyElementsOfType(Task.class);

        for(Task task : tasks) {
            compareObjectsAndAssert(entities.getFirst(), task);
        }
    }

    @Test
    void testUpdateAllFieldsSuccessful() {
        String name = "Comprar SSD";
        TaskEntity entity = TaskDataProvider.returnEntityCompraSSD();
        Task taskUpdate = TaskDataProvider.returnTaskCompraSSDActualizado();
        when(taskRepository.findByTaskName(name)).thenReturn(Optional.of(entity));

        taskDao.updateTask(name, taskUpdate);

        ArgumentCaptor<TaskEntity> entityUpdated = ArgumentCaptor.forClass(TaskEntity.class);

        verify(taskRepository, times(1)).findByTaskName(name);
        verify(taskRepository, times(1)).save(entityUpdated.capture());

        TaskEntity entityCaptured = entityUpdated.getValue();
        compareObjectsAndAssert(entityCaptured, taskUpdate);
    }

    @Test
    void testUpdateFailed_NullFields() {
        String name = "Comprar SSD";
        TaskEntity entity = TaskDataProvider.returnEntityCompraSSD();
        when(taskRepository.findByTaskName(name)).thenReturn(Optional.of(entity));
        Task taskReceived = TaskDataProvider.returnTaskNullFields();

        taskDao.updateTask(name, taskReceived);

        ArgumentCaptor<TaskEntity> entityUpdated = ArgumentCaptor.forClass(TaskEntity.class);

        verify(taskRepository, times(1)).findByTaskName(name);
        verify(taskRepository, times(1)).save(entityUpdated.capture());

        TaskEntity entityCaptured = entityUpdated.getValue();
        assertThat(entityCaptured).isNotNull();
        assertThat(entityCaptured.getTaskName()).isEqualTo("Comprar SSD");
        assertThat(entityCaptured.getTaskDescription())
                .isEqualTo("Revisar precios y comprar donde este mas barato");
        assertThat(entityCaptured.getTaskStartDate()).isEqualTo(LocalDate.now());
        assertThat(entityCaptured.getTaskDueDate()).isEqualTo(LocalDate.now().plusDays(1));
        assertThat(entityCaptured.getFinished()).isEqualTo(false);
    }

    @Test
    void testUpdateFailed_EmptyFields() {
        String name = "Comprar SSD";
        TaskEntity entityRetrieved = TaskDataProvider.returnEntityCompraSSD();
        when(taskRepository.findByTaskName(name)).thenReturn(Optional.of(entityRetrieved));
        Task taskReceived = TaskDataProvider.returnTaskEmptyFields();

        taskDao.updateTask(name, taskReceived);

        ArgumentCaptor<TaskEntity> entityUpdated = ArgumentCaptor.forClass(TaskEntity.class);

        verify(taskRepository, times(1)).findByTaskName(name);
        verify(taskRepository, times(1)).save(entityUpdated.capture());

        TaskEntity entityCaptured = entityUpdated.getValue();
        assertThat(entityCaptured).isNotNull();
        assertThat(entityCaptured.getTaskName()).isEqualTo("Comprar SSD");
        assertThat(entityCaptured.getTaskDescription())
                .isEqualTo("Revisar precios y comprar donde este mas barato");
    }

    @Test
    void testUpdateFailed_MismatchedDates() {
        String name = "Comprar SSD";
        TaskEntity entity = TaskDataProvider.returnEntityCompraSSD();
        when(taskRepository.findByTaskName(name))
                .thenReturn(Optional.of(entity));
        Task taskReceived = Task.builder()
                .taskName("Comprar SSD")
                .taskDescription("Revisar precios y comprar donde este mas barato")
                .taskStartDate(LocalDate.of(2025, 3, 24))
                .taskDueDate(null)
                .finished(false)
                .build();

        assertThatThrownBy(() -> taskDao.updateTask(name, taskReceived))
                .isInstanceOf(MismatchedDatesException.class)
                .hasMessage("New start date cannot be after than due date saved");
    }

    @Test
    void deleteTaskSuccessful() {
        String name = "Comprar SSD";
        TaskEntity entity = TaskDataProvider.returnEntityCompraSSD();
        when(taskRepository.findByTaskName(name)).thenReturn(Optional.of(entity));

        taskDao.deleteTask(name);

        verify(taskRepository, times(1)).delete(entity);
    }

    @Test
    void deleteTask_ThrowsTaskNotFoundException() {
        String name = "Nombre invalido";
        when(taskRepository.findByTaskName(name)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskDao.deleteTask(name));
    }


    public static void compareObjectsAndAssert(TaskEntity entity, Task task) {
        assertThat(entity).isNotNull();
        assertThat(task).isNotNull();
        assertThat(entity.getTaskName()).isEqualTo(task.getTaskName());
        assertThat(entity.getTaskDescription())
                .isEqualTo(task.getTaskDescription());
        assertThat(entity.getTaskStartDate()).isEqualTo(task.getTaskStartDate());
        assertThat(entity.getTaskDueDate()).isEqualTo(task.getTaskDueDate());
        assertThat(entity.getFinished()).isEqualTo(task.getFinished());
    }
}
