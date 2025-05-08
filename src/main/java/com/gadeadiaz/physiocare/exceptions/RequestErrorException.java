package com.gadeadiaz.physiocare.exceptions;

import com.gadeadiaz.physiocare.models.ErrorResponse;

public class RequestErrorException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public RequestErrorException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
