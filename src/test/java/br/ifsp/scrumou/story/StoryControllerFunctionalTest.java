package br.ifsp.scrumou.story;

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
public class StoryControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateStorySuccessfully() throws Exception {
        String json = """
                    {
                        "title": "Functional Test Story",
                        "description": "Functional Test Description",
                        "priority": 1
                    }
                """;

        mockMvc.perform(post("/api/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Functional Test Story"));
    }

    @Test
    public void shouldReturnListOfStories() throws Exception {
        mockMvc.perform(get("/api/stories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void shouldReturnStoryById() throws Exception {
        String createJson = """
                {
                    "title": "Story to Get",
                    "description": "Description",
                    "priority": 1
                }
                """;
        MvcResult createResult = mockMvc.perform(post("/api/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer storyId = JsonPath.read(responseString, "$.id");

        mockMvc.perform(get("/api/stories/" + storyId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(storyId));
    }

    @Test
    public void shouldReturnNotFoundWhenStoryDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/stories/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateStoryPriority() throws Exception {
        String createJson = """
                {
                    "title": "Story to Update Priority",
                    "description": "Description",
                    "priority": 1
                }
                """;
        MvcResult createResult = mockMvc.perform(post("/api/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer storyId = JsonPath.read(responseString, "$.id");

        mockMvc.perform(patch("/api/stories/" + storyId + "/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority").value(2));
    }

    @Test
    public void shouldUpdateStoryPartially() throws Exception {
        String createJson = """
                {
                    "title": "Story to Update Partially",
                    "description": "Description",
                    "priority": 1
                }
                """;
        MvcResult createResult = mockMvc.perform(post("/api/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer storyId = JsonPath.read(responseString, "$.id");

        String updateJson = """
                {
                    "title": "Updated Title"
                }
                """;
        mockMvc.perform(patch("/api/stories/" + storyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void shouldDeleteStory() throws Exception {
        String createJson = """
                {
                    "title": "Story to Delete",
                    "description": "Description",
                    "priority": 1
                }
                """;
        MvcResult createResult = mockMvc.perform(post("/api/stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer storyId = JsonPath.read(responseString, "$.id");

        mockMvc.perform(delete("/api/stories/" + storyId))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/stories/" + storyId))
                .andExpect(status().isNotFound());
    }
}
