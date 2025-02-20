package com.ega.to_do_app.tasks.application.input;

import com.ega.to_do_app.tasks.domain.useCases.input.InputUseCases;
import com.ega.to_do_app.tasks.infrastructure.rest.dtos.TaskDto;

public interface InputPort extends InputUseCases<TaskDto, String> {
}
