package com.ecse428.flowfinder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CreateInstructorRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String bio; // optional
    private Boolean isDeleted;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    // must contain at least one element (validated in service to include good message)
    private List<Long> specificClassIds;

    //getters & setters
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getBio() { return bio; }
    public void setBio(String b) { this.bio = b; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
    public List<Long> getSpecificClassIds() { return specificClassIds; }
    public void setSpecificClassIds(List<Long> ids) { this.specificClassIds = ids; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean d) { this.isDeleted = d; }
}
