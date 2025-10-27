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
  private int limit;
  private LocalTime start;
  private LocalTime end;

  @ManyToOne
  private DanceClass danceClass;
  @ManyToOne
  private Instructor instructor;

  public SpecificClass(boolean aIsDeleted, String aLocation, LocalDate aDate, int aLimit, LocalTime aStart,
      LocalTime aEnd, DanceClass aClass, Instructor aInstructor) {
    validateLimit(aLimit);
    validateStartAndEndTime(aStart, aEnd);
    validateDateAndTime(aDate, aStart, aEnd);
    validateLocation(aLocation);

    if (aClass == null || aInstructor == null) {
      throw new IllegalArgumentException("DanceClass and Instructor must not be null");
    }

    isDeleted = aIsDeleted;
    location = aLocation;
    date = aDate;
    limit = aLimit;
    start = aStart;
    end = aEnd;
    danceClass = aClass;
    instructor = aInstructor;
  }

  public void setLocation(String aLocation) {
    validateLocation(aLocation);
    location = aLocation;
  }

  public void setDate(LocalDate aDate) {
    validateDateAndTime(aDate, this.start, this.end);
    date = aDate;
  }

  public void setLimit(int aLimit) {
    validateLimit(aLimit);
    limit = aLimit;
  }

  public void setStart(LocalTime aStart) {
    validateDateAndTime(this.date, aStart, this.end);
    start = aStart;
  }

  public void setEnd(LocalTime aEnd) {
    validateDateAndTime(this.date, this.start, aEnd);
    end = aEnd;
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

  public int getLimit() {
    return limit;
  }

  public LocalTime getStart() {
    return start;
  }

  public LocalTime getEnd() {
    return end;
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

  private void validateLimit(int aLimit) {
    if (aLimit <= 0) {
      throw new IllegalArgumentException("Limit must be positive");
    }
  }

  private void validateDateAndTime(LocalDate date, LocalTime start, LocalTime end) {
    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();

    if (date == null) {
      throw new IllegalArgumentException("Date cannot be null");
    }

    if (date.isBefore(today)) {
      throw new IllegalArgumentException("Date of class cannot be in the past");
    }
    validateStartAndEndTime(start, end);

    if (date.isEqual(today)) {
      if (!start.isAfter(now) || !end.isAfter(now)) {
        throw new IllegalArgumentException("Class start and end time must be after current time");
      }

    }
  }

  public void validateStartAndEndTime(LocalTime start, LocalTime end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Start and end times cannot be null");
    }

    if (!end.isAfter(start)) {
      throw new IllegalArgumentException("End time cannot be equal or before start time");
    }
  }

  public void validateLocation(String aLocation) {
    if (aLocation == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    if (aLocation.trim().isEmpty()) {
      throw new IllegalArgumentException("Location cannot be empty");
    }
  }

  public String toString() {
    return super.toString() + "[" +
        "id" + ":" + getId() + "," +
        "isDeleted" + ":" + getIsDeleted() + "," +
        "location" + ":" + getLocation() + "," +
        "limit" + ":" + getLimit() + "]" + System.getProperties().getProperty("line.separator") +
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