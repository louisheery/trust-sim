package com.trustsim.simulator.agents.WangTrustModel;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.ServiceRequest;
import com.trustsim.simulator.agents.TruthAgent;

import java.util.List;

public class WangTrustTruthAgent extends WangTrustAgent implements TruthAgent {

  List<ServiceRequest> jobs;

  public WangTrustTruthAgent(Graph graph, int id, List<Double> dimensions, List<Double> agentPersonalityDimensions) {
    super(graph, id, dimensions, agentPersonalityDimensions);
  }

}