package br.ifsp.scrumou.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO add validations for all fields

    @NotNull(message = "O título não pode estar vazio")
    private String title;

    @NotNull(message = "A descrição não pode estar vazia")
    private String description;

    @NotNull(message = "O nível de prioridade não pode estar vazio")
    private Integer priority;

}
