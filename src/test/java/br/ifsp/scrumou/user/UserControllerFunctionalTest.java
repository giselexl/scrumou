package br.ifsp.scrumou.user;

import br.ifsp.scrumou.dto.user.UserRequestDTO;
import br.ifsp.scrumou.model.User;
import br.ifsp.scrumou.model.User.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerFunctionalTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private UserRequestDTO validUserDTO;
    
    @BeforeEach
    public void setUp() {
        validUserDTO = new UserRequestDTO();
        validUserDTO.setName("João Silva");
        validUserDTO.setEmail("joao@example.com");
        validUserDTO.setToken("validtoken123456");
        validUserDTO.setUserType(UserType.DEVELOPER);
    }
    
    @Test
    public void testCreateUserSuccess() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@example.com"))
                .andExpect(jsonPath("$.userType").value("DEVELOPER"));
    }
    
    @Test
    public void testCreateUserWithInvalidEmail() throws Exception {
        validUserDTO.setEmail("invalid-email");
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testCreateUserWithShortName() throws Exception {
        validUserDTO.setName("Jo");
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testCreateUserWithShortToken() throws Exception {
        validUserDTO.setToken("short");
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testCreateUserWithNullName() throws Exception {
        validUserDTO.setName(null);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 99999L))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", instanceOf(java.util.List.class)));
    }
    
    @Test
    public void testGetUserByEmailNotFound() throws Exception {
        mockMvc.perform(get("/api/users/email/{email}", "notfound@example.com"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetUserByTokenNotFound() throws Exception {
        mockMvc.perform(get("/api/users/token/{token}", "notfoundtoken"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testUpdateUserNotFound() throws Exception {
        mockMvc.perform(put("/api/users/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 99999L))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testCreateUserWithPMType() throws Exception {
        validUserDTO.setUserType(UserType.PM);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userType").value("PM"));
    }
    
    @Test
    public void testCreateUserWithNullEmail() throws Exception {
        validUserDTO.setEmail(null);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testCreateUserWithNullToken() throws Exception {
        validUserDTO.setToken(null);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testCreateUserWithNullUserType() throws Exception {
        validUserDTO.setUserType(null);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isBadRequest());
    }
}

