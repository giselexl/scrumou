package br.ifsp.scrumou.meeting;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
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
public class MeetingControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    private Integer sprintId;

    @BeforeEach
    public void setup() throws Exception {
        String sprintJson = String.format("""
                {
                    "title": "Setup Sprint for Meeting Test",
                    "startDate": "%s",
                    "endDate": "%s"
                }
            """, LocalDate.now(), LocalDate.now().plusDays(14));

        MvcResult result = mockMvc.perform(post("/api/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sprintJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        sprintId = JsonPath.read(responseString, "$.id");
    }

    @Test
    public void shouldCreateMeetingSuccessfully() throws Exception {
        String json = String.format("""
                    {
                        "title": "Functional Test Meeting",
                        "minutes": "Some minutes",
                        "date": "%s",
                        "time": "10:00",
                        "sprint": {
                            "id": %d
                        }
                    }
                """, LocalDate.now(), sprintId);

        mockMvc.perform(post("/api/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Functional Test Meeting"));
    }

    @Test
    public void shouldReturnListOfMeetings() throws Exception {
        mockMvc.perform(get("/api/meetings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void shouldReturnMeetingById() throws Exception {
        String createJson = String.format("""
                {
                    "title": "Meeting to Get",
                    "minutes": "Some minutes",
                    "date": "%s",
                    "time": "11:00",
                    "sprint": {
                        "id": %d
                    }
                }
                """, LocalDate.now(), sprintId);
        MvcResult createResult = mockMvc.perform(post("/api/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer meetingId = JsonPath.read(responseString, "$.id");

        mockMvc.perform(get("/api/meetings/" + meetingId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(meetingId));
    }

    @Test
    public void shouldReturnMeetingsBySprintId() throws Exception {
        String createJson = String.format("""
                {
                    "title": "Meeting for Sprint",
                    "minutes": "Some minutes",
                    "date": "%s",
                    "time": "12:00",
                    "sprint": {
                        "id": %d
                    }
                }
                """, LocalDate.now(), sprintId);
        mockMvc.perform(post("/api/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/meetings/sprint/" + sprintId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].sprint.id").value(sprintId));
    }

    @Test
    public void shouldReturnNotFoundWhenMeetingDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/meetings/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateMeetingPartially() throws Exception {
        String createJson = String.format("""
                {
                    "title": "Meeting to Update Partially",
                    "minutes": "Some minutes",
                    "date": "%s",
                    "time": "13:00",
                    "sprint": {
                        "id": %d
                    }
                }
                """, LocalDate.now(), sprintId);
        MvcResult createResult = mockMvc.perform(post("/api/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer meetingId = JsonPath.read(responseString, "$.id");

        String updateJson = """
                {
                    "title": "Updated Title"
                }
                """;
        mockMvc.perform(patch("/api/meetings/" + meetingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void shouldDeleteMeeting() throws Exception {
        String createJson = String.format("""
                {
                    "title": "Meeting to Delete",
                    "minutes": "Some minutes",
                    "date": "%s",
                    "time": "14:00",
                    "sprint": {
                        "id": %d
                    }
                }
                """, LocalDate.now(), sprintId);
        MvcResult createResult = mockMvc.perform(post("/api/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = createResult.getResponse().getContentAsString();
        Integer meetingId = JsonPath.read(responseString, "$.id");

        mockMvc.perform(delete("/api/meetings/" + meetingId))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/meetings/" + meetingId))
                .andExpect(status().isNotFound());
    }
}
