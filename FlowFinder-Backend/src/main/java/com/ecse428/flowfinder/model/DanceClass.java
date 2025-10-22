package com.ecse428.flowfinder.model;

import jakarta.persistence.*;

@Entity
public class DanceClass {

  @Id
  @GeneratedValue
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

  public void setIsPrivate(boolean aIsPrivate) {
    isPrivate = aIsPrivate;
  }

  public void setName(String aName) {
    name = aName;
  }

  public void setGenre(String aGenre) {
    genre = aGenre;
  }

  public void setDescription(String aDescription) {
    description = aDescription;
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