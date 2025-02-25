package com.ega.to_do_app.tasks;

import com.ega.to_do_app.tasks.domain.models.Task;
import com.ega.to_do_app.tasks.infrastructure.persistences.entities.TaskEntity;
import com.ega.to_do_app.tasks.infrastructure.rest.dtos.TaskDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskDataProvider {

    public static TaskDto returnDtoCompraSSD() {
        return TaskDto.builder()
                .taskName("Comprar SSD")
                .taskDescription("Revisar precios y comprar donde este mas barato")
                .taskStartDate(LocalDate.now())
                .taskDueDate(LocalDate.now().plusDays(1))
                .finished(false)
                .build();
    }

    public static Task returnTaskCompraSSD() {
        return Task.builder()
                .taskName("Comprar SSD")
                .taskDescription("Revisar precios y comprar donde este mas barato")
                .taskStartDate(LocalDate.now())
                .taskDueDate(LocalDate.now().plusDays(1))
                .finished(false)
                .build();
    }

    public static TaskEntity returnEntityCompraSSD() {
        return TaskEntity.builder()
                .taskName("Comprar SSD")
                .taskDescription("Revisar precios y comprar donde este mas barato")
                .taskStartDate(LocalDate.now())
                .taskDueDate(LocalDate.now().plusDays(1))
                .finished(false)
                .build();
    }

    public static Task returnTaskCompraSSDActualizado() {
        return Task.builder()
                .taskName("Comprar Disco Duro SSD 480GB")
                .taskDescription("Kingston o ADATA")
                .taskStartDate(LocalDate.now().plusDays(1))
                .taskDueDate(LocalDate.now().plusDays(2))
                .finished(false)
                .build();
    }

    public static Task returnTaskNullFields() {
        return Task.builder()
                .taskName(null)
                .taskDescription(null)
                .taskStartDate(null)
                .taskDueDate(null)
                .finished(null)
                .build();
    }

    public static Task returnTaskEmptyFields() {
        return Task.builder()
                .taskName("")
                .taskDescription("")
                .taskStartDate(null)
                .taskDueDate(null)
                .finished(null)
                .build();
    }

    public static String returnStringDto() {
        String startDate = LocalDate.now().toString();
        String dueDate = LocalDate.now().plusDays(1).toString();
        return "{" +
                "\"taskName\" : \"Comprar SSD\"," +
                "\"taskDescription\" : \"Revisar precios y comprar donde este mas barato\"," +
                "\"taskStartDate\" : \"" + startDate + "\"," +
                "\"taskDueDate\" : \"" + dueDate + "\"," +
                "\"finished\" : false" +
                "}";
    }

    public static TaskDto returnDtoToMismatchedDates() {
        return TaskDto.builder()
                .taskName("Comprar SSD")
                .taskDescription("This is a new line")
                .taskStartDate(LocalDate.of(2025, 4, 29))
                .taskDueDate(null)
                .finished(false)
                .build();
    }
}
