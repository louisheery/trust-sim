package com.trustsim.simulator.agents;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

  private int id;
  private String graphName;
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

    // remove agent entry itself
    agents.remove(agent);

    // remove agent in List<Agent> of another agent
    for (Map.Entry<Agent, List<Agent>> iter : agents.entrySet()) {
      iter.getValue().remove(agent);
    }

  }

  public boolean containsAgent(Agent agent) {
    return agents.containsKey(agent);
  }

  public void addEdge(Agent agent1, Agent agent2) {

    if (agents.containsKey(agent1)) {
      agents.get(agent1).add(agent2);
    } else {
      agents.put(agent1, Arrays.asList(agent2));
    }

    if (agents.containsKey(agent2)) {
      agents.get(agent2).add(agent1);
    } else {
      agents.put(agent2, Arrays.asList(agent1));
    }

  }

  public boolean removeEdge(Agent agent1, Agent agent2) {

    if (!hasDirectConnection(agent1, agent2)) {
      return false;
    }

    agents.get(agent1).remove(agent2);
    agents.get(agent2).remove(agent1);
    return true;
  }

  public boolean hasDirectConnection(Agent agent1, Agent agent2) {
    return agents.get(agent1).contains(agent2) && agents.get(agent2).contains(agent1);
  }

  public List<Agent> getEdges(Agent agent) {
    return agents.get(agent);
  }

  public int getIndirectTrustPathway(Agent agent1, Agent agent2) {
    // implement this;
    // depth-first search of the graph -> to find the maximum trust score route between agent1 and agent2
    return 1;
  }

}
