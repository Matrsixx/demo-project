package com.example.demo.Auth;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AuthResponse {

    private final String username;
    private final String role;

    public AuthResponse(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
