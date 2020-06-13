package com.trustsim.simulator.agents.WangTrustModel;

import com.trustsim.simulator.agents.*;

import java.util.List;

public class WangTrustConsumerAgent extends WangTrustAgent implements ConsumerAgent {

  private List<ServiceRequest> requests;
  private TrustVectorList trustVectorList;

  public WangTrustConsumerAgent(Graph graph, int id, List<Double> agentTrustDimensions, List<Double> agentPersonalityDimensions) {
    super(graph, id, agentTrustDimensions, agentPersonalityDimensions);
  }

  public void assignServiceRequest(ServiceRequest request) {
    requests.add(request);
  }

  @Override
  public ProducerAgent getPreferredProducerForTransaction(List<ProducerAgent> producerAgentsWillingToDoTransaction) {

    // NEED TO IMPLEMENT THIS BASED ON TOPSIS METHOD


    return null;
  }
//    Map<ProducerAgent, TrustVectorList> producerTrustScores = new HashMap<>();
//
//    for (ProducerAgent producerAgent : producerAgentsWillingToDoTransaction) {
//      producerTrustScores.put(producerAgent, this.requestTrustScoreInAnotherAgent(producerAgent));
//    }
//
//    // RETURN PRODUCER AGENT WITH THE HIGHEST TRUST SCORE
//    // IMPLEMENT THIS!!! THIS CURRENT RETURN IS NOT RIGHT!!!
//    // INSTEAD YOU NEED TO FIND THE PRODUCER THAT HAS THE HIGHEST TRUST SCORE!!!
//    return producerTrustScores.keySet().iterator().next();
//
//  }

}
