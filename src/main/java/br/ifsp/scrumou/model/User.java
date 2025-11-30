package br.ifsp.scrumou.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome não pode estar vazio")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotNull(message = "O email não pode estar vazio")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotNull(message = "O token não pode estar vazio")
    @Size(min = 10, message = "O token deve ter no mínimo 10 caracteres")
    private String token;

    @NotNull(message = "O tipo de usuário não pode estar vazio")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public enum UserType {
        DEVELOPER("Desenvolvedor"),
        PM("Gerente de Projeto");

        private final String description;

        UserType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
