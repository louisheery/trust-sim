package com.trustsim.simulator.agents.WangTrustModel;

import com.trustsim.simulator.agents.Agent;
import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.ServiceRequest;
import com.trustsim.simulator.agents.TrustVectorList;
import com.trustsim.simulator.trustmodel.WangTrustModel;
import com.trustsim.synthesiser.TransactionalVectorList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WangTrustAgent implements Agent {

  public Graph graph;
  private int id;
  private HashMap<String, Double> trustDimensions;
  private Double honestyToGiveTruthfulFeedback; // BIAS TO MEAN value of whether truthful feedback is given i.e. extent to which recommendation value is the inverse of truthful value.
  private Double abilityToPerformServiceRequest; // MEAN of whether ServiceRequest is completed or not
  private Double likelihoodToPerformServiceRequest; // STDEV of whether ServiceRequest is completed or not
  private Double trustThresholdToPerformATransactionForAConsumer = 0.5;
  // AgentPersonalityDimensions Array //
  // AgentPersonalityDimensions[0] = honestyToGiveTruthfulFeedback
  // AgentPersonalityDimensions[1] = abilityToPerformServiceRequest
  // AgentPersonalityDimensions[2] = likelihoodToPerformServiceRequest
  // AgentPersonalityDimensions[3] = trustThresholdToPerformATransactionForAConsumer
  /////////////////////////////////////


  WangTrustAgent(Graph graph, int id, List<Double> dimensions, List<Double> agentPersonalityDimensions) {
    this.graph = graph;
    this.id = id;
    this.honestyToGiveTruthfulFeedback = agentPersonalityDimensions.get(0);
    this.abilityToPerformServiceRequest = agentPersonalityDimensions.get(1);
    this.likelihoodToPerformServiceRequest = agentPersonalityDimensions.get(2);

    for (int i = 0; i < dimensions.size(); i++) {
      if (dimensions.get(i) != null) {
        trustDimensions.put(Integer.toString(i), dimensions.get(i));
      }
    }
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Agent other) {
    return this.id == other.getId();
  }

  @Override
  public int compareTo(Agent o) {
    return this.id == o.getId() ? 0 : 1;
  }

  public int hashCode() {
    final int primeNum = 11;
    int output = 1;
    output = (primeNum * output) + id;
    return output;
  }

  public void updateTransactionHistory(Agent otherAgent, ServiceRequest serviceRequestTransaction) {

    TransactionalVectorList transactionalVector = graph.getTransactionalVector(this, otherAgent);
    transactionalVector.setTransactionalVectorValue(serviceRequestTransaction.getCompletionTime(), serviceRequestTransaction.isCompleted());

    graph.addTransactionalEdge(this, otherAgent, transactionalVector);

  }

  public double factorialFunc(double input) {
    int value = (int) input;
    int result = 1;

    for (int i = 1; i < value; i++) {
      result *= i;
    }

    return result;
  }

  @Override
  public void updateTrustValues(Agent otherAgent) {

    TransactionalVectorList transactionalVector = graph.getTransactionalVector(this, otherAgent);
    Map<Integer, Boolean> transactionalValues = transactionalVector.getTransactionsMap();

    // CALCULATE DIRECT TRUST
    // CALCULATE TRANSACTION FREQUENCY, f(N(t))

    double lambdaGraphAgentsAverageTransactionsPerUnitTime = graph.getLambda(); // IMPLEMENT THIS FUNCTION!!
    // I THINK GETLAMBDA() should return the "average times per unit time" i.e. the average number of transactions
    // that are carried out per unit time averaged across all of the agents.
    double intervalTime = transactionalVector.getLastTransactionTime() - transactionalVector.getFirstTransactionTime();
    double kConstant = transactionalVector.getNumberOfTransactions();

    double transactionFrequency = Math.exp(-1 * lambdaGraphAgentsAverageTransactionsPerUnitTime * intervalTime) * (Math.pow(lambdaGraphAgentsAverageTransactionsPerUnitTime * intervalTime, kConstant) / (factorialFunc(kConstant))); // IMPLEMENT FORMULAE FOR THIS BASED ON transactionalVector values;

    // CALCULATE UNWEIGHTED DIRECT TRUST, DT_{SP}^{E}
    double successfulServices = Collections.frequency(transactionalVector.getTransactionsMap().values(), true);
    double unsuccessfulServices = Collections.frequency(transactionalVector.getTransactionsMap().values(), false);

    double unweightedDirectTrust = (successfulServices + 1) / (successfulServices + unsuccessfulServices + 2);
    double directTrust = transactionFrequency * unweightedDirectTrust;


    // CALCULATE INDIRECT TRUST
    double indirectTrustTotal = 0;
    double indirectTrustN = 0;

    // DEPTH FIRST SEARCH OF THE GRAPH
    // USING FORMULA (13) + (18) of Paper!
    // https://www.geeksforgeeks.org/find-paths-given-source-destination/

    double indirectTrust = indirectTrustTotal / indirectTrustN;  // IMPLEMENT FORMULAE FOR THIS BASED ON graph.getTrustRecommendations();

    // CALCULATE GENERAL TRUST VALUE
    double generalTrust = (directTrust * WangTrustModel.LAMBDA_CONST) + (indirectTrust * WangTrustModel.GAMMA_CONST);


    // UPDATE TRUSTVECTORLIST
    TrustVectorList trustVector = graph.getTrustVector(this, otherAgent);
    trustVector.setTrustVectorValue(directTrust, 0);
    trustVector.setTrustVectorValue(indirectTrust, 1);
    trustVector.setTrustVectorValue(generalTrust, 2);

    graph.addTrustEdge(this, otherAgent, trustVector);


  }

  public int provideARecommendation(Agent receivingAgent, ServiceRequest serviceRequestTransaction) {
    return 0;
  }


  public void giveFeedback(Agent receivingAgent, ServiceRequest serviceRequest) {
    // IMPLEMENT THIS!
  }

  public Double requestTrustScoreInAnotherAgent(Agent otherAgent) {

    Agent currentAgent = this;

    // DOES WANG IMPLEMENT THE ABILITY OF AN AGENT TO DECIDE NOT TO GIVE A TRUST RECOMMENDATION TO
    // ANOTHER AGENT

    // ALSO SHOULD THIS BE RETURNING GENERALTRUST OR DIRECT TRUST SCORE????
    if (graph.hasDirectConnection(this, otherAgent)) {
      return graph.getTrustVector(this, otherAgent).getTrustVectorValue(0);
    } else {
      return null;
    }

  }


}