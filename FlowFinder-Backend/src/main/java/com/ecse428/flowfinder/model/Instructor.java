package com.ecse428.flowfinder.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Instructor")
public class Instructor extends Person {

  public Instructor(String aName, String aBio, String aEmail, String aPassword, LocalDate aDate, boolean aIsDeleted) {
    super(aName, aBio, aEmail, aPassword, aDate, aIsDeleted);
  }
  public Instructor() {
    super();
  }

}