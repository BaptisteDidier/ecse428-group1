package com.ecse428.flowfinder.model;

public class Registration {

  private Client participant;
  private SpecificClass danceClass;

  public Registration(Client aParticipant, SpecificClass aClass) {
    if (!setParticipant(aParticipant)) {
      throw new RuntimeException("Unable to create Registration due to aParticipant");
    }
    if (!setClass(aClass)) {
      throw new RuntimeException("Unable to create Registration due to aClass");
    }
  }

  public Client getParticipant() {
    return participant;
  }

  public SpecificClass getDanceClass() {
    return danceClass;
  }

  public boolean setParticipant(Client aNewParticipant) {
    boolean wasSet = false;
    if (aNewParticipant != null) {
      participant = aNewParticipant;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setClass(SpecificClass aNewClass) {
    boolean wasSet = false;
    if (aNewClass != null) {
      danceClass = aNewClass;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    participant = null;
    danceClass = null;
  }

}