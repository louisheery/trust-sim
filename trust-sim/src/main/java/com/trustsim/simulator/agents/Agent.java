package com.trustsim.simulator.agents;

public interface Agent extends Comparable<Agent> {

  // Comparable<AgentInterface> methods
  boolean equals(Agent other);

  int hashCode();

  void updateTrustScores(Agent otherAgent, ServiceRequest serviceRequestTransaction);

  int provideARecommendation(Agent receivingAgent, ServiceRequest serviceRequestTransaction);

  void giveFeedback(Agent receivingAgent, ServiceRequest serviceRequest);

  TrustVectorList requestTrustScoreInAnotherAgent(Agent otherAgent);

  int getId();
}
