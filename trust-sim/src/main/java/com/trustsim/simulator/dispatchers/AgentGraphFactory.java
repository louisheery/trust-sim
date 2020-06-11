package com.trustsim.simulator.dispatchers;

import com.trustsim.TrustSim;
import com.trustsim.simulator.agents.ConsumerAgent;
import com.trustsim.simulator.agents.FCTrustModel.FCTrustConsumerAgent;
import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.FCTrustModel.FCTrustProducerAgent;

import java.util.Random;

public class AgentGraphFactory {

  public AgentGraphFactory() {

  }

  public Graph createGraph(int consumerAgents, int producerAgents, int numberOfConsumerConsumerConnections, int numberOfProducerProducerConnections, int nuumberOfConsumerProducerConnections, int numberOfProducerConsumerConnections, int truthAgents, double consumerMean, double consumerVar, double producerMean, double producerVar) {

    Graph graph = new Graph();

    // CONSUMERS
    for (int i = 0; i < consumerAgents; i++) {

      Double[] trustDimensions = trustDimensionDistribution(consumerMean, consumerVar, TrustSim.NUM_OF_TRUST_DIMS);

      Double[] agentPersonalityDimensions = new Double[]{0.1, 0.2, 0.1, 0.9, 0.1};

      ConsumerAgent consumerAgent = new FCTrustConsumerAgent(graph, i, trustDimensions, agentPersonalityDimensions);
      graph.addAgent(consumerAgent);
    }

    // PRODUCERS
    for (int i = 0; i < producerAgents; i++) {

      Double[] trustDimensions = trustDimensionDistribution(consumerMean, consumerVar, TrustSim.NUM_OF_TRUST_DIMS);

      Double[] agentPersonalityDimensions = new Double[]{0.4, 0.1, 0.3, 0.1, 0.8};

      FCTrustProducerAgent producerAgent = new FCTrustProducerAgent(graph, i, trustDimensions, agentPersonalityDimensions);
      graph.addAgent(producerAgent);
    }

//    // CONSUMER-CONSUMER CONNECTIONS
//    List<Agent> consumerAgentList = graph.getAllConsumerAgents();
//
//    for (int i = 0; i < numberOfConsumerConsumerConnections; i++) {
//      Random random = new Random();
//      Agent randomAgent1 = consumerAgentList.get(random.nextInt(consumerAgentList.size()));
//      consumerAgentList.remove(randomAgent1);
//      Agent randomAgent2 = consumerAgentList.get(random.nextInt(consumerAgentList.size()));
//      consumerAgentList.add(randomAgent1);
//
//      double trustMean = Math.random();
//      double trustVar = Math.random();
//      TrustVectorList trustVectorList = new TrustVectorList(trustDimensionDistribution(trustMean, trustVar, TrustSim.NUM_OF_TRUST_DIMS), TrustSim.NUM_OF_TRUST_DIMS);
//      graph.addEdge(randomAgent1, randomAgent2, trustVectorList);
//    }
//
//    // PRODUCER-PRODUCER CONNECTIONS
//    List<Agent> producerAgentList = graph.getAllProducerAgents();
//
//    for (int i = 0; i < numberOfProducerProducerConnections; i++) {
//      Random random = new Random();
//      Agent randomAgent1 = producerAgentList.get(random.nextInt(producerAgentList.size()));
//      producerAgentList.remove(randomAgent1);
//      Agent randomAgent2 = producerAgentList.get(random.nextInt(producerAgentList.size()));
//      producerAgentList.add(randomAgent1);
//
//      double trustMean = Math.random();
//      double trustVar = Math.random();
//      TrustVectorList trustVectorList = new TrustVectorList(trustDimensionDistribution(trustMean, trustVar, TrustSim.NUM_OF_TRUST_DIMS), TrustSim.NUM_OF_TRUST_DIMS);
//      graph.addEdge(randomAgent1, randomAgent2, trustVectorList);
//    }
//
//    // CONSUMER-PRODUCER CONNECTIONS
//    for (int i = 0; i < nuumberOfConsumerProducerConnections; i++) {
//      Random random = new Random();
//      Agent randomAgent1 = consumerAgentList.get(random.nextInt(consumerAgentList.size()));
//      Agent randomAgent2 = producerAgentList.get(random.nextInt(producerAgentList.size()));
//
//      double trustMean = Math.random();
//      double trustVar = Math.random();
//      TrustVectorList trustVectorList = new TrustVectorList(trustDimensionDistribution(trustMean, trustVar, TrustSim.NUM_OF_TRUST_DIMS), TrustSim.NUM_OF_TRUST_DIMS);
//      graph.addEdge(randomAgent1, randomAgent2, trustVectorList);
//    }
//
//    // PRODUCER-CONSUMER CONNECTIONS
//    for (int i = 0; i < numberOfProducerConsumerConnections; i++) {
//      Random random = new Random();
//      Agent randomAgent1 = producerAgentList.get(random.nextInt(producerAgentList.size()));
//      Agent randomAgent2 = consumerAgentList.get(random.nextInt(consumerAgentList.size()));
//
//      double trustMean = Math.random();
//      double trustVar = Math.random();
//      TrustVectorList trustVectorList = new TrustVectorList(trustDimensionDistribution(trustMean, trustVar, TrustSim.NUM_OF_TRUST_DIMS), TrustSim.NUM_OF_TRUST_DIMS);
//      graph.addEdge(randomAgent1, randomAgent2, trustVectorList);
//    }

    return graph;
  }

  private Double[] trustDimensionDistribution(double consumerMean, double consumerVar, int numberOfDimensions) {
    Random randomNum = new Random();

    Double[] trustArray = new Double[numberOfDimensions];

    for (int i = 0; i < numberOfDimensions; i++) {
      trustArray[i] = consumerMean + (randomNum.nextGaussian() * consumerVar);
    }

    return trustArray;
  }


  public void dispatchAgents(int consumerAgents, int producerAgents, int truthAgents) {

  }

  public void removeAgents(int consumerAgents, int producerAgents, int truthAgents) {

  }

}


