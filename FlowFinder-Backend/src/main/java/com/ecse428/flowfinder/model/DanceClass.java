package com.ecse428.flowfinder.model;

public class DanceClass {

  private int id;
  private boolean isPrivate;
  private String name;
  private String genre;
  private String description;

  public DanceClass(boolean aIsPrivate, String aName, String aGenre, String aDescription) {
    isPrivate = aIsPrivate;
    name = aName;
    genre = aGenre;
    description = aDescription;
  }

  public boolean setIsPrivate(boolean aIsPrivate) {
    boolean wasSet = false;
    isPrivate = aIsPrivate;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setGenre(String aGenre) {
    boolean wasSet = false;
    genre = aGenre;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription) {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public int getId() {
    return id;
  }

  public boolean getIsPrivate() {
    return isPrivate;
  }

  public String getName() {
    return name;
  }

  public String getGenre() {
    return genre;
  }

  public String getDescription() {
    return description;
  }

  public String toString() {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "isPrivate" + ":" + getIsPrivate()+ "," +
            "name" + ":" + getName()+ "," +
            "genre" + ":" + getGenre()+ "," +
            "description" + ":" + getDescription()+ "]";
  }
}