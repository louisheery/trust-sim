package com.trustsim.simulator.agents;

public interface ProducerAgent extends Agent {

  void requestService(ServiceRequest request);

  void updateTrustValues(Agent otherAgent);

  boolean receiveTransactionRequest(ServiceRequest serviceRequest);


}

