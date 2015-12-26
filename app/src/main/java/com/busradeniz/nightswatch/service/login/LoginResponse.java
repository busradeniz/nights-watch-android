package com.busradeniz.nightswatch.service.login;

import com.busradeniz.nightswatch.service.Response;

/**
 * Created by busradeniz on 26/12/15.
 */
public class LoginResponse extends Response {



    private String token ;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
