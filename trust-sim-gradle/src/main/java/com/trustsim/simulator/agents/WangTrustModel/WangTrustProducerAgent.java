package com.trustsim.simulator.agents.WangTrustModel;

import com.trustsim.simulator.agents.ConsumerAgent;
import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.ProducerAgent;
import com.trustsim.simulator.agents.ServiceRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WangTrustProducerAgent extends WangTrustAgent implements ProducerAgent {

  List<ServiceRequest> jobs;

  public WangTrustProducerAgent(Graph graph, int id) {
    super(graph, id, new ArrayList<Double>(Arrays.asList(1.0,1.0,1.0,1.0,1.0)), new ArrayList<Double>(Arrays.asList(1.0,1.0,1.0,1.0,1.0)));
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
    // Double consumerTrustScore = 0.0;
    // return trustThresholdToPerformATransactionForAConsumer > consumerTrustScore;
//    return Math.random() > 0.5;

    // IMPLEMENT THIS: PRODUCER SHOULD INSTEAD DETERMINE WHETHER THEY WANT TO CARRY OUT TRANSACTION
    // BASED ON THEIR TRUST THRESHOLD AND THEIR GENERAL TRUST SCORE OF THE CONSUMER i.e. serviceRequest.getConsumerAgent();

    // this.updateTrustValues(serviceRequest.getConsumerAgent());
    // return trustThreshold < graph.getTrustVector(this, serviceRequest.getConsumerAgentOfRequest());

    return true;

  }

  @Override
  public boolean completeTransactionRequest(ServiceRequest serviceRequest) {
    // IMPLEMENT THIS !!
    return Math.random() > 0.5;
  }


}