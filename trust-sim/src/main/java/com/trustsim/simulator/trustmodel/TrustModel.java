package com.trustsim.simulator.trustmodel;

import com.trustsim.simulator.agents.Agent;
import com.trustsim.simulator.agents.Graph;

public interface TrustModel {

  public int calculateTrust(Graph agentGraph, Agent agent1, Agent agent2);

}
