package br.ifsp.scrumou.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageResponseDTO {
    private String sender;
    private String content;
}
