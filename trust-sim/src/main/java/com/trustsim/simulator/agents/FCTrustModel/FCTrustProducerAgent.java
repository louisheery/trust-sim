package com.trustsim.simulator.agents.FCTrustModel;

import com.trustsim.simulator.agents.*;

import java.util.List;

public class FCTrustProducerAgent extends FCTrustAgent implements ProducerAgent {

  List<ServiceRequest> jobs;

  public FCTrustProducerAgent(Graph graph, int id, Double[] dimensions, Double[] agentPersonalityDimensions) {
    super(graph, id, dimensions, agentPersonalityDimensions);
  }

  public void requestService(ServiceRequest request) {
    jobs.add(request);
  }


  public boolean receiveTransactionRequest(ServiceRequest serviceRequest) {

    // CODE DETERMINES IF PRODUCER WILL CARRY OUT TRANSACTION
    trustThresholdToPerformATransactionForAConsumer

    ConsumerAgent consumerRequestingTransaction = serviceRequest.getConsumerAgentOfRequest();

    // LOCAL TRUST SCORE
    int degreesOfFreedom = 0;
    while (degreesOfFreedom < 3) {
      if (graph.hasDirectConnection(this, otherAgent)) {
        return graph.getTrustVector(this, otherAgent);
      } else {

      }
    }

    // GLOBAL TRUST SCORE
    Double consumerTrustScore = 0;
    return trustThresholdToPerformATransactionForAConsumer > consumerTrustScore;
  }


  public boolean receiveTransactionRequest(ServiceRequest serviceRequest) {

    // FORMULAE THAT DETERMINES IF PRODUCER WILL CARRY OUT TRANSACTION BASED ON ITS PERSONALITY

    return Math.random() > 0.5;

  }

}