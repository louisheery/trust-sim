package com.trustsim.simulator.simulationmanager;

import java.util.List;
import org.json.JSONObject;

/**
 * Class representing a Service Request event, that is a Service Request. That is an object which
 * represents a singular transaction between a consumer agent and a producer agent. The status of
 * this transaction is boolean.
 */
public class ServiceRequest implements Comparable<ServiceRequest> {

  protected Integer consumerAgentId = null;
  protected Integer producerAgentId = null;
  protected boolean isCompleted = false;
  protected int requestTime = 0;
  protected int completionTime = 0;
  protected Integer serviceType = 0;
  protected List<Double> serviceTrustCharacteristics;

  /**
   * Function used to generate a JSON representation of this object, for use in exportation to JSON
   * file.
   *
   * @return JSON object
   */
  public JSONObject toJsonObject() {

    JSONObject object = new JSONObject();
    object.put("consumerAgentID", consumerAgentId);
    object.put("producerAgentID", producerAgentId);
    object.put("isCompleted", isCompleted);
    object.put("requestTime", requestTime);
    object.put("completionTime", completionTime);
    object.put("serviceType", serviceType);
    return object;
  }

  public ServiceRequest(Integer serviceType) {
    this.serviceType = serviceType;
  }

  public ServiceRequest(List<Double> serviceTrustCharacteristics, int requestTime) {
    this.requestTime = requestTime;
    this.serviceTrustCharacteristics = serviceTrustCharacteristics;
  }

  public List<Double> getServiceTrustCharacteristics() {
    return serviceTrustCharacteristics;
  }

  public void setConsumer(Integer consumerAgentId) {
    this.consumerAgentId = consumerAgentId;
  }

  public void setProducer(Integer producerAgentId) {
    this.producerAgentId = producerAgentId;
  }

  public Integer getConsumer() {
    return consumerAgentId;
  }

  public Integer getProducer() {
    return producerAgentId;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public void setTransactionOutcome(boolean isTransactionCompletedSuccessfully) {
    isCompleted = isTransactionCompletedSuccessfully;
  }

  public int getRequestTime() {
    return requestTime;
  }

  public int getCompletionTime() {
    return completionTime;
  }

  public void setRequestTime(int requestTime) {
    this.requestTime = requestTime;
  }

  public void setCompletionTime(int time) {
    this.completionTime = time;
  }

  @Override
  public int compareTo(ServiceRequest o) {
    return this.getRequestTime() - o.getRequestTime();
  }
}
