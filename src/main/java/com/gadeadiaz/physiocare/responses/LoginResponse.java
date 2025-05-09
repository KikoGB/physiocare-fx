package com.gadeadiaz.physiocare.responses;

/**
 * Class Auth Response represents the api response when the user logs in
 */
public class LoginResponse {
    private String token;
    private String rol;

    public String getToken() {
        return token;
    }

    public String getRol() {
        return rol;
    }
}
