package com.ega.to_do_app.tasks.infrastructure.persistences.entities;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "tasks")
public class TaskEntity {

    @Id
    private String id;
    private String taskName;
    private String taskDescription;
    private LocalDate taskStartDate;
    private LocalDate taskDueDate;
    private Boolean finished;

}
