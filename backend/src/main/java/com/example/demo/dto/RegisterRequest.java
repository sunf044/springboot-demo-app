package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank
    public String name;

    @Email
    @NotBlank
    public String email;

    @Size(min = 6)
    public String password;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public CharSequence getPassword() {
        return password;
    }
}
