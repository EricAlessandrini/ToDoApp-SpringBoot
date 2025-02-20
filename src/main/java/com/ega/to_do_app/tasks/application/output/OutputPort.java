package com.ega.to_do_app.tasks.application.output;

import com.ega.to_do_app.tasks.domain.models.Task;
import com.ega.to_do_app.tasks.domain.useCases.output.OutputUseCases;

public interface OutputPort extends OutputUseCases<Task, String> {
}
