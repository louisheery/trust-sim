package com.trustsim.synthesiser;

import com.trustsim.simulator.agents.Agent;
import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.TrustVectorList;
import com.trustsim.simulator.agents.WangTrustModel.WangTrustAgent;
import com.trustsim.simulator.agents.WangTrustModel.WangTrustConsumerAgent;
import com.trustsim.simulator.agents.WangTrustModel.WangTrustProducerAgent;

import java.util.ArrayList;
import java.util.List;

public class WangAgentGraphFactory implements AgentGraphFactory {

  public WangAgentGraphFactory() {

  }

  @Override
  public Graph createGraph(int numberOfConsumers, int numberOfProducers) {
    Graph graph = new Graph();

    int i = 0;
    while (i < numberOfConsumers) {
      WangTrustAgent agent = new WangTrustConsumerAgent(graph, i, null, null);
      graph.addAgent(agent);
      i++;
    }
    while (i < numberOfConsumers + numberOfProducers) {
      WangTrustAgent agent = new WangTrustProducerAgent(graph, i, null, null);
      graph.addAgent(agent);
      i++;
    }

    return graph;
  }

  @Override
  public void initialiseGraphTransactions(Graph graph, List<TransactionHistory> historicTransactions) {

    for (TransactionHistory transaction : historicTransactions) {

      if (graph.containsAgent(transaction.getConsumerAgent())) {
        graph.addAgent(transaction.getConsumerAgent());
      }

      if (graph.containsAgent(transaction.getProducerAgent())) {
        graph.addAgent(transaction.getProducerAgent());
      }

      graph.addTransactionalEdge(transaction.getConsumerAgent(), transaction.getProducerAgent(), transaction.getTransactionalVector());
      graph.addTransactionalEdge(transaction.getProducerAgent(), transaction.getConsumerAgent(), transaction.getTransactionalVector());
    }
  }


  @Override
  public void initialiseGraphTrust(Graph graph) {

    List<Agent> allAgents = graph.getAllAgents();

    for (Agent agentA : allAgents) {
      for (Agent agentB : allAgents) {
        if (!agentA.equals(agentB)) {
          List<Double> trustValues = new ArrayList<>(List.of(0.5));
          TrustVectorList trustScores = new TrustVectorList(trustValues);
          graph.addTrustEdge(agentA, agentB, trustScores);
        }
      }
    }
  }
//
//  @Override
//  public Double[] personalityDimensionDistribution(double minValue, double maxValue, int numberOfDimensions) {
//
//    Double[] personalityArray = new Double[numberOfDimensions];
//
//    for (int i = 0; i < personalityArray.length; i++) {
//      personalityArray[i] = (Math.random() * (maxValue - minValue)) + minValue;
//    }
//
//    return personalityArray;
//  }
//
//  @Override
//  public Double[] trustDimensionDistribution(double consumerMean, double consumerVar, int numberOfDimensions) {
//    Random randomNum = new Random();
//
//    Double[] trustArray = new Double[numberOfDimensions];
//
//    for (int i = 0; i < numberOfDimensions; i++) {
//      trustArray[i] = consumerMean + (randomNum.nextGaussian() * consumerVar);
//    }
//
//    return trustArray;
//  }
//
//
//  @Override
//  public void dispatchAgents(int consumerAgents, int producerAgents, int truthAgents) {
//
//  }
//
//  @Override
//  public void removeAgents(int consumerAgents, int producerAgents, int truthAgents) {
//
//  }
}


