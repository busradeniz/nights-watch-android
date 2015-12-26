package com.busradeniz.nightswatch.service.login;

/**
 * Created by busradeniz on 26/12/15.
 */
public class LoginRequest {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
