package com.trustsim.simulator.trustmodel;

import com.trustsim.simulator.agents.Agent;
import com.trustsim.simulator.agents.Graph;

public class EigenTrustModel implements TrustModelInterface {

  @Override
  public int calculateTrust(Graph agentGraph, Agent agent1, Agent agent2) {

    return agentGraph.getIndirectTrustPathway(agent1, agent2);

  }
}
