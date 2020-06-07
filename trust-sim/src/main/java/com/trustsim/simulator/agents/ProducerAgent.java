package com.trustsim.simulator.agents;

import java.util.List;

public class ProducerAgent extends Agent {

  List<ServiceRequest> jobs;

  public ProducerAgent(Graph graph, int id, Integer[] dimensions) {
    super(graph, id, dimensions);
  }

  public void requestService(ServiceRequest request) {
    jobs.add(request);
  }

}