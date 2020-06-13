package com.trustsim.simulator.agents;

public interface Agent extends Comparable<Agent> {

  // Comparable<AgentInterface> methods
  boolean equals(Agent other);

  int hashCode();

  void updateTrustValues(Agent otherAgent);

  void updateTransactionHistory(Agent otherAgent, ServiceRequest serviceRequestTransaction);

  int provideARecommendation(Agent receivingAgent, ServiceRequest serviceRequestTransaction);

  void giveFeedback(Agent receivingAgent, ServiceRequest serviceRequest);

  Double requestTrustScoreInAnotherAgent(Agent otherAgent);

  int getId();
}
