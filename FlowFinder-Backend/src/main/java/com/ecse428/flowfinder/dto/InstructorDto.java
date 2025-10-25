package com.ecse428.flowfinder.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InstructorDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Bio cannot be empty")
    private String bio;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

     public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
