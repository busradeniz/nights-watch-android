package com.busradeniz.nightswatch.service.signup;

/**
 * Created by busradeniz on 27/12/15.
 */
public class SignUpRequest {
    private String email;
    private String username;
    private String password;

    public SignUpRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
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
