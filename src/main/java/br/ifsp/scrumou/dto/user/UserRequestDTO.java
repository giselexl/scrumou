package br.ifsp.scrumou.dto.user;

import br.ifsp.scrumou.model.User.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {
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
    private UserType userType;
}
