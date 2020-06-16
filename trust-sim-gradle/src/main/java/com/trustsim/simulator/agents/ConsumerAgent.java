package com.trustsim.simulator.agents;

public interface ConsumerAgent extends Agent {

  void assignServiceRequest(ServiceRequest request);

  void updateTrustValues(Agent otherAgent);

  Double provideEvaluationValueOfProducer(ProducerAgent producer);

  // ProducerAgent getPreferredProducerForTransaction(List<ProducerAgent> producerAgentsWillingToDoTransaction);
}
