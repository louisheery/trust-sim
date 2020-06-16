package com.trustsim.synthesiser;

import com.trustsim.simulator.agents.Graph;

import java.util.List;

public interface AgentGraphFactory {

  Graph createGraph(int numberOfConsumers, int numberOfProducers);

  void initialiseGraphTransactions(Graph graph, List<TransactionHistory> historicTransactions);

  void initialiseGraphTrust(Graph graph);

//  Double[] personalityDimensionDistribution(double minValue, double maxValue, int numberOfDimensions);
//
//  Double[] trustDimensionDistribution(double consumerMean, double consumerVar, int numberOfDimensions);
//
//  void dispatchAgents(int consumerAgents, int producerAgents, int truthAgents);
//
//  void removeAgents(int consumerAgents, int producerAgents, int truthAgents);
}
