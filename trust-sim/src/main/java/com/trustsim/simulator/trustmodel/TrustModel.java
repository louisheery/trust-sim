package com.trustsim.simulator.trustmodel;

import com.trustsim.simulator.agents.WangTrustModel.WangTrustAgent;
import com.trustsim.simulator.agents.Graph;

public interface TrustModel {

  public int calculateTrust(Graph agentGraph, WangTrustAgent agent1, WangTrustAgent agent2);

  String getName();
}
