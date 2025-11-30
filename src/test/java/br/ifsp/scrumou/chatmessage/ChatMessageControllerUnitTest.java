package br.ifsp.scrumou.chatmessage;

import br.ifsp.scrumou.controller.ChatMessageController;
import br.ifsp.scrumou.service.KafkaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ChatMessageControllerUnitTest {

    @Mock
    private KafkaService kafkaService;

    @InjectMocks
    private ChatMessageController chatMessageController;

    @Test
    public void testSendMessage_WithValidParams() {
        String key = "key1";
        String sender = "testUser";
        String receiver = "receiverUser";
        String content = "Hello, World!";

        chatMessageController.sendMessage(key, sender, receiver, content);

        verify(kafkaService, times(1)).sendMessage(key, sender, receiver, content);
    }

    @Test
    public void testSendMessage_VerifyServiceIsCalled() {
        String key = "msg-123";
        String sender = "alice";
        String receiver = "bob";
        String content = "Test message";

        chatMessageController.sendMessage(key, sender, receiver, content);

        verify(kafkaService, times(1)).sendMessage(key, sender, receiver, content);
    }

    @Test
    public void testSendMessage_WithSpecialCharacters() {
        String key = "key1";
        String sender = "user1";
        String receiver = "user2";
        String specialContent = "Message with special chars: !@#$%^&*()";

        chatMessageController.sendMessage(key, sender, receiver, specialContent);

        verify(kafkaService, times(1)).sendMessage(key, sender, receiver, specialContent);
    }

    @Test
    public void testSendMessage_MultipleDifferentMessages() {
        chatMessageController.sendMessage("key1", "alice", "bob", "Hello from Alice");
        chatMessageController.sendMessage("key2", "bob", "alice", "Hello from Bob");

        verify(kafkaService, times(1)).sendMessage("key1", "alice", "bob", "Hello from Alice");
        verify(kafkaService, times(1)).sendMessage("key2", "bob", "alice", "Hello from Bob");
    }

    @Test
    public void testSendMessage_WithLongContent() {
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longContent.append("a");
        }
        String content = longContent.toString();

        chatMessageController.sendMessage("key1", "sender", "receiver", content);

        verify(kafkaService, times(1)).sendMessage("key1", "sender", "receiver", content);
    }

    @Test
    public void testSendMessage_WithEmptyContent() {
        chatMessageController.sendMessage("key1", "sender", "receiver", "");

        verify(kafkaService, times(1)).sendMessage("key1", "sender", "receiver", "");
    }
}
