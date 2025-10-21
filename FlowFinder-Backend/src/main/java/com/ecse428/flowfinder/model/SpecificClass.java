package com.ecse428.flowfinder.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class SpecificClass {

  private int id;
  private boolean isDeleted;
  private String location;
  private LocalDate date;
  private int limit;
  private LocalTime start;
  private LocalTime end;

  private DanceClass danceClass;
  private Instructor instructor;

  public SpecificClass(String aLocation, LocalDate aDate, int aLimit, LocalTime aStart, LocalTime aEnd, DanceClass aClass, Instructor aInstructor) {
    isDeleted = false;
    location = aLocation;
    date = aDate;
    limit = aLimit;
    start = aStart;
    end = aEnd;
    if (!setDanceClass(aClass)) {
      throw new RuntimeException("Unable to create SpecificClass due to aClass");
    }
    if (!setInstructor(aInstructor)) {
      throw new RuntimeException("Unable to create SpecificClass due to aInstructor");
    }
  }

  public boolean setLocation(String aLocation) {
    boolean wasSet = false;
    location = aLocation;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(LocalDate aDate) {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setLimit(int aLimit) {
    boolean wasSet = false;
    limit = aLimit;
    wasSet = true;
    return wasSet;
  }

  public boolean setStart(LocalTime aStart) {
    boolean wasSet = false;
    start = aStart;
    wasSet = true;
    return wasSet;
  }

  public boolean setEnd(LocalTime aEnd) {
    boolean wasSet = false;
    end = aEnd;
    wasSet = true;
    return wasSet;
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

  public void delete() {
    danceClass = null;
    instructor = null;
    isDeleted = true;
  }


  public String toString() {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "isDeleted" + ":" + getIsDeleted()+ "," +
            "location" + ":" + getLocation()+ "," +
            "limit" + ":" + getLimit()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "start" + "=" + (getStart() != null ? !getStart().equals(this)  ? getStart().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "end" + "=" + (getEnd() != null ? !getEnd().equals(this)  ? getEnd().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "class = "+(getClass()!=null?Integer.toHexString(System.identityHashCode(getClass())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "instructor = "+(getInstructor()!=null?Integer.toHexString(System.identityHashCode(getInstructor())):"null");
  }
}