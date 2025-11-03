package br.ifsp.scrumou.dto.task;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDTO {
    @NotNull(message = "O título não pode estar vazio")
    private String title;

    @NotNull(message = "A descrição não pode estar vazia")
    private String description;

    @NotNull(message = "A estimativa de horas não pode estar vazia")
    private Integer hourEstimated;

    @NotNull(message = "O campo não pode estar vazio")
    // mudar para tipo usuário
    private String developer;

    @NotNull(message = "O status não pode estar vazio")
    //@Enumerated(EnumType.STRING)
    private String status;
}
