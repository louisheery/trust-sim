package com.trustsim.simulator.trustmodel;

import com.trustsim.simulator.agents.FCTrustModel.FCTrustAgent;
import com.trustsim.simulator.agents.Graph;

public interface TrustModel {

  public int calculateTrust(Graph agentGraph, FCTrustAgent agent1, FCTrustAgent agent2);

  String getName();
}
