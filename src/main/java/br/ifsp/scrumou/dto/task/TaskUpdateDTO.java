package br.ifsp.scrumou.dto.task;

public class TaskUpdateDTO {
    private String title;
    private String description;
    private Integer hourEstimated;
    // mudar para tipo usuário
    private String developer;
    //@Enumerated(EnumType.STRING)
    private String status;
}
