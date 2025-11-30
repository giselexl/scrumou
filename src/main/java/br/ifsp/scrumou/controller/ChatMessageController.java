package br.ifsp.scrumou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.scrumou.service.KafkaService;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {
    @Autowired
    private KafkaService kafkaService;

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestParam String key, @RequestParam String sender, @RequestParam String receiver, @RequestParam String content) {
        kafkaService.sendMessage(key, sender, receiver, content);
    }
}
