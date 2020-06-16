package com.trustsim.simulator.events;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.ProducerAgent;
import com.trustsim.simulator.agents.ServiceRequest;

import java.util.List;

public interface SimulationBiddingManager {

  List<ProducerAgent> processRequestsToFindProducersWhoWillCarryOutTransaction(Graph agentGraph, ServiceRequest serviceRequests);

  List<ProducerAgent> rankProducersByTOPSISClosenessDegree(List<ProducerAgent> producerAgentsWillingToDoTransaction);

  void processRequestsToFindProducersWhoWillCarryOutTransactionThenRankBasedOnTopsisThenAssignProducersToTransactions(Graph currentGraph, List<ServiceRequest> currentServiceRequestForThisTimeStep);
}
