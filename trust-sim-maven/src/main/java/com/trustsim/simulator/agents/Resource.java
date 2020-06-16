package com.trustsim.simulator.agents;

public class Resource {

  int id;
  int lifetime;

  public Resource(int id, int lifetime) {
    this.id = id;
    this.lifetime = lifetime;
  }

  public int getId() {
    return id;
  }
}
