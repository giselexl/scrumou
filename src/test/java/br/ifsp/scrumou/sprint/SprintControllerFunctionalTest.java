package br.ifsp.scrumou.sprint;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SprintControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateSprintSuccessfully() throws Exception {
        String json = String.format("""
                    {
                        "title": "Functional Test Sprint",
                        "startDate": "%s",
                        "endDate": "%s"
                    }
                """, LocalDate.now(), LocalDate.now().plusDays(14));

        mockMvc.perform(post("/api/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Functional Test Sprint"));
    }

    @Test
    public void shouldReturnListOfSprints() throws Exception {
        mockMvc.perform(get("/api/sprints"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void shouldReturnSprintById() throws Exception {
        String createJson = String.format("""
                {
                    "title": "Sprint to Get",
                    "startDate": "%s",
                    "endDate": "%s"
                }
                """, LocalDate.now(), LocalDate.now().plusDays(14));
        MvcResult createResult = mockMvc.perform(post("/api/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer sprintId = JsonPath.read(responseString, "$.id");

        mockMvc.perform(get("/api/sprints/" + sprintId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sprintId));
    }

    @Test
    public void shouldReturnNotFoundWhenSprintDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/sprints/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateSprintPartially() throws Exception {
        String createJson = String.format("""
                {
                    "title": "Sprint to Update Partially",
                    "startDate": "%s",
                    "endDate": "%s"
                }
                """, LocalDate.now(), LocalDate.now().plusDays(14));
        MvcResult createResult = mockMvc.perform(post("/api/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer sprintId = JsonPath.read(responseString, "$.id");

        String updateJson = """
                {
                    "title": "Updated Title"
                }
                """;
        mockMvc.perform(patch("/api/sprints/" + sprintId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void shouldDeleteSprint() throws Exception {
        String createJson = String.format("""
                {
                    "title": "Sprint to Delete",
                    "startDate": "%s",
                    "endDate": "%s"
                }
                """, LocalDate.now(), LocalDate.now().plusDays(14));
        MvcResult createResult = mockMvc.perform(post("/api/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer sprintId = JsonPath.read(responseString, "$.id");

        mockMvc.perform(delete("/api/sprints/" + sprintId))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/sprints/" + sprintId))
                .andExpect(status().isNotFound());
    }
}
