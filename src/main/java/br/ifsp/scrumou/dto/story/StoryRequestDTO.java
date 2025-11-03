package br.ifsp.scrumou.dto.story;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryRequestDTO {

    @NotNull(message = "O título não pode estar vazio")
    private String title;

    @NotNull(message = "A descrição não pode estar vazia")
    private String description;

    @NotNull(message = "O nível de prioridade não pode estar vazio")
    private Integer priority;

}
