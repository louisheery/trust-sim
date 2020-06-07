package com.trustsim.simulator.agents;

import java.util.HashMap;

public class Agent {

  Graph graph;
  int id;
  HashMap<String, Integer> trustDimensions;

  Agent(Graph graph, int id, Integer[] dimensions) {
    this.graph = graph;
    this.id = id;

    for (int i = 0; i < dimensions.length; i++) {
      if (dimensions[i] != null) {
      trustDimensions.put(Integer.toString(i), dimensions[i]);
      }
    }
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

  public int getRecommendation(Agent agent) {
    return graph.getIndirectTrustPathway(this, agent);
  }

}