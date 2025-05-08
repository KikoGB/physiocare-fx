package com.gadeadiaz.physiocare.models.auth;

import com.gadeadiaz.physiocare.models.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class Auth Response represents the api response when the user logs in
 */
public class AuthResponse extends BaseResponse {
    private String token;
    private String rol;

    public String getToken() {
        return token;
    }

    public String getRol() {
        return rol;
    }
}
