package br.ifsp.scrumou.dto.task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer hourEstimated;
    // mudar para tipo usuário
    private String developer;
    //@Enumerated(EnumType.STRING)
    private String status;
}
