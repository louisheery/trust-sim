package com.trustsim.simulator.agents.WangTrustModel;

import com.trustsim.simulator.agents.*;

import java.util.HashMap;

public class WangTrustAgent implements Agent {

  Graph graph;
  int id;
  HashMap<String, Double> trustDimensions;
  Double honestyToGiveTruthfulFeedback; // BIAS TO MEAN value of whether truthful feedback is given i.e. extent to which recommendation value is the inverse of truthful value.
  Double abilityToPerformServiceRequest; // MEAN of whether ServiceRequest is completed or not
  Double likelihoodToPerformServiceRequest; // STDEV of whether ServiceRequest is completed or not
  Double trustThresholdToPerformATransactionForAConsumer = 0.5;
  // AgentPersonalityDimensions Array //
  // AgentPersonalityDimensions[0] = honestyToGiveTruthfulFeedback
  // AgentPersonalityDimensions[1] = abilityToPerformServiceRequest
  // AgentPersonalityDimensions[2] = likelihoodToPerformServiceRequest
  // AgentPersonalityDimensions[3] = trustThresholdToPerformATransactionForAConsumer
  /////////////////////////////////////


  WangTrustAgent(Graph graph, int id, Double[] dimensions, Double[] agentPersonalityDimensions) {
    this.graph = graph;
    this.id = id;
    this.honestyToGiveTruthfulFeedback = agentPersonalityDimensions[0];
    this.abilityToPerformServiceRequest = agentPersonalityDimensions[1];
    this.likelihoodToPerformServiceRequest = agentPersonalityDimensions[2];

    for (int i = 0; i < dimensions.length; i++) {
      if (dimensions[i] != null) {
      trustDimensions.put(Integer.toString(i), dimensions[i]);
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

  public void updateTrustScores(Agent otherAgent, ServiceRequest serviceRequestTransaction) {

  }

  public int provideARecommendation(Agent receivingAgent, ServiceRequest serviceRequestTransaction) {
    return 0;
  }


  public void giveFeedback(Agent receivingAgent, ServiceRequest serviceRequest) {
    // IMPLEMENT THIS!
  }

  public TrustVectorList requestTrustScoreInAnotherAgent(Agent otherAgent) {

    Agent currentAgent = this;
    while (!graph.hasDirectConnection(currentAgent, otherAgent)) {



      if (graph.hasDirectConnection(this, otherAgent)) {
        return (TrustVectorList) graph.getTrustVector(this, otherAgent);
      } else {

      }
    }



  }


}