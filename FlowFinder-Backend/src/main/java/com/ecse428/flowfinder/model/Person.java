package com.ecse428.flowfinder.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Person {

  public Person() {
   
  }

  @Id
  @GeneratedValue
  private int id;
  private String name;
  private String bio;
  private String email;
  private String password;
  private LocalDate creationDate;
  private boolean isDeleted;

  public Person(String aName, String aBio, String aEmail, String aPassword, LocalDate aDate, boolean aIsDeleted) {
    name = aName;
    bio = aBio;
    email = aEmail;
    password = aPassword;
    creationDate = aDate;
    isDeleted = aIsDeleted;
  }

  public void setName(String aName) {
    name = aName;
  }

  public void setBio(String aBio) {
    bio = aBio;
  }

  public void setEmail(String aEmail) {
    email = aEmail;
  }

  public void setPassword(String aPassword) {
    password = aPassword;
  }

  public void setCreationDate(LocalDate aCreationDate) {
    creationDate = aCreationDate;
  }

  public void setIsDeleted(boolean aIsDeleted) {
    isDeleted = aIsDeleted;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getBio() {
    return bio;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public String toString() {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "name" + ":" + getName()+ "," +
            "bio" + ":" + getBio()+ "," +
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "," +
            "isDeleted" + ":" + getIsDeleted()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "creationDate" + "=" + (getCreationDate() != null ? !getCreationDate().equals(this)  ? getCreationDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}