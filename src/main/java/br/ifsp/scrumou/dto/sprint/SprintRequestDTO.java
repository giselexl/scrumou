package br.ifsp.scrumou.dto.sprint;

import java.time.LocalDate;

import br.ifsp.scrumou.model.Story;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SprintRequestDTO {
    @NotNull(message = "O título não pode ser nulo")
    private String title;

    @NotNull(message = "A data de início não pode ser nula")
    private LocalDate startDate;

    @NotNull(message = "A data de término não pode ser nula")
    private LocalDate endDate;

    private Story story;
}
