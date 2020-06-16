package com.trustsim.simulator.agents.WangTrustModel;

import com.trustsim.simulator.agents.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WangTrustConsumerAgent extends WangTrustAgent implements ConsumerAgent {

  private List<ServiceRequest> requests;
  private TrustVectorList trustVectorList;

  public WangTrustConsumerAgent(Graph graph, int id) {
    super(graph, id, new ArrayList<Double>(Arrays.asList(1.0,1.0,1.0,1.0,1.0)), new ArrayList<Double>(Arrays.asList(1.0,1.0,1.0,1.0,1.0)));
  }

  public void assignServiceRequest(ServiceRequest request) {
    requests.add(request);
  }

  @Override
  public Double provideEvaluationValueOfProducer(ProducerAgent producer) {

    TrustVectorList trustVector = graph.getDirectTrustVector(this, producer);
    List<Double> trustVectorList = trustVector.getTrustVector();

    Double sum = 0.0;
    for (Double trustValue : trustVectorList) {
      sum += trustValue;
    }
    Double averageTrustVectorValue = sum / trustVectorList.size();

    Double evaluationValue = averageTrustVectorValue;

    return evaluationValue;
  }
//
//  @Override
//  public ProducerAgent getPreferredProducerForTransaction(List<ProducerAgent> producerAgentsWillingToDoTransaction) {
//
//    // NEED TO IMPLEMENT THIS BASED ON TOPSIS METHOD
//
//
//    return null;
//  }
////    Map<ProducerAgent, TrustVectorList> producerTrustScores = new HashMap<>();
////
////    for (ProducerAgent producerAgent : producerAgentsWillingToDoTransaction) {
////      producerTrustScores.put(producerAgent, this.requestTrustScoreInAnotherAgent(producerAgent));
////    }
////
////    // RETURN PRODUCER AGENT WITH THE HIGHEST TRUST SCORE
////    // IMPLEMENT THIS!!! THIS CURRENT RETURN IS NOT RIGHT!!!
////    // INSTEAD YOU NEED TO FIND THE PRODUCER THAT HAS THE HIGHEST TRUST SCORE!!!
////    return producerTrustScores.keySet().iterator().next();
////
////  }

}
