package br.ifsp.scrumou.dto.user;

import br.ifsp.scrumou.model.User.UserType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String token;
    private UserType userType;
}
