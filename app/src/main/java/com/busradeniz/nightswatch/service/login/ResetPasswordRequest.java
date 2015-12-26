package com.busradeniz.nightswatch.service.login;

/**
 * Created by busradeniz on 27/12/15.
 */
public class ResetPasswordRequest {
    private String email;
    private String username;


    public ResetPasswordRequest(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
