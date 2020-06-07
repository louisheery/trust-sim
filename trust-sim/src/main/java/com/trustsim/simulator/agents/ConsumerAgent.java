package com.trustsim.simulator.agents;

import java.util.List;

public class ConsumerAgent extends Agent {

  List<ServiceRequest> requests;

  public ConsumerAgent(Graph graph, int id, Integer[] dimensions) {
    super(graph, id, dimensions);
  }

  public void assignServiceRequest(ServiceRequest request) {
    requests.add(request);
  }

}
