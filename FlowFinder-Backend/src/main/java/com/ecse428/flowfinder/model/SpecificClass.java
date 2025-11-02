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

  public SpecificClass(boolean aIsDeleted, String aLocation, LocalDate aDate, int aLimit, LocalTime aStart,
      LocalTime aEnd, DanceClass aClass, Instructor aInstructor) {
    isDeleted = aIsDeleted;
    location = aLocation;
    date = aDate;
    classLimit = aLimit;
    startTime = aStart;
    endTime = aEnd;
    danceClass = aClass;
    instructor = aInstructor;
  }

  public SpecificClass() {}

  public void setLocation(String aLocation) {
    location = aLocation;
  }

  public void setDate(LocalDate aDate) {
    date = aDate;
  }

  public void setClassLimit(int aLimit) {
    classLimit = aLimit;
  }

  public void setStartTime(LocalTime aStart) {
    startTime = aStart;
  }

  public void setEndTime(LocalTime aEnd) {
    endTime = aEnd;
  }

  public int getId() {
    return id;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public String getLocation() {
    return location;
  }

  public LocalDate getDate() {
    return date;
  }

  public int getClassLimit() {
    return classLimit;
  }

  public LocalTime getStart() {
    return startTime;
  }

  public LocalTime getEnd() {
    return endTime;
  }

  public DanceClass getDanceClass() {
    return danceClass;
  }

  public Instructor getInstructor() {
    return instructor;
  }

  public boolean setDanceClass(DanceClass aNewClass) {
    boolean wasSet = false;
    if (aNewClass != null) {
      danceClass = aNewClass;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setInstructor(Instructor aNewInstructor) {
    boolean wasSet = false;
    if (aNewInstructor != null) {
      instructor = aNewInstructor;
      wasSet = true;
    }
    return wasSet;
  }

  public String toString() {
    return super.toString() + "[" +
        "id" + ":" + getId() + "," +
        "isDeleted" + ":" + getIsDeleted() + "," +
        "location" + ":" + getLocation() + "," +
        "limit" + ":" + getClassLimit() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "date" + "="
        + (getDate() != null ? !getDate().equals(this) ? getDate().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "start" + "="
        + (getStart() != null ? !getStart().equals(this) ? getStart().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "end" + "="
        + (getEnd() != null ? !getEnd().equals(this) ? getEnd().toString().replaceAll("  ", "    ") : "this" : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "class = " + (getClass() != null ? Integer.toHexString(System.identityHashCode(getClass())) : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "instructor = "
        + (getInstructor() != null ? Integer.toHexString(System.identityHashCode(getInstructor())) : "null");
  }
}
