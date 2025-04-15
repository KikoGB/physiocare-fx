package com.gadeadiaz.physiocare.models.auth;

import com.google.gson.annotations.Expose;

/***
 * Class Login Request represents the body of the request that the api needs to process the login request
 */
public class LoginRequest {

    @Expose
    private String login;
    @Expose
    private String password;

    public LoginRequest(String username, String password) {
        this.login = username;
        this.password = password;
    }
}
