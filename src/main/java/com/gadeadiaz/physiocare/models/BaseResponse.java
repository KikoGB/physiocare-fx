package com.gadeadiaz.physiocare.models;

/**
 * Class BaseResponse represents a base response that is common across various API responses.
 */
public class BaseResponse {
    private boolean ok;
    private String error;


    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
