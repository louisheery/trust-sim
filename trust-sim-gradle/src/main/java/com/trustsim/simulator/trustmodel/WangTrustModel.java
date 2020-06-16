package com.trustsim.simulator.trustmodel;

import com.trustsim.simulator.agents.WangTrustModel.WangTrustAgent;
import com.trustsim.simulator.agents.Graph;

public class WangTrustModel implements TrustModel {

  public static final Double LAMBDA_CONST = 0.5;
  public static final Double GAMMA_CONST = 0.5;
  public final String MODEL_NAME = "WangTrust";

  @Override
  public int calculateTrust(Graph agentGraph, WangTrustAgent agent1, WangTrustAgent agent2) {

    //return agentGraph.getIndirectTrustPathway(agent1, agent2);
    return 1;
  }

  @Override
  public String getName() {
    return MODEL_NAME;
  }

  @Override
  public String toString(){
    return MODEL_NAME;
  }
}
