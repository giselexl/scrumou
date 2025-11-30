package br.ifsp.scrumou.github;

import br.ifsp.scrumou.controller.GithubController;
import br.ifsp.scrumou.dto.github.GithubRepoDTO;
import br.ifsp.scrumou.service.GithubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GithubControllerUnitTest {

    @Mock
    private GithubService githubService;

    @InjectMocks
    private GithubController githubController;

    @Test
    public void testGetRepo_ReturnsRepository() {
        GithubRepoDTO repoDTO = new GithubRepoDTO();
        repoDTO.setId("123456");
        repoDTO.setName("hello-world");
        repoDTO.setFull_name("octocat/hello-world");
        repoDTO.setDescription("Test repository");
        repoDTO.setHtml_url("https://github.com/octocat/hello-world");
        repoDTO.setLanguage("Java");
        repoDTO.setStargazers_count(50);
        repoDTO.setForks_count(10);
        repoDTO.setOpen_issues_count(5);

        when(githubService.getRepository("octocat", "hello-world")).thenReturn(repoDTO);

        ResponseEntity<GithubRepoDTO> response = githubController.getRepo("octocat", "hello-world");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("hello-world", response.getBody().getName());
        assertEquals("octocat/hello-world", response.getBody().getFull_name());
    }

    @Test
    public void testGetRepo_ReturnsNotFoundForNonExistentRepository() {
        when(githubService.getRepository("nonexistent", "repo")).thenReturn(null);

        ResponseEntity<GithubRepoDTO> response = githubController.getRepo("nonexistent", "repo");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetRepo_VerifyStargazersCount() {
        GithubRepoDTO repoDTO = new GithubRepoDTO();
        repoDTO.setId("123456");
        repoDTO.setName("hello-world");
        repoDTO.setStargazers_count(1000);
        repoDTO.setForks_count(500);

        when(githubService.getRepository("popular", "repo")).thenReturn(repoDTO);

        ResponseEntity<GithubRepoDTO> response = githubController.getRepo("popular", "repo");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1000, response.getBody().getStargazers_count());
        assertEquals(500, response.getBody().getForks_count());
    }

    @Test
    public void testGetRepo_VerifyLanguageField() {
        GithubRepoDTO repoDTO = new GithubRepoDTO();
        repoDTO.setId("123456");
        repoDTO.setName("project");
        repoDTO.setLanguage("Python");

        when(githubService.getRepository("user", "project")).thenReturn(repoDTO);

        ResponseEntity<GithubRepoDTO> response = githubController.getRepo("user", "project");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Python", response.getBody().getLanguage());
    }
}
