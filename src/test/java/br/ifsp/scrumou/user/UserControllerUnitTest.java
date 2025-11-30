package br.ifsp.scrumou.user;

import br.ifsp.scrumou.controller.UserController;
import br.ifsp.scrumou.dto.user.UserRequestDTO;
import br.ifsp.scrumou.dto.user.UserResponseDTO;
import br.ifsp.scrumou.model.User;
import br.ifsp.scrumou.model.User.UserType;
import br.ifsp.scrumou.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserControllerUnitTest {
    
    @Mock
    private UserService userService;
    
    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private UserController userController;
    
    private User user;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new User();
        user.setId(1L);
        user.setName("João Silva");
        user.setEmail("joao@example.com");
        user.setToken("token123456");
        user.setUserType(UserType.DEVELOPER);
        
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("João Silva");
        userRequestDTO.setEmail("joao@example.com");
        userRequestDTO.setToken("token123456");
        userRequestDTO.setUserType(UserType.DEVELOPER);
        
        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setName("João Silva");
        userResponseDTO.setEmail("joao@example.com");
        userResponseDTO.setToken("token123456");
        userResponseDTO.setUserType(UserType.DEVELOPER);
    }
    
    @Test
    public void testCreateUserSuccess() {
        when(modelMapper.map(userRequestDTO, User.class)).thenReturn(user);
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);
        
        ResponseEntity<UserResponseDTO> response = userController.createUser(userRequestDTO);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().getName());
        verify(userService, times(1)).createUser(any(User.class));
    }
    
    @Test
    public void testGetUserByIdSuccess() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);
        
        ResponseEntity<UserResponseDTO> response = userController.getUserById(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(userService, times(1)).getUserById(1L);
    }
    
    @Test
    public void testGetUserByIdNotFound() {
        when(userService.getUserById(99999L)).thenReturn(Optional.empty());
        
        ResponseEntity<UserResponseDTO> response = userController.getUserById(99999L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUserById(99999L);
    }
    
    @Test
    public void testGetAllUsersSuccess() {
        List<User> users = new ArrayList<>();
        users.add(user);
        
        when(userService.getAllUsers()).thenReturn(users);
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);
        
        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }
    
    @Test
    public void testGetAllUsersEmpty() {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        
        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }
    
    @Test
    public void testGetUserByEmailSuccess() {
        when(userService.getUserByEmail("joao@example.com")).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);
        
        ResponseEntity<UserResponseDTO> response = userController.getUserByEmail("joao@example.com");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("joao@example.com", response.getBody().getEmail());
        verify(userService, times(1)).getUserByEmail("joao@example.com");
    }
    
    @Test
    public void testGetUserByEmailNotFound() {
        when(userService.getUserByEmail("notfound@example.com")).thenReturn(Optional.empty());
        
        ResponseEntity<UserResponseDTO> response = userController.getUserByEmail("notfound@example.com");
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUserByEmail("notfound@example.com");
    }
    
    @Test
    public void testGetUserByTokenSuccess() {
        when(userService.getUserByToken("token123456")).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);
        
        ResponseEntity<UserResponseDTO> response = userController.getUserByToken("token123456");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token123456", response.getBody().getToken());
        verify(userService, times(1)).getUserByToken("token123456");
    }
    
    @Test
    public void testGetUserByTokenNotFound() {
        when(userService.getUserByToken("notfoundtoken")).thenReturn(Optional.empty());
        
        ResponseEntity<UserResponseDTO> response = userController.getUserByToken("notfoundtoken");
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUserByToken("notfoundtoken");
    }
    
    @Test
    public void testUpdateUserSuccess() {
        when(modelMapper.map(userRequestDTO, User.class)).thenReturn(user);
        when(userService.updateUser(1L, user)).thenReturn(user);
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);
        
        ResponseEntity<UserResponseDTO> response = userController.updateUser(1L, userRequestDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }
    
    @Test
    public void testUpdateUserNotFound() {
        when(modelMapper.map(userRequestDTO, User.class)).thenReturn(user);
        when(userService.updateUser(99999L, user))
                .thenThrow(new RuntimeException("User not found with id: 99999"));
        
        ResponseEntity<UserResponseDTO> response = userController.updateUser(99999L, userRequestDTO);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).updateUser(eq(99999L), any(User.class));
    }
    
    @Test
    public void testDeleteUserSuccess() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userService).deleteUser(1L);
        
        ResponseEntity<Void> response = userController.deleteUser(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }
    
    @Test
    public void testDeleteUserNotFound() {
        when(userService.getUserById(99999L)).thenReturn(Optional.empty());
        
        ResponseEntity<Void> response = userController.deleteUser(99999L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).deleteUser(99999L);
    }
}
