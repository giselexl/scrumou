package br.ifsp.scrumou.dto.sprint;

import java.time.LocalDate;

import br.ifsp.scrumou.model.Story;

public class SprintUpdateDTO {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Story story;
}
