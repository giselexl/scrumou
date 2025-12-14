package br.ifsp.scrumou.model;

import br.ifsp.scrumou.identity.dto.UserResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO add validations for all fields

    @NotNull(message = "O título não pode estar vazio")
    private String title;

    @NotNull(message = "A descrição não pode estar vazia")
    private String description;

    @NotNull(message = "A estimativa de horas não pode estar vazia")
    private Integer hourEstimated;

    @NotNull(message = "O campo não pode estar vazio")
    @Column(name = "developer_id")
    private Long developerId;

    @Transient
    private UserResponse developer;

    @NotNull(message = "O status não pode estar vazio")
    //TODO change to enum type
    //@Enumerated(EnumType.STRING)
    private String status;

    public Long getDeveloperId() {
        return this.developerId;
    }

    public void setDeveloperId(Long developerId) {
        this.developerId = developerId;
    }

    public UserResponse getDeveloper() {
        return this.developer;
    }

    public void setDeveloper(UserResponse developer) {
        this.developer = developer;
    }
}