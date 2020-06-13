package com.trustsim.simulator.agents;

import java.util.List;

public interface ConsumerAgent extends Agent {

  void assignServiceRequest(ServiceRequest request);

  void updateTrustValues(Agent otherAgent);

  ProducerAgent getPreferredProducerForTransaction(List<ProducerAgent> producerAgentsWillingToDoTransaction);
}
