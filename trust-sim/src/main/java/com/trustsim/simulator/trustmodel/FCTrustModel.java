package com.trustsim.simulator.trustmodel;

import com.trustsim.simulator.agents.FCTrustModel.FCTrustAgent;
import com.trustsim.simulator.agents.Graph;

public class FCTrustModel implements TrustModel {

  @Override
  public int calculateTrust(Graph agentGraph, FCTrustAgent agent1, FCTrustAgent agent2) {

    //return agentGraph.getIndirectTrustPathway(agent1, agent2);
    return 1;
  }

  @Override
  public String getName() {
    return "FCTrust";
  }
}
