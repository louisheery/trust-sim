package com.trustsim.simulator.agents.FCTrustModel;

import com.trustsim.simulator.agents.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FCTrustConsumerAgent extends FCTrustAgent implements ConsumerAgent {

  List<ServiceRequest> requests;

  public FCTrustConsumerAgent(Graph graph, int id, Double[] dimensions, Double[] agentPersonalityDimensions) {
    super(graph, id, dimensions, agentPersonalityDimensions);
  }

  public void assignServiceRequest(ServiceRequest request) {
    requests.add(request);
  }

  @Override
  public ProducerAgent getPreferredProducerForTransaction(List<ProducerAgent> producerAgentsWillingToDoTransaction) {
    Map<ProducerAgent, TrustVectorList> producerTrustScores = new HashMap<>();

    for (ProducerAgent producerAgent : producerAgentsWillingToDoTransaction) {
      producerTrustScores.put(producerAgent, this.requestTrustScoreInAnotherAgent(producerAgent));
    }

    // RETURN PRODUCER AGENT WITH THE HIGHEST TRUST SCORE
    // IMPLEMENT THIS!!! THIS CURRENT RETURN IS NOT RIGHT!!!
    // INSTEAD YOU NEED TO FIND THE PRODUCER THAT HAS THE HIGHEST TRUST SCORE!!!
    return producerTrustScores.keySet().iterator().next();

  }

}
