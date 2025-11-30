package br.ifsp.scrumou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.ifsp.scrumou.dto.github.GithubRepoDTO;

@Service
public class GithubService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${github.token:}")
    private String githubToken;

    private static final String GITHUB_REPO_URL = "https://api.github.com/repos/%s/%s";

    public GithubRepoDTO getRepository(String owner, String repo) {
        String url = String.format(GITHUB_REPO_URL, owner, repo);

        HttpHeaders headers = new HttpHeaders();
        if (githubToken != null && !githubToken.isBlank()) {
            headers.setBearerAuth(githubToken.trim());
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<GithubRepoDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, GithubRepoDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        }
    }
}
