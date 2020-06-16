package com.trustsim.simulator.agents;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequest implements Comparable<ServiceRequest> {

  private ConsumerAgent consumerAgent = null;
  private ProducerAgent producerAgent = null;

  private boolean isCompleted = false;

  private int requestTimeStep = 0;
  private int completionTimeStep;

  private List<Double> serviceTrustCharacteristics = new ArrayList<>();

  public ServiceRequest(List<Double> serviceTrustCharacteristics, int requestTimeStep) {
    this.requestTimeStep = requestTimeStep;
    this.serviceTrustCharacteristics = serviceTrustCharacteristics;
  }

  public void setConsumerAgentOfRequest(ConsumerAgent consumerAgent) {
    this.consumerAgent = consumerAgent;
  }

  public void setProducerAgentOfRequest(ProducerAgent producerAgent) {
    this.producerAgent = producerAgent;
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


  @Override
  public int compareTo(ServiceRequest o) {
    return this.getRequestTime() - o.getRequestTime();
  }
}
