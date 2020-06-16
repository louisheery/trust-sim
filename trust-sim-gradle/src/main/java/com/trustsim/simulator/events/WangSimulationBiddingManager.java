package com.trustsim.simulator.events;

import com.trustsim.simulator.agents.*;

import java.util.*;

public class WangSimulationBiddingManager implements SimulationBiddingManager {

  private static WangSimulationBiddingManager INSTANCE = null;

  private WangSimulationBiddingManager() {

  }

  public static WangSimulationBiddingManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new WangSimulationBiddingManager();
    }
    return INSTANCE;
  }

  @Override
  public List<ProducerAgent> processRequestsToFindProducersWhoWillCarryOutTransaction(Graph agentGraph, ServiceRequest serviceRequests) {

    // OVERSIMPLIFIED HERE: Currently ALL Producers will Accept a Transaction Request
    // So producersWillingToDoTransaction == graph.getAllProducerAgents();

    List<ProducerAgent> producersWillingToDoTransaction = new ArrayList<>();

    List<ProducerAgent> allProducerAgents = agentGraph.getAllProducerAgents();

    for (ProducerAgent producerAgent : allProducerAgents) {

      if (producerAgent.receiveTransactionRequest(serviceRequests)) {
        producersWillingToDoTransaction.add(producerAgent);
      }

    }
    return producersWillingToDoTransaction;
  }

  @Override
  public List<ProducerAgent> rankProducersByTOPSISClosenessDegree(List<ProducerAgent> producerAgentsWillingToDoTransaction) {

    Map<Double, ProducerAgent> rankedProducers = new HashMap<>();

    for (ProducerAgent producer : producerAgentsWillingToDoTransaction) {

      // IMPLEMENT THIS // using TOPSIS Algorithm; or just randomly match agents?
      Double closenessDegree = Math.random();


      rankedProducers.put(closenessDegree, producer);

    }

    List<Double> producersClosenessScore = new ArrayList<>();

    TreeMap<Double, ProducerAgent> sortedRankedProducers = new TreeMap<>(rankedProducers);

    return new ArrayList<>(sortedRankedProducers.values());
  }

  @Override
  public void processRequestsToFindProducersWhoWillCarryOutTransactionThenRankBasedOnTopsisThenAssignProducersToTransactions(Graph currentGraph, List<ServiceRequest> currentServiceRequestForThisTimeStep) {

    // Count number of unique consumer agents
    Collection<ServiceRequest> serviceRequestsWithUniqueConsumerAgents = currentServiceRequestForThisTimeStep.stream()
        .<Map<Integer, ServiceRequest>> collect(HashMap::new,(m,e)->m.put(e.getConsumerAgentOfRequest().getId(), e), Map::putAll)
        .values();

    Integer numberOfConsumerAgents = serviceRequestsWithUniqueConsumerAgents.size();


    // Count number of unique producer agents
    Collection<ServiceRequest> serviceRequestsWithUniqueProducerAgents = currentServiceRequestForThisTimeStep.stream()
        .<Map<Integer, ServiceRequest>> collect(HashMap::new,(m,e)->m.put(e.getProducerAgentOfRequest().getId(), e), Map::putAll)
        .values();

    Integer numberOfProducerAgents = serviceRequestsWithUniqueProducerAgents.size();


    // Map< [ConsumerAgent i.e. 'j', ProducerAgent i.e. 'i'], [EvaluationValue i.e. d_{ij}] >
    Map<List<Agent>, Double> serviceToProducerMap = new HashMap<>();

    for (ServiceRequest request : currentServiceRequestForThisTimeStep) {
      ConsumerAgent consumer = request.getConsumerAgentOfRequest();

      List<ProducerAgent> producersWillingToDoTransaction = processRequestsToFindProducersWhoWillCarryOutTransaction(currentGraph, request);

      for (ProducerAgent producer : producersWillingToDoTransaction) {
        double evaluationValue = consumer.provideEvaluationValueOfProducer(producer);
        serviceToProducerMap.put(new ArrayList<>(Arrays.asList(consumer, producer)), evaluationValue);
      }
    }


    // EQUATION (1) of Wang Paper
    List<Double> evaluationValues = new ArrayList<>(serviceToProducerMap.values());

    double sumOfAllEvaluationValuesSquared = 0.0;
    for (Double evaluationValue : evaluationValues) {
      sumOfAllEvaluationValuesSquared += (evaluationValue * evaluationValue);
    }
    double squaredSumOfAllEvaluationValues = Math.sqrt(sumOfAllEvaluationValuesSquared);

    for (Double evaluationValue : serviceToProducerMap.values()) {
      evaluationValue /= squaredSumOfAllEvaluationValues;
    }

    // EQUATION (2) of Wang Paper
    Map<Agent, Double> informationEntryOfConsumers = new HashMap<>();
    for (Map.Entry<List<Agent>, Double> entry : serviceToProducerMap.entrySet()) {

      if (!informationEntryOfConsumers.containsKey(entry.getKey().get(0))) {
        informationEntryOfConsumers.put(entry.getKey().get(0), 0.0);
      }

      double infoEntropyOfSingleIndex = - (1 / Math.log(numberOfProducerAgents)) * entry.getValue() * Math.log(entry.getValue());
      informationEntryOfConsumers.put(entry.getKey().get(0), infoEntropyOfSingleIndex);

    }

    // EQUATION (3 + 4) of Wang Paper
    double e0generalEntropy = 0;
    for (Double entropy : informationEntryOfConsumers.values()) {
      e0generalEntropy += entropy;
    }

    Map<Agent, Double> entropyWeightsOfConsumers = new HashMap<>();
    for (Map.Entry<Agent, Double> entry : informationEntryOfConsumers.entrySet()) {
      double entropyWeight = (1 - entry.getValue()) / (numberOfConsumerAgents - e0generalEntropy);
      entropyWeightsOfConsumers.put(entry.getKey(), entropyWeight);
    }

    // EQUATION (5) of Wang Paper
    //double xIJ[][] = wJ * dIJ;
    // IMPLEMENT THIS



  }
}
