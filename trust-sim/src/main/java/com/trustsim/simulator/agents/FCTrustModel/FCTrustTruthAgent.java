package com.trustsim.simulator.agents.FCTrustModel;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.ServiceRequest;
import com.trustsim.simulator.agents.TruthAgent;

import java.util.List;

public class FCTrustTruthAgent extends FCTrustAgent implements TruthAgent {

  List<ServiceRequest> jobs;

  public FCTrustTruthAgent(Graph graph, int id, Double[] dimensions, Double[] agentPersonalityDimensions) {
    super(graph, id, dimensions, agentPersonalityDimensions);
  }

}