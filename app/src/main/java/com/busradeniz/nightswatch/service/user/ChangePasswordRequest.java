package com.busradeniz.nightswatch.service.user;

/**
 * Created by busradeniz on 27/12/15.
 */
public class ChangePasswordRequest {

    private String newPassword;
    private String oldPassword;

    public ChangePasswordRequest(String newPassword, String oldPassword) {
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
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
