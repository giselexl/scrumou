package br.ifsp.scrumou.dto.meeting;

import java.time.LocalDate;

import br.ifsp.scrumou.model.Sprint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingResponseDTO {
    private Long id;
    private String title;
    private String minutes;
    private LocalDate date;
    private String time;
    private Sprint sprint;
}
