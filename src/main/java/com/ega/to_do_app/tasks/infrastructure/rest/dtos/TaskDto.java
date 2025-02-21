package com.ega.to_do_app.tasks.infrastructure.rest.dtos;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto {

    @NotBlank(message = "Es necesario que la tarea tenga un nombre")
    @Size(max = 100, message = "El nombre no puede tener mas de 100 caracteres")
    private String taskName;
    @Size(max = 255, message = "La descripcion no puede tener mas de 255 caracteres")
    private String taskDescription;
    @FutureOrPresent(message = "La fecha no puede estar en el pasado")
    private LocalDate taskStartDate;
    @Future(message = "La fecha tiene que ser en el futuro")
    private LocalDate taskDueDate;
    private Boolean finished;

}
