package com.ecse428.flowfinder.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
@Table(name = "specific_class")  // explicit table name
public class SpecificClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // works well with PostgreSQL
    private int id;

    private boolean isDeleted;
    private String location;
    private LocalDate date;
    private int limit;
    private LocalTime start;
    private LocalTime end;

    @ManyToOne
    @JoinColumn(name = "dance_class_id")  // foreign key column in DB
    private DanceClass danceClass;

    @ManyToOne
    @JoinColumn(name = "instructor_id")  // foreign key column in DB
    private Instructor instructor;

    // ---------- Constructors ----------

    public SpecificClass() {
    }

    public SpecificClass(boolean isDeleted, String location, LocalDate date, int limit, LocalTime start, LocalTime end, DanceClass danceClass, Instructor instructor) {
        this.isDeleted = isDeleted;
        this.location = location;
        this.date = date;
        this.limit = limit;
        this.start = start;
        this.end = end;
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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
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
                ", limit=" + limit +
                ", start=" + start +
                ", end=" + end +
                ", danceClass=" + (danceClass != null ? danceClass.getName() : "null") +
                ", instructor=" + (instructor != null ? instructor.getName() : "null") +
                '}';
    }
}
