package com.trustsim.simulator.agents;

import com.trustsim.TrustSim;

import static java.lang.System.arraycopy;

public class ServiceRequest {

  private ConsumerAgent consumerAgent = null;
  private ProducerAgent producerAgent = null;

  private boolean isCompleted = false;

  private Double[] serviceTrustCharacteristics = new Double[TrustSim.NUM_OF_TRUST_DIMS];

  public ServiceRequest(Double[] serviceTrustCharacteristics) {

    arraycopy(serviceTrustCharacteristics, 0,
        this.serviceTrustCharacteristics, 0, serviceTrustCharacteristics.length);
  }

  public void assignConsumerAgentToRequest(ConsumerAgent agent) {
    this.consumerAgent = agent;
  }

  public void assignProducerAgentToRequest(ProducerAgent agent) {
    this.producerAgent = agent;
  }

  public ConsumerAgent getConsumerAgentOfRequest() {
    return consumerAgent;
  }

  public ProducerAgent getProducerAgentOfRequest() {
    return producerAgent;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public void markWhetherCompleted(boolean isTransactionCompletedSuccessfully) {
    isCompleted = isTransactionCompletedSuccessfully;
  }
}
