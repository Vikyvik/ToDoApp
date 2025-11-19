package com.example.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createAndListTask() throws Exception {
        // Create new task
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"My First Task\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("My First Task"))
                .andExpect(jsonPath("$.done").value(false));

        // List all tasks should contain the created task
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void getTaskById_NotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("not found")));
    }

    @Test
    void createTask_Invalid() throws Exception {
        // No title
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"No title here\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Title is required")));
    }

    @Test
    void updateTaskPatch_andDelete() throws Exception {
        // Create a task
        String result = mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"To be updated\"}"))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        // Extract ID with primitive means (not pretty, avoids dependency)
        String id = result.replaceAll(".*\"id\":(\\d+).*", "$1");

        // Update the task
        mockMvc.perform(patch("/api/tasks/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"done\":true,\"title\":\"Now done\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.done").value(true))
            .andExpect(jsonPath("$.title").value("Now done"));

        // Delete the task
        mockMvc.perform(delete("/api/tasks/" + id))
            .andExpect(status().isNoContent());

        // Gone after delete
        mockMvc.perform(get("/api/tasks/" + id))
            .andExpect(status().isNotFound());
    }
}
