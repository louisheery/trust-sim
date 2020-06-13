package com.trustsim.simulator.agents.WangTrustModel;

import com.trustsim.simulator.agents.ConsumerAgent;
import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.ProducerAgent;
import com.trustsim.simulator.agents.ServiceRequest;

import java.util.List;

public class WangTrustProducerAgent extends WangTrustAgent implements ProducerAgent {

  List<ServiceRequest> jobs;

  public WangTrustProducerAgent(Graph graph, int id, List<Double> agentTrustDimensions, List<Double> agentPersonalityDimensions) {
    super(graph, id, agentTrustDimensions, agentPersonalityDimensions);
  }

  public void requestService(ServiceRequest request) {
    jobs.add(request);
  }



  public boolean receiveTransactionRequest(ServiceRequest serviceRequest) {

    // CODE DETERMINES IF PRODUCER WILL CARRY OUT TRANSACTION
    //// //trustThresholdToPerformATransactionForAConsumer

    ConsumerAgent consumerRequestingTransaction = serviceRequest.getConsumerAgentOfRequest();

    // LOCAL TRUST SCORE
//    int degreesOfFreedom = 0;
//    while (degreesOfFreedom < 3) {
//      if (graph.hasDirectConnection(this, serviceRequest.getConsumerAgentOfRequest())) {
//        return graph.getTrustVector(this, serviceRequest.getConsumerAgentOfRequest());
//      } else {
//
//      }
//    }

    // GLOBAL TRUST SCORE
    Double consumerTrustScore = 0.0;
    // return trustThresholdToPerformATransactionForAConsumer > consumerTrustScore;
    return Math.random() > 0.5;
  }


}