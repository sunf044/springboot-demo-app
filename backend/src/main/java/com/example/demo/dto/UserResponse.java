package com.example.demo.dto;

public class UserResponse {
    public Long id;
    public String name;
    public String email;

    public UserResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
