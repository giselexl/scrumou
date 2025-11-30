package br.ifsp.scrumou.controller;

import br.ifsp.scrumou.dto.user.UserRequestDTO;
import br.ifsp.scrumou.dto.user.UserResponseDTO;
import br.ifsp.scrumou.model.User;
import br.ifsp.scrumou.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = modelMapper.map(userRequestDTO, User.class);
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(createdUser, UserResponseDTO.class));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(user.get(), UserResponseDTO.class));
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(user.get(), UserResponseDTO.class));
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/token/{token}")
    public ResponseEntity<UserResponseDTO> getUserByToken(@PathVariable String token) {
        Optional<User> user = userService.getUserByToken(token);
        if (user.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(user.get(), UserResponseDTO.class));
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, 
                                                       @Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            User userDetails = modelMapper.map(userRequestDTO, User.class);
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(modelMapper.map(updatedUser, UserResponseDTO.class));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
