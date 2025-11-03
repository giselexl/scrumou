package br.ifsp.scrumou.dto.meeting;

import java.time.LocalDate;

import br.ifsp.scrumou.model.Sprint;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingRequestDTO {
    @NotNull(message = "O título não pode ser nulo")
    private String title;

    @NotNull(message = "A ata não pode ser nula")
    private String minutes;

    @NotNull(message = "A data não pode ser nula")
    private LocalDate date;

    @NotNull(message = "O horário não pode ser nulo")
    private String time;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Uma sprint deve ser associada à reunião")
    private Sprint sprint;
}
