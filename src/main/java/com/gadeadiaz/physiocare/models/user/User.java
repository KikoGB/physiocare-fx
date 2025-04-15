package com.gadeadiaz.physiocare.models.user;

import com.google.gson.annotations.SerializedName;

/**
 *  Class User represents a user entity with authentication and identification details.
 */
public class User {
    @SerializedName("_id")
    private String id;
    private String token;
    private String login;
    private String email;
    private String password;
    private String rol;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
