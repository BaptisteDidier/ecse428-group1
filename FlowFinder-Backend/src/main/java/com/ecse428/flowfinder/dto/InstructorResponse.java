package com.ecse428.flowfinder.dto;

import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;

import java.time.LocalDate;
import java.util.List;

public class InstructorResponse {
    private int id;
    private String name;
    private String bio;
    private String email;
    private LocalDate creationDate;
    private boolean isDeleted;
    private List<Integer> assignedSpecificClassIds;

    public static InstructorResponse from(Instructor i, List<SpecificClass> classes) {
        InstructorResponse r = new InstructorResponse();
        r.id = i.getId();
        r.name = i.getName();
        r.bio = i.getBio();
        r.email = i.getEmail();
        r.creationDate = i.getCreationDate();
        r.isDeleted = Boolean.TRUE.equals(i.getIsDeleted());
        r.assignedSpecificClassIds = classes.stream().map(SpecificClass::getId).toList();
        return r;
    }

    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBio() { return bio; }
    public String getEmail() { return email; }
    public LocalDate getCreationDate() { return creationDate; }
    public boolean isDeleted() { return isDeleted; }
    public List<Integer> getAssignedSpecificClassIds() { return assignedSpecificClassIds; }
}