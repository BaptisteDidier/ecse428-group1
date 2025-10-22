package com.ecse428.flowfinder.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Client")
public class Client extends Person {

  public Client(String aName, String aBio, String aEmail, String aPassword, LocalDate aDate, boolean aIsDeleted) {
    super(aName, aBio, aEmail, aPassword, aDate, aIsDeleted);
  }

}