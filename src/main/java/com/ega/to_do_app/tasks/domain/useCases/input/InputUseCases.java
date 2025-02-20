package com.ega.to_do_app.tasks.domain.useCases.input;

import java.util.List;

public interface InputUseCases<T, U> {

    void saveTask(T task);
    T findByTaskName(U taskName);
    List<T> findAllTasks();
    void updateTask(U taskName, T task);
    void deleteTask(U taskName);

}
