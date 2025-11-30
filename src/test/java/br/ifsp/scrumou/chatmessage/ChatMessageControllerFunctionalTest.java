package br.ifsp.scrumou.chatmessage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatMessageControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldSendMessageWithRequestParams() throws Exception {
        mockMvc.perform(post("/api/chat/sendMessage")
                .param("key", "msg-001")
                .param("sender", "user1")
                .param("receiver", "user2")
                .param("content", "Hello from user1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSendMessageWithAllParams() throws Exception {
        mockMvc.perform(post("/api/chat/sendMessage")
                .param("key", "msg-002")
                .param("sender", "alice")
                .param("receiver", "bob")
                .param("content", "This is a test message"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSendMessageWithEmptyContent() throws Exception {
        mockMvc.perform(post("/api/chat/sendMessage")
                .param("key", "msg-003")
                .param("sender", "user1")
                .param("receiver", "user2")
                .param("content", ""))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnServerErrorWhenMissingRequiredParam() throws Exception {
        mockMvc.perform(post("/api/chat/sendMessage")
                .param("key", "msg-004")
                .param("sender", "user1")
                .param("receiver", "user2"))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldReturnServerErrorWhenMissingSender() throws Exception {
        mockMvc.perform(post("/api/chat/sendMessage")
                .param("key", "msg-005")
                .param("receiver", "user2")
                .param("content", "Message without sender"))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldHandleLongMessageContent() throws Exception {
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longContent.append("a");
        }

        mockMvc.perform(post("/api/chat/sendMessage")
                .param("key", "msg-006")
                .param("sender", "user1")
                .param("receiver", "user2")
                .param("content", longContent.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldHandleMultipleConsecutiveMessages() throws Exception {
        mockMvc.perform(post("/api/chat/sendMessage")
                .param("key", "msg-007")
                .param("sender", "user1")
                .param("receiver", "user2")
                .param("content", "First message"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/chat/sendMessage")
                .param("key", "msg-008")
                .param("sender", "user2")
                .param("receiver", "user1")
                .param("content", "Second message"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSendMessageToKafkaTopic() throws Exception {
        mockMvc.perform(post("/api/chat/sendMessage")
                .param("key", "broadcast-001")
                .param("sender", "broadcaster")
                .param("receiver", "all")
                .param("content", "Broadcasting to all subscribers"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
