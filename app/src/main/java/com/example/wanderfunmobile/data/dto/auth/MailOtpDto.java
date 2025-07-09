package com.example.wanderfunmobile.data.dto.auth;

public class MailOtpDto {
    private String email;
    private String otp;

    public MailOtpDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
