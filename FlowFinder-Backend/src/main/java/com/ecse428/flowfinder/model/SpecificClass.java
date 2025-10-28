package com.ecse428.flowfinder.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
public class SpecificClass {

    @Id
    @GeneratedValue
    private int id;
    private boolean isDeleted;
    private String location;
    private LocalDate date;
    private int classLimit;
    private LocalTime startTime;
    private LocalTime endTime;
    @ManyToOne
    private DanceClass danceClass;
    @ManyToOne
    private Instructor instructor;

    // ---------- Constructors ----------

    public SpecificClass() {
    }

    public SpecificClass(boolean isDeleted, String location, LocalDate date, int limit, LocalTime start, LocalTime end, DanceClass danceClass, Instructor instructor) {
        this.isDeleted = isDeleted;
        this.location = location;
        this.date = date;
        this.classLimit = limit;
        this.startTime = start;
        this.endTime = end;
        this.danceClass = danceClass;
        this.instructor = instructor;
    }

    // ---------- Getters & Setters ----------

    public int getId() {
        return id;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getClassLimit() {
        return classLimit;
    }

    public void setClassLimit(int limit) {
        this.classLimit = limit;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime start) {
        this.startTime = start;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime end) {
        this.endTime = end;
    }

    public DanceClass getDanceClass() {
        return danceClass;
    }

    public void setDanceClass(DanceClass danceClass) {
        this.danceClass = danceClass;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    // ---------- toString ----------
    @Override
    public String toString() {
        return "SpecificClass{" +
                "id=" + id +
                ", isDeleted=" + isDeleted +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", limit=" + classLimit +
                ", start=" + startTime +
                ", end=" + endTime +
                ", danceClass=" + (danceClass != null ? danceClass.getName() : "null") +
                ", instructor=" + (instructor != null ? instructor.getName() : "null") +
                '}';
    }
}
