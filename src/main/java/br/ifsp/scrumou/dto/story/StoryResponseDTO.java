package br.ifsp.scrumou.dto.story;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer priority;
}
