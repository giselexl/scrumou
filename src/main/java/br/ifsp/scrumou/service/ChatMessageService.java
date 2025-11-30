package br.ifsp.scrumou.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import br.ifsp.scrumou.dto.chat.ChatMessageRequestDTO;
import br.ifsp.scrumou.dto.chat.ChatMessageResponseDTO;

@Service
public class ChatMessageService {
    private final ModelMapper modelMapper;

    @Autowired
    public ChatMessageService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ChatMessageResponseDTO registerSession(@Payload ChatMessageRequestDTO chatMessageRequestDTO, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessageRequestDTO.getSender());
        return modelMapper.map(chatMessageRequestDTO, ChatMessageResponseDTO.class);
    }

    public ChatMessageResponseDTO sendMessage(@Payload ChatMessageRequestDTO chatMessageRequestDTO) {
        return modelMapper.map(chatMessageRequestDTO, ChatMessageResponseDTO.class);
    }
}
