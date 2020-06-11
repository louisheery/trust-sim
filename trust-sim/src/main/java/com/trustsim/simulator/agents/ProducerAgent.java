package com.trustsim.simulator.agents;

public interface ProducerAgent extends Agent {

  void requestService(ServiceRequest request);

  boolean receiveTransactionRequest(ServiceRequest serviceRequest);


}

