package br.ifsp.scrumou.dto.sprint;

import java.time.LocalDate;

import br.ifsp.scrumou.model.Story;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SprintResponseDTO {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Story story;
}
