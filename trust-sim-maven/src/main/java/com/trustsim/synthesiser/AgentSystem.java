package com.trustsim.synthesiser;

import com.trustsim.simulator.agents.Graph;

import java.io.Serializable;

public class AgentSystem implements Serializable {

  private String systemName;
  private String dateCreated;
  private Graph agentGraph;
  private Integer numberOfConsumersParameter = 1;
  private Integer numberOfProducersParameter = 1;
  private Integer numberOfServiceRequests = 1;
  private Integer parameter4 = 0;


  public AgentSystem() {

  }

  public AgentSystem(String systemName, String dateCreated, Graph agentGraph) {
    this.systemName = new String(systemName);
    this.dateCreated = new String(dateCreated);
    this.agentGraph = agentGraph;
  }

  public String getSystemName() {
    return systemName;
  }

  public String getDateCreated() {
    return dateCreated;
  }

  public void setSystemName(String systemNameInput) {
    systemName = systemNameInput;
  }

  public void setDateCreated(String dateCreatedInput) {
    dateCreated = dateCreatedInput;
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
