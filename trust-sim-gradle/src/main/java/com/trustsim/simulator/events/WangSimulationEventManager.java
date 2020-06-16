package com.trustsim.simulator.events;

import com.trustsim.simulator.agents.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class WangSimulationEventManager implements SimulationEventManager {

  private boolean earlyStop = false;
  int totalNumberOfServiceRequests;
  private Graph currentGraph;
  private final SimulationBiddingManager biddingManager = WangSimulationBiddingManager.getInstance();
  private final PriorityQueue<ServiceRequest> requestQueueForEachTimeStepInSimulation = new PriorityQueue<>();

  public WangSimulationEventManager(int totalNumberOfServiceRequests, Graph currentGraph) {

    this.totalNumberOfServiceRequests = totalNumberOfServiceRequests;
    this.currentGraph = currentGraph;
  }

  @Override
  public void startSim() {

    int numberOfDispatchedEvents = 0;

    // 1. GENERATE ALL SERVICE REQUESTS AND PUT THEM IN THE QUEUE
    while(numberOfDispatchedEvents < totalNumberOfServiceRequests && !earlyStop) {

      // Create New Service Request
      ServiceRequest newServiceRequest = generateRandomServiceRequest();

      // Add Service Request to Queue
      requestQueueForEachTimeStepInSimulation.add(newServiceRequest);

      numberOfDispatchedEvents++;

    }

    // 2. ACTUALLY RUN THE SIMULATION, i.e. remove each event from Queue one by one.
    while (requestQueueForEachTimeStepInSimulation.size() != 0) {

      List<ServiceRequest> currentServiceRequestForThisTimeStep = new ArrayList<>();

      // a. Get first service request from the queue
      currentServiceRequestForThisTimeStep.add(requestQueueForEachTimeStepInSimulation.poll());

      // b. Get any other service requests which have same Time of Request as 1st Service Request
      while (currentServiceRequestForThisTimeStep.get(0).getRequestTime() == requestQueueForEachTimeStepInSimulation.peek().getRequestTime()) {
        currentServiceRequestForThisTimeStep.add(requestQueueForEachTimeStepInSimulation.poll());
      }

      // c. TOPSIS BIDDING PROCESS TO DETERMINE WHO SHOULD DO EACH TRANSACTION
      biddingManager.processRequestsToFindProducersWhoWillCarryOutTransactionThenRankBasedOnTopsisThenAssignProducersToTransactions(currentGraph, currentServiceRequestForThisTimeStep);

//      for (ServiceRequest request : currentServiceRequestForThisTimeStep) {
//        List<ProducerAgent> producerAgentsWillingToDoTransaction = biddingManager.processRequestsToFindProducersWhoWillCarryOutTransaction(currentGraph, request);
//        // Determine Which Producer Will Carry out that Service Request
//        ConsumerAgent consumerAgentOfTransaction = request.getConsumerAgentOfRequest();
//        ProducerAgent producerAgentChosenToCompleteTransaction = biddingManager.rankProducersByTOPSISClosenessDegree(producerAgentsWillingToDoTransaction).get(0);
//        request.setProducerAgentOfRequest(producerAgentChosenToCompleteTransaction);
//      }



      // d. Determine Whether Producer Completes ServiceRequest & Update ServiceRequest properties
      for (ServiceRequest request : currentServiceRequestForThisTimeStep) {
        boolean isTransactionCompletedSuccessfully = request.getProducerAgentOfRequest().completeTransactionRequest(request);

        // Update Trust Values of Consumer in the Producer and the Producer in the Consumer; based on
        // the attributes of the transaction and whether it was processed successfully.
        request.markWhetherCompleted(isTransactionCompletedSuccessfully);

        // Update Transaction History for ConsumerAgent and ProducerAgent
        ConsumerAgent consumerAgent = request.getConsumerAgentOfRequest();
        ProducerAgent producerAgent = request.getProducerAgentOfRequest();
        consumerAgent.updateTransactionHistory(request.getProducerAgentOfRequest(), request);
        producerAgent.updateTransactionHistory(request.getConsumerAgentOfRequest(), request);


        // Update Indirect Trust Value Calculations
        updateIndirectTrustValues(consumerAgent); // SHOULD CONSUMER TRUST VALUE BE AFFECTED???
        updateIndirectTrustValues(producerAgent);


      }
    }
  }

  public void updateIndirectTrustValues(Agent agentBeingEvaluated) {

    double recommendationTrustValue = 0;

    List<Agent> allAgents = currentGraph.getAllAgents();

    for (Agent evaluator : allAgents) {
      for (Agent recommender : allAgents) {
        if (recommender == evaluator || recommender == agentBeingEvaluated || evaluator == agentBeingEvaluated) {
          continue;
        }

        double recommenderEvaluatorToAgentSimilarity = calculateAgentSimilarity(recommender, evaluator, agentBeingEvaluated);

        // RECOMMENDER-CALCULATED VALUE OF TRUST OF AGENT BEING EVALUATED
        double alpha;
        if (currentGraph.hasDirectConnection(agentBeingEvaluated, recommender)) {
          alpha = currentGraph.getDirectTrustVector(agentBeingEvaluated, recommender).getAverageTrustVectorValue();
        } else {
          List<List<Agent>> routesBetweenAgentIandAgentK = currentGraph.getAllRoutesBetweenAgents(recommender, agentBeingEvaluated);

          alpha = 0;

          for (List<Agent> currentRoute : routesBetweenAgentIandAgentK) {
            double alphaFromCurrentRoute = 1 / routesBetweenAgentIandAgentK.size();

            for (int i = 0; i < currentRoute.size() - 1; i++) {
              alphaFromCurrentRoute *= currentGraph.getDirectPartialTrustValue(currentRoute.get(i), currentRoute.get(i + 1));
            }

            alpha += alphaFromCurrentRoute;
          }

        }
        double weightOfAcquaintanceRecommendationNode;
        if (currentGraph.hasDirectConnection(recommender, agentBeingEvaluated)) {
          weightOfAcquaintanceRecommendationNode = currentGraph.getDirectTrustVector(recommender, agentBeingEvaluated).getAverageTrustVectorValue();
        } else {
          weightOfAcquaintanceRecommendationNode = 0.5;
        }

        double directTrustRecommenderVsServiceProvider = currentGraph.getDirectTrustVector(recommender, agentBeingEvaluated).getAverageTrustVectorValue();


        double recommenderCalculatedTrustInAgentBeingEvaluated = Math.cbrt(alpha * weightOfAcquaintanceRecommendationNode * directTrustRecommenderVsServiceProvider);

        // EVALUATOR-CALCULATED VALUE OF TRUST OF AGENT BEING EVALUATED
        List<Agent> agentsWithDirectTrustInEvaluatorAgent = currentGraph.getDirectConnections(evaluator);
        double beta = 0;
        for (Agent connectionOfEvaluator : agentsWithDirectTrustInEvaluatorAgent) {
          beta += currentGraph.getDirectTrustVector(connectionOfEvaluator, evaluator).getAverageTrustVectorValue();
        }
        beta /= agentsWithDirectTrustInEvaluatorAgent.size();

        double weightOfStrangerRecommendationNode;
        if (currentGraph.hasDirectConnection(evaluator, agentBeingEvaluated)) {
          weightOfStrangerRecommendationNode = currentGraph.getDirectTrustVector(evaluator, agentBeingEvaluated).getAverageTrustVectorValue();
        } else {
          weightOfStrangerRecommendationNode = 0.5;
        }

        double directPartialTrustEvaluatorInServiceProvider = currentGraph.getDirectPartialTrustValue(evaluator, agentBeingEvaluated);

        double evaluatorCalculatedTrustInAgentBeingEvaluated = Math.cbrt(beta * weightOfStrangerRecommendationNode * directPartialTrustEvaluatorInServiceProvider);

        recommendationTrustValue += recommenderEvaluatorToAgentSimilarity * (recommenderCalculatedTrustInAgentBeingEvaluated + evaluatorCalculatedTrustInAgentBeingEvaluated);
      }
    }

    TrustVectorList trustVectorList = new TrustVectorList(new ArrayList<>(Arrays.asList((Double) recommendationTrustValue)));

    currentGraph.setIndirectTrustVector(agentBeingEvaluated, trustVectorList);

  }

  private double calculateAgentSimilarity(Agent recommender, Agent evaluator, Agent agentBeingEvaluated) {
    // IMPLEMENT THIS;
    return 1.0;
  }

  @Override
  public ServiceRequest generateRandomServiceRequest() {
    List<Double> requestTrustRequirements = new ArrayList<>(List.of(Math.random(),Math.random(),Math.random(),Math.random(),Math.random()));
    int requestTime = 10; // IMPLEMENT THIS;
    ServiceRequest request = new ServiceRequest(requestTrustRequirements, requestTime);

    List<ConsumerAgent> consumerAgents = currentGraph.getAllConsumerAgents();

    request.setConsumerAgentOfRequest(consumerAgents.get((int) (Math.random() * consumerAgents.size())));
    return new ServiceRequest(requestTrustRequirements, requestTime);
  }

  @Override
  public void stopEarly() {
    earlyStop = true;
  }

//
//  @Override
//  public void addToQueue(ServiceRequest serviceRequestsAtCurrentTimestep) {
//    requestQueueForEachTimeStepInSimulation.add(serviceRequestsAtCurrentTimestep);
//  }
//
//  @Override
//  public ServiceRequest retrieveFromQueue() {
//    return requestQueueForEachTimeStepInSimulation.poll();
//  }



}
