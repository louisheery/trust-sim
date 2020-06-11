package com.trustsim.simulator.events;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.ProducerAgent;
import com.trustsim.simulator.agents.ServiceRequest;

import java.util.List;

public class SimulationBiddingManager {

  private static SimulationBiddingManager INSTANCE;

  private SimulationBiddingManager() {

  }

  public static SimulationBiddingManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SimulationBiddingManager();
    }
    return INSTANCE;
  }

  public List<ProducerAgent> processRequestsToFindProducersWhoWillCarryOutTransaction(Graph agentGraph, ServiceRequest serviceRequests) {

    List<ProducerAgent> producersWillingToDoTransaction = null;

    List<ProducerAgent> allProducerAgents = agentGraph.getAllProducerAgents();

    for (ProducerAgent producerAgent: allProducerAgents) {

      if (producerAgent.receiveTransactionRequest(serviceRequests)) {
        producersWillingToDoTransaction.add(producerAgent);
      }

    }

    return producersWillingToDoTransaction;
  }

}
