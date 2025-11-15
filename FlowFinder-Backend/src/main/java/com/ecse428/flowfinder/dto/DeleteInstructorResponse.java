package com.ecse428.flowfinder.dto;

public class DeleteInstructorResponse {
    private String email;
    private String message;

    public DeleteInstructorResponse(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }
}
