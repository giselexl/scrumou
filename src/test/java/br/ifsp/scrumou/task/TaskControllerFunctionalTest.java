package br.ifsp.scrumou.task;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateTaskSuccessfully() throws Exception {
        String json = """
                    {
                        "title": "Functional Test Task",
                        "description": "Functional Test Description",
                        "hourEstimated": 5,
                        "developer": "Functional Tester",
                        "status": "TO_DO"
                    }
                """;

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Functional Test Task"));
    }

    @Test
    public void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/tasks/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnListOfTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void shouldUpdateTaskStatus() throws Exception {
        String createJson = """
                {
                    "title": "Task to Update Status",
                    "description": "Description",
                    "hourEstimated": 1,
                    "developer": "Developer",
                    "status": "TO_DO"
                }
                """;
        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer taskId = JsonPath.read(responseString, "$.id");

        mockMvc.perform(patch("/api/tasks/" + taskId + "/DONE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    public void shouldUpdateTaskPartially() throws Exception {
        String createJson = """
                {
                    "title": "Task to Update Partially",
                    "description": "Description",
                    "hourEstimated": 1,
                    "developer": "Developer",
                    "status": "TO_DO"
                }
                """;
        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer taskId = JsonPath.read(responseString, "$.id");

        String updateJson = """
                {
                    "title": "Updated Title"
                }
                """;
        mockMvc.perform(patch("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        String createJson = """
                {
                    "title": "Task to Delete",
                    "description": "Description",
                    "hourEstimated": 1,
                    "developer": "Developer",
                    "status": "TO_DO"
                }
                """;
        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer taskId = JsonPath.read(responseString, "$.id");

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }
}
