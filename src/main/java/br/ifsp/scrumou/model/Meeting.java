package br.ifsp.scrumou.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
