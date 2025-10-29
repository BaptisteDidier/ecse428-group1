package com.ecse428.flowfinder.dto;

import com.ecse428.flowfinder.model.DanceClass;

public class DanceClassResponse {
    private int id;
    private boolean isPrivate;
    private String name;
    private String genre;
    private String description;

    public static DanceClassResponse from(DanceClass danceClass) {
        DanceClassResponse response = new DanceClassResponse();
        response.id = danceClass.getId();
        response.isPrivate = danceClass.getIsPrivate();
        response.name = danceClass.getName();
        response.genre = danceClass.getGenre();
        response.description = danceClass.getDescription();
        return response;
    }

    // Getters
    public int getId() {return id;}
    public boolean getIsPrivate() {return isPrivate;}
    public String getName() {return name;}
    public String getGenre() {return genre;}
    public String getDescription() {return description;}
    
}