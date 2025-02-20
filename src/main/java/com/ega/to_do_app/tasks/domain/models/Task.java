package com.ega.to_do_app.tasks.domain.models;
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
public class Task {

    public String taskName;
    public String taskDescription;
    public LocalDate taskStartDate;
    public LocalDate taskDueDate;
    public boolean finished;

}
