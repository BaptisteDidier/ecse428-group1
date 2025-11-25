package com.ecse428.flowfinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateDanceClassRequest {

    @NotNull(message = "Class type is required")
    private Boolean isPrivate;

    @NotBlank(message = "Class name is required")
    private String name;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "Description is required")
    private String description;
    
    // Getters and Setters
    public Boolean getIsPrivate() {return isPrivate;}
    public void setIsPrivate(Boolean isPrivate) {this.isPrivate = isPrivate;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getGenre() {return genre;}
    public void setGenre(String genre) {this.genre = genre;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}