package com.trustsim.simulator.agents;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequest {

  private ConsumerAgent consumerAgent = null;
  private ProducerAgent producerAgent = null;

  private boolean isCompleted = false;

  private int requestTimeStep;
  private int completionTimeStep;

  private List<Double> serviceTrustCharacteristics = new ArrayList<>();

  public ServiceRequest(List<Double> serviceTrustCharacteristics, int requestTimeStep) {
    this.requestTimeStep = requestTimeStep;
    this.serviceTrustCharacteristics = serviceTrustCharacteristics;
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

  public int getRequestTime() {
    return requestTimeStep;
  }

  public int getCompletionTime() {
    return completionTimeStep;
  }
}
