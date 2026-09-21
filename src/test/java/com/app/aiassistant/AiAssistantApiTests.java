package com.app.aiassistant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
class AiAssistantApiTests {

    @Autowired
    private JsonMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFindTasks() throws Exception {
        mockMvc.perform(
                get("/tasks")
        ).andExpect(
                org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .status().isOk()
        );
    }

    @Test
    void shouldCreateTask() throws Exception {

        String json = """
            {
                "title": "Estudar Java",
                "description": "Estudar por duas horas",
                "durationMinutes": 120,
                "dueAt": "25/09/2026 19:00:00"
            }
            """;

        mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    void activityShouldChangeTaskProgress() throws Exception {

        String taskJson = """
                {
                    "title": "Estudar Java",
                    "description": "Estudar Java por duas horas",
                    "durationMinutes": 120,
                    "dueAt": "25/09/2026 19:00:00"
                }
                """;

        String taskResponse = mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(taskJson)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode taskJsonNode = objectMapper.readTree(taskResponse);
        long taskId = taskJsonNode.get("id").asLong();

        String activityJson = """
                {
                    "description": "Estudei Java por uma hora",
                    "durationMinutes": 60,
                    "date": "20/09/2026",
                    "taskID": %d
                }
                """.formatted(taskId);

        mockMvc.perform(
                        post("/activities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(activityJson)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/tasks/" + taskId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.durationMinutes").value(120))
                .andExpect(jsonPath("$.completedValue").value(60))
                .andExpect(jsonPath("$.completedPercentage").value(50.0))
                .andExpect(jsonPath("$.status").value("RUNNING"));
    }

    @Test
    void activityShouldCompleteTask() throws Exception {

        String taskJson = """
            {
                "title": "Estudar Java",
                "description": "Estudar Java por duas horas",
                "durationMinutes": 120,
                "dueAt": "25/09/2026 19:00:00"
            }
            """;

        String taskResponse = mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(taskJson)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode taskNode = objectMapper.readTree(taskResponse);
        long taskId = taskNode.get("id").asLong();

        String firstActivityJson = """
            {
                "description": "Estudei Java por uma hora",
                "durationMinutes": 60,
                "date": "20/09/2026",
                "taskID": %d
            }
            """.formatted(taskId);

        mockMvc.perform(
                        post("/activities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(firstActivityJson)
                )
                .andExpect(status().isOk());

        String secondActivityJson = """
            {
                "description": "Estudei Java por mais uma hora",
                "durationMinutes": 60,
                "date": "20/09/2026",
                "taskID": %d
            }
            """.formatted(taskId);

        mockMvc.perform(
                        post("/activities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(secondActivityJson)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/tasks/" + taskId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.durationMinutes").value(120))
                .andExpect(jsonPath("$.completedValue").value(120))
                .andExpect(jsonPath("$.completedPercentage").value(100.0))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.completedAt").isNotEmpty());
    }
}
