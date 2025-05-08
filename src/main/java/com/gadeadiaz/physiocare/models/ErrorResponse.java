package com.gadeadiaz.physiocare.models;

public class ErrorResponse {
    private String message;
    private String error;
    private int statusCode;

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return message + " - " + error + " - " + statusCode;
    }
}
