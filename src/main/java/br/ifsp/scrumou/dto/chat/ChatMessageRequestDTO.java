package br.ifsp.scrumou.dto.chat;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequestDTO {
    private String sender;

    @NotEmpty(message = "A mensagem não pode ser vazia")
    private String content;
}
