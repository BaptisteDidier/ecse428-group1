package com.ecse428.flowfinder.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateDanceClassRequest {

    @NotBlank(message = "Privacy must be specified")
    private boolean isPrivate;

    @NotBlank(message = "Class name is required")
    private String name;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "Description is required")
    private String description;
    
    // Getters and Setters
    public boolean getIsPrivate() {return isPrivate;}
    public void setIsPrivate(boolean isPrivate) {this.isPrivate = isPrivate;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getGenre() {return genre;}
    public void setGenre(String genre) {this.genre = genre;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}