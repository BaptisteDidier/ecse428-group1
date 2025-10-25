package com.ecse428.flowfinder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public class FlowFinderException extends RuntimeException {
    @NonNull
    private HttpStatus status;

    public FlowFinderException(@NonNull HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}
