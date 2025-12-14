package br.ifsp.scrumou.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {
    @NotBlank
    @Size(min=3, max=100)
    public String name;

    @NotBlank
    @Email
    public String email;

    @NotBlank
    @Size(min=10)
    public String token;

    @NotBlank
    public String userType;
}