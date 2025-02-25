package com.ega.to_do_app.tasks.infrastructure.rest;

import com.ega.to_do_app.tasks.TaskDataProvider;
import com.ega.to_do_app.tasks.application.input.InputPort;
import com.ega.to_do_app.tasks.domain.exceptions.InvalidInputException;
import com.ega.to_do_app.tasks.domain.exceptions.MismatchedDatesException;
import com.ega.to_do_app.tasks.domain.exceptions.TaskNotFoundException;
import com.ega.to_do_app.tasks.infrastructure.rest.dtos.TaskDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskRestController.class)
@Import(TestTaskControllerAdvice.Config.class)
public class TestTaskControllerAdvice {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InputPort inputPort;

    @Test
    void testTaskNotFoundExceptionIsThrown() throws Exception {
        String name = "Computacion Libre";
        Mockito.when(inputPort.findByTaskName(name))
                .thenThrow(new TaskNotFoundException());

        mockMvc.perform(get("/api/tasks/find")
                        .param("taskName", name))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code")
                        .value("T-001"))
                .andExpect(jsonPath("$.message")
                        .value("Task was not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testInvalidInputExceptionIsThrown() throws Exception {
        String input = "";
        Mockito.when(inputPort.findByTaskName(input))
                .thenThrow(new InvalidInputException());

        mockMvc.perform(get("/api/tasks/find")
                .param("taskName", input))
                .andExpect(status().isBadRequest())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code")
                        .value("T-002"))
                .andExpect(jsonPath("$.message")
                        .value("Provided task name is not valid for search"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testInvalidArgumentExceptionIsThrown() throws Exception {
        String invalidInput = "{}";

        mockMvc.perform(post("/api/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidInput))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("T-003"))
                .andExpect(jsonPath("$.message")
                        .value("Invalid field data received, check details for more information"))
                .andExpect(jsonPath("$.details")
                        .isNotEmpty())
                .andExpect(jsonPath("$.timestamp")
                        .exists());
    }

    @Test
    void testMismatchedDatesExceptionIsThrown() throws Exception {
        String name = "Comprar SSD";
        String taskDto = "{" +
                "\"taskName\" : \"Comprar SSD\"," +
                "\"taskDescription\" : \"This is a new task\"," +
                "\"taskStartDate\" : \"2025-04-24\"," +
                "\"taskDueDate\" : null," +
                "\"finished\" : false" +
                "}";

        Mockito.doThrow(new MismatchedDatesException()).when(inputPort)
                .updateTask(anyString(), any(TaskDto.class));

        mockMvc.perform(put("/api/tasks/update")
                .contentType(MediaType.APPLICATION_JSON)
                .param("taskName", name)
                .content(taskDto))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("T-004"))
                .andExpect(jsonPath("$.message")
                        .value("New start date cannot be after than due date saved"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());
    }

    @TestConfiguration
    static class Config {
        @Bean
        public InputPort inputPort() {
            return Mockito.mock(InputPort.class);
        }
    }
}
