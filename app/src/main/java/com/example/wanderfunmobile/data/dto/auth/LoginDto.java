package com.example.wanderfunmobile.data.dto.auth;

public class LoginDto {
    private String email;
    private String password;

    public LoginDto() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
