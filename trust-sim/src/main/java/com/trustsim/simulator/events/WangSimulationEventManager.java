package com.trustsim.simulator.events;

import com.trustsim.simulator.agents.ConsumerAgent;
import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.ProducerAgent;
import com.trustsim.simulator.agents.ServiceRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class WangSimulationEventManager implements SimulationEventManager {

  private boolean earlyStop = false;
  int dispatchFrequency;
  int totalNumberOfServiceRequests;
  private Graph currentGraph;
  private final SimulationBiddingManager biddingManager = SimulationBiddingManager.getInstance();
  private final PriorityQueue<ServiceRequest> requestQueueForEachTimeStepInSimulation = new PriorityQueue<>();

  public WangSimulationEventManager(int frequency, int totalNumberOfServiceRequests, Graph currentGraph) {

    this.dispatchFrequency = frequency;
    this.totalNumberOfServiceRequests = totalNumberOfServiceRequests;
    this.currentGraph = currentGraph;
  }

  @Override
  public void start() {

    int numberOfDispatchedEvents = 0;

    while(numberOfDispatchedEvents < totalNumberOfServiceRequests && !earlyStop) {

      // Create New Service Request
      ServiceRequest newServiceRequest = generateRandomServiceRequest();
      List<ProducerAgent> producerAgentsWillingToDoTransaction = biddingManager.processRequestsToFindProducersWhoWillCarryOutTransaction(currentGraph, newServiceRequest);
      numberOfDispatchedEvents++;

      // Determine Which Producer Will Carry out that Service Request
      ConsumerAgent consumerAgentOfTransaction = newServiceRequest.getConsumerAgentOfRequest();
      ProducerAgent producerAgentChosenToCompleteTransaction = consumerAgentOfTransaction.getPreferredProducerForTransaction(producerAgentsWillingToDoTransaction);

      // Determine Whether Producer Completes ServiceRequest & Update ServiceRequest properties
      boolean isTransactionCompletedSuccessfully = producerAgentChosenToCompleteTransaction.receiveTransactionRequest(newServiceRequest);

      // Update Trust Values of Consumer in the Producer and the Producer in the Consumer; based on
      // the attributes of the transaction and whether it was processed successfully.
      newServiceRequest.markWhetherCompleted(isTransactionCompletedSuccessfully);

      consumerAgentOfTransaction.updateTrustScores(producerAgentChosenToCompleteTransaction, newServiceRequest);
      producerAgentChosenToCompleteTransaction.updateTrustScores(consumerAgentOfTransaction, newServiceRequest);


    }
  }

  @Override
  public ServiceRequest generateRandomServiceRequest() {
    List<Double> requestTrustRequirements = new ArrayList<>(List.of(Math.random(),Math.random(),Math.random(),Math.random(),Math.random()));
    return new ServiceRequest(requestTrustRequirements);
  }

  @Override
  public void stopEarly() {
    earlyStop = true;
  }


  @Override
  public void addToQueue(ServiceRequest serviceRequestsAtCurrentTimestep) {
    requestQueueForEachTimeStepInSimulation.add(serviceRequestsAtCurrentTimestep);
  }

  @Override
  public ServiceRequest retrieveFromQueue() {
    return requestQueueForEachTimeStepInSimulation.poll();
  }



}
