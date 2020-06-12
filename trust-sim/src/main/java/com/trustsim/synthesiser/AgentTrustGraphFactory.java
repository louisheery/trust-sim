package com.trustsim.synthesiser;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.TrustVectorList;

public interface AgentTrustGraphFactory {
  void initialiseGraphTrust(Graph<TrustVectorList, TransactionalVectorList> graph);

//  Double[] personalityDimensionDistribution(double minValue, double maxValue, int numberOfDimensions);
//
//  Double[] trustDimensionDistribution(double consumerMean, double consumerVar, int numberOfDimensions);
//
//  void dispatchAgents(int consumerAgents, int producerAgents, int truthAgents);
//
//  void removeAgents(int consumerAgents, int producerAgents, int truthAgents);
}
