package com.example.wanderfunmobile.data.dto.auth;

public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDto() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
