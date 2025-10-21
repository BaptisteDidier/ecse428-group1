package com.ecse428.flowfinder.model;

import java.util.*;
import java.time.LocalDate;

public class Person {

  private static Map<String, Person> personsByEmail = new HashMap<String, Person>();

  private int id;
  private String name;
  private String bio;
  private String email;
  private String password;
  private LocalDate creationDate;
  private boolean isDeleted;

  public Person(String aEmail, String aPassword) {
    name = null;
    bio = null;
    password = aPassword;
    creationDate = LocalDate.now();
    isDeleted = false;
    if (!setEmail(aEmail)) {
      throw new RuntimeException("Cannot create due to duplicate email");
    }
  }

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setBio(String aBio) {
    boolean wasSet = false;
    bio = aBio;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail) {
    boolean wasSet = false;
    String anOldEmail = getEmail();
    if (anOldEmail != null && anOldEmail.equals(aEmail)) {
      return true;
    }
    if (hasWithEmail(aEmail)) {
      return wasSet;
    }
    email = aEmail;
    wasSet = true;
    if (anOldEmail != null) {
      personsByEmail.remove(anOldEmail);
    }
    personsByEmail.put(aEmail, this);
    return wasSet;
  }

  public boolean setPassword(String aPassword) {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setCreationDate(LocalDate aCreationDate) {
    boolean wasSet = false;
    creationDate = aCreationDate;
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    isDeleted = true;
    personsByEmail.remove(getEmail());
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

  public static Person getWithEmail(String aEmail) {
    return personsByEmail.get(aEmail);
  }

  public static boolean hasWithEmail(String aEmail) {
    return getWithEmail(aEmail) != null;
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