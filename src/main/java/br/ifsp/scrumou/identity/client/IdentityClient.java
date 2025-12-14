package br.ifsp.scrumou.identity.client;

import br.ifsp.scrumou.identity.dto.UserRequest;
import br.ifsp.scrumou.identity.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "identityClient", url = "http://localhost:8081")
public interface IdentityClient {
    @PostMapping("/users")
    UserResponse createUser(@RequestBody UserRequest request);

    @GetMapping("/users")
    UserResponse getUsers();

    @GetMapping("/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);

    @GetMapping("/users/email/{email}")
    UserResponse getUserByEmail(@PathVariable("email") String email);

}