package br.ifsp.scrumou.dto.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubRepoDTO {
    private String id;
    private String name;
    private String full_name;
    private String description;
    private String html_url;
    private String language;
    private int stargazers_count;
    private int forks_count;
    private int open_issues_count;
}
