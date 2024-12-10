package com.example.wanderfunmobile.network.dto.auth;

import com.example.wanderfunmobile.domain.model.enums.UserRole;

public class LoginResponseDto {
    private Long id;
    private String email;
    private UserRole role;
    private final String tokenType;
    private String accessToken;
    private String refreshToken;

    public LoginResponseDto() {
        tokenType = "Bearer";
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
