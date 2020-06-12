package com.trustsim.synthesiser;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.TrustVectorList;

import java.util.List;

public interface AgentGraphFactory {

  Graph<TrustVectorList, TransactionalVectorList> createGraph(int numberOfConsumers, int numberOfProducers);

  void initialiseGraphTransactions(Graph<TrustVectorList, TransactionalVectorList> graph, List<TransactionHistory> historicTransactions);

  void initialiseGraphTrust(Graph<TrustVectorList, TransactionalVectorList> graph);

//  Double[] personalityDimensionDistribution(double minValue, double maxValue, int numberOfDimensions);
//
//  Double[] trustDimensionDistribution(double consumerMean, double consumerVar, int numberOfDimensions);
//
//  void dispatchAgents(int consumerAgents, int producerAgents, int truthAgents);
//
//  void removeAgents(int consumerAgents, int producerAgents, int truthAgents);
}
