package com.ega.to_do_app.tasks.infrastructure.rest.dtos;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    private String taskName;
    private String taskDescription;
    private LocalDate taskStartDate;
    private LocalDate taskDueDate;
    private boolean finished;

}
