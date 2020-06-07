package com.trustsim.simulator.agents;

import java.util.List;

public class TruthAgent extends Agent {

  List<ServiceRequest> jobs;

  public TruthAgent(Graph graph, int id, Integer[] dimensions) {
    super(graph, id, dimensions);
  }

}