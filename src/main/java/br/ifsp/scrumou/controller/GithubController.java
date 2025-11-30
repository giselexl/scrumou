package br.ifsp.scrumou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.scrumou.dto.github.GithubRepoDTO;
import br.ifsp.scrumou.service.GithubService;

@RestController
@RequestMapping("/api/github")
public class GithubController {
    @Autowired
    private GithubService githubService;

    @GetMapping("/repo")
    public ResponseEntity<GithubRepoDTO> getRepo(@RequestParam String owner, @RequestParam String repo) {
        GithubRepoDTO dto = githubService.getRepository(owner, repo);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
