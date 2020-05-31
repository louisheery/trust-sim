package com.trustsim.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

  private Map<Agent, List<Agent>> agents;

  public Graph() {
    agents = new HashMap<Agent, List<Agent>>();
  }

  public void addAgent(Agent agent) {
    if (!agents.containsKey(agent)) {
      agents.put(agent, null);
    }
  }

  public void removeAgent(Agent agent) {
    agents.remove(agent);
  }

  public boolean containsAgent(Agent agent) {
    return agents.containsKey(agent);
  }

  public void addEdge(Agent a, Agent b) {

  }

  public void removeEdge(Agent a, Agent b) {

  }

  public List<Agent> getEdges(Agent agent) {
    return agents.get(agent);
  }


}
