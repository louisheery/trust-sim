package com.trustsim.simulator.subclasses;

public class Agent {

  int id;

  Agent(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public boolean equals(Agent other) {
    return this.id == other.getId();
  }

  public int hashCode() {
    final int primeNum = 11;
    int output = 1;
    output = (primeNum * output) + id;
    return output;
  }

}