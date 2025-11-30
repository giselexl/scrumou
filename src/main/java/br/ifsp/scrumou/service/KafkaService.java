package br.ifsp.scrumou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.ifsp.scrumou.model.ChatMessage;

@Service
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;

    @Value(value = "${message.topic.private.name}")
    private String privateTopicName;

    public void sendMessage(String key, String sender, String receiver, String content) {
        kafkaTemplate.send(privateTopicName, key, ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build());
    }

    @KafkaListener(topics = "${message.topic.private.name}", containerFactory = "mobileContainerFactory")
    public void listenerMobile(ChatMessage message) {
        System.out.println(message.getSender() + " : " + message.getContent());
    }
}
