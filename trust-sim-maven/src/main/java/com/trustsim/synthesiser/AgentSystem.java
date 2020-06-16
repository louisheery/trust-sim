package com.trustsim.synthesiser;

import java.io.Serializable;

public class AgentSystem implements Serializable {

  private String systemName;
  private Integer numberOfConsumersParameter = 1;
  private Integer numberOfProducersParameter = 1;
  private Integer numberOfServiceRequests = 1;
  private Integer parameter4 = 0;


  public AgentSystem(String systemName) {
    this.systemName = systemName;
    this.numberOfConsumersParameter = 1;
    this.numberOfProducersParameter = 1;
    this.numberOfServiceRequests = 1;
  }

  public AgentSystem(String systemName, String numberOfConsumers, String numberOfProducers, String numberOfServiceRequests) {
    this.systemName = new String(systemName);
    this.numberOfConsumersParameter = Integer.parseInt(numberOfConsumers);
    this.numberOfProducersParameter = Integer.parseInt(numberOfProducers);
    this.numberOfServiceRequests = Integer.parseInt(numberOfServiceRequests);
  }

  public String getSystemName() {
    return systemName;
  }

  public void setSystemName(String systemNameInput) {
    systemName = systemNameInput;
  }

  @Override
  public String toString() {

    return (systemName);
  }

  public void setNumberOfConsumersParameter(Integer value) {
    this.numberOfConsumersParameter = value;
  }

  public void setNumberOfProducersParameter(Integer value) {
    this.numberOfProducersParameter = value;
  }

  public void setNumberOfServiceRequests(Integer value) {
    this.numberOfServiceRequests = value;
  }

  public void setParameter4Value(Integer value) {
    this.parameter4 = value;
  }

  public Integer getNumberOfConsumersParameter() {
    return numberOfConsumersParameter;
  }

  public Integer getNumberOfProducersParameter() {
    return numberOfProducersParameter;
  }

  public Integer getNumberOfServiceRequests() {
    return numberOfServiceRequests;
  }

  public Integer getParameter4() {
    return parameter4;
  }
}
