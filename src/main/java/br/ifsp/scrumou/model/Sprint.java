package br.ifsp.scrumou.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO add validations for all fields

    @NotNull(message = "O título não pode ser nulo")
    private String title;

    @NotNull(message = "A data de início não pode ser nula")
    private LocalDate startDate;

    @NotNull(message = "A data de término não pode ser nula")
    private LocalDate endDate;

    @OneToOne(cascade = CascadeType.ALL)
    private Story story;
}
