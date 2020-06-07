package com.trustsim.synthesiser;

import com.trustsim.simulator.agents.Graph;

public class AgentSystem {

  private String systemName;
  private String dateCreated;
  private Graph agentGraph;
  private String parameter1 = "A";
  private String parameter2 = "A";
  private String parameter3 = "A";
  private String parameter4 = "A";


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

    return (systemName + "--" + dateCreated);
  }

  public void setParameter1Value(String parameter1) {
    this.parameter1 = parameter1;
  }
  public void setParameter2Value(String parameter2) {
    this.parameter2 = parameter2;
  }
  public void setParameter3Value(String parameter3) {
    this.parameter3 = parameter3;
  }
  public void setParameter4Value(String parameter4) {
    this.parameter4 = parameter4;
  }

  public String getParameter1() {
    return parameter1;
  }
  public String getParameter2() {
    return parameter2;
  }
  public String getParameter3() {
    return parameter3;
  }
  public String getParameter4() {
    return parameter4;
  }
}
