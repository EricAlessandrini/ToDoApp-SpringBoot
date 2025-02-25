package com.ega.to_do_app.tasks.infrastructure.rest;

import com.ega.to_do_app.tasks.TaskDataProvider;
import com.ega.to_do_app.tasks.application.input.InputPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskRestController.class)
@Import(TestTaskRestController.Config.class)
public class TestTaskRestController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InputPort inputPort;

    @Test
    void testSaveTaskEndpoint() throws Exception {
        String dtoString = TaskDataProvider.returnStringDto();

        mockMvc.perform(post("/api/tasks/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoString))
                .andExpect(status().isCreated())
                .andExpect(content().string("Saved"));
    }

    @Test
    void findAllEndpoint() throws Exception {
        Mockito.when(inputPort.findAllTasks())
                .thenReturn(List.of(TaskDataProvider.returnDtoCompraSSD()));

        mockMvc.perform(get("/api/tasks/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].taskName")
                        .value("Comprar SSD"))
                .andExpect(jsonPath("$[0].taskDescription")
                        .value("Revisar precios y comprar donde este mas barato"))
                .andExpect(jsonPath("$[0].taskStartDate")
                        .value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[0].taskDueDate")
                        .value(LocalDate.now().plusDays(1).toString()))
                .andExpect(jsonPath("$[0].finished")
                        .value(false));
    }

    @Test
    void testFindByTaskNameEndpoint() throws Exception {
        String name = "Comprar SSD";
        Mockito.when(inputPort.findByTaskName(name))
                .thenReturn(TaskDataProvider.returnDtoCompraSSD());

        mockMvc.perform(get("/api/tasks/find")
                .param("taskName", name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskName")
                        .value("Comprar SSD"))
                .andExpect(jsonPath("$.taskDescription")
                        .value("Revisar precios y comprar donde este mas barato"))
                .andExpect(jsonPath("$.taskStartDate")
                        .value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.taskDueDate")
                        .value(LocalDate.now().plusDays(1).toString()))
                .andExpect(jsonPath("$.finished")
                        .value(false));
    }

    @Test
    void testUpdateTaskEndpoint() throws Exception {
        String name = "Comprar SSD";
        String dtoString = TaskDataProvider.returnStringDto();

        mockMvc.perform(put("/api/tasks/update")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("taskName", name)
                        .content(dtoString))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated"));
    }

    @Test
    void testDeleteEndpoint() throws Exception {
        String name = "Comprar SSD";

        mockMvc.perform(delete("/api/tasks/delete")
                .param("taskName", name))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }

    @TestConfiguration
    static class Config {
        @Bean
        public InputPort inputPort() {
            return Mockito.mock(InputPort.class);
        }
    }
}
