package com.example.wanderfunmobile.data.dto.auth;

public class ChangePasswordDto {
    private String newPassword;
    private String oldPassword;

    public ChangePasswordDto() {
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
