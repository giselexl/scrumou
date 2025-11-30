package br.ifsp.scrumou.github;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GithubControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGetRepositorySuccessfully() throws Exception {
        mockMvc.perform(get("/api/github/repo")
                .param("owner", "octocat")
                .param("repo", "hello-world"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.full_name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void shouldReturnNotFoundForNonExistentRepository() throws Exception {
        mockMvc.perform(get("/api/github/repo")
                .param("owner", "nonexistentowner")
                .param("repo", "nonexistentrepo"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnRepositoryWithAllFields() throws Exception {
        mockMvc.perform(get("/api/github/repo")
                .param("owner", "octocat")
                .param("repo", "hello-world"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.full_name").exists())
                .andExpect(jsonPath("$.html_url").exists())
                .andExpect(jsonPath("$.stargazers_count").isNumber())
                .andExpect(jsonPath("$.forks_count").isNumber())
                .andExpect(jsonPath("$.open_issues_count").isNumber());
    }

    @Test
    public void shouldReturnRepositoryLanguageWhenAvailable() throws Exception {
        mockMvc.perform(get("/api/github/repo")
                .param("owner", "octocat")
                .param("repo", "hello-world"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name").exists());
    }

    @Test
    public void shouldReturnServerErrorWhenOwnerMissing() throws Exception {
        mockMvc.perform(get("/api/github/repo")
                .param("repo", "hello-world"))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldReturnServerErrorWhenRepoMissing() throws Exception {
        mockMvc.perform(get("/api/github/repo")
                .param("owner", "octocat"))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }
}
