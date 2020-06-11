package com.trustsim.simulator.agents;

import com.trustsim.simulator.agents.FCTrustModel.FCTrustConsumerAgent;
import com.trustsim.simulator.agents.FCTrustModel.FCTrustProducerAgent;

import java.util.*;

public class Graph {

  private int id;
  private String graphName;
  private final Map<Agent, List<Edge<Agent>>> agents = new HashMap<>();

  public Graph() {
  }

  public static class Edge<V> {
    V src;
    V dest;
    TrustVectorList edgeWeight;

    public Edge(V srcValue, V destValue) {
      this.src = srcValue;
      this.dest = destValue;
    }

//    public Edge(V srcValue, V destValue, TrustVectorList vectors) {
//      this.src = srcValue;
//      this.dest = destValue;
//      this.edgeWeight = vectors;
//    }

    public V getDest() {
      return dest;
    }

    public V getSrc() {
      return src;
    }

    public TrustVectorList getEdgeWeight() {
      return edgeWeight;
    }

    public void setEdgeWeight(TrustVectorList weights) {
      edgeWeight = weights;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Edge<Agent> otherAgent = (Edge<Agent>) o;
      return this.getSrc() == otherAgent.getSrc() && this.getDest() == otherAgent.getDest();
    }

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
    for (Map.Entry<Agent, List<Edge<Agent>>> iter : agents.entrySet()) {
      List<Edge<Agent>> adjacentEdges = iter.getValue();

      for (Edge<Agent> edge : adjacentEdges) {
        if (edge.getDest() == agent) {
          iter.getValue().remove(edge);
        }
      }
    }
  }

  public boolean containsAgent(Agent agent) {
    return agents.containsKey(agent);
  }

  public void addEdge(Agent agent1, Agent agent2, TrustVectorList vectors) {

    if (!agents.containsKey(agent1)) {
      this.addAgent(agent1);
    }

    if (!agents.containsKey(agent2)) {
      this.addAgent(agent2);
    }

    List<Edge<Agent>> adjacentEdges = agents.get(agent1);

    for (Edge<Agent> edge : adjacentEdges) {
      if (edge.getSrc() == agent1) {
        edge.setEdgeWeight(vectors);
      }
    }
  }

  public boolean removeEdge(Agent agent1, Agent agent2) {

    if (!hasDirectConnection(agent1, agent2)) {
      return false;
    }

    agents.get(agent1).remove(new Edge<Agent>(agent1, agent2));
    return true;
  }

  public TrustVectorList getTrustVector(Agent agent1, Agent agent2) {
    if (hasDirectConnection(agent1, agent2)) {
      List<Edge<Agent>> adjacentEdges = agents.get(agent1);
      for (Edge<Agent> edge : adjacentEdges) {
        if (edge.getDest() == agent2) {
          return edge.getEdgeWeight();
        }
      }
    }
    return null;
  }

  public boolean hasDirectConnection(Agent agent1, Agent agent2) {

    List<Edge<Agent>> adjacentEdges = agents.get(agent1);
    for (Edge<Agent> edge : adjacentEdges) {
      if (edge.getDest() == agent2) {
        return true;
      }
    }

    return false;
  }

  public List<Edge<Agent>> getEdges(Agent agent) {
    return agents.get(agent);
  }

//  public int getIndirectTrustPathway(Agent agent1, Agent agent2) {
//    // implement this;
//    // depth-first search of the graph -> to find the maximum trust score route between agent1 and agent2
//    return 1;
//  }

  public List<Agent> getAllConsumerAgents() {

    List<Agent> agentList = new ArrayList<Agent>();

    for (Agent entry : agents.keySet()) {
      if (entry instanceof FCTrustConsumerAgent) {
        agentList.add(entry);
      }
    }
    return agentList;
  }

  public List<ProducerAgent> getAllProducerAgents() {

    List<ProducerAgent> agentList = new ArrayList<>();

    for (Agent entry : agents.keySet()) {
      if (entry instanceof FCTrustProducerAgent) {
        agentList.add((ProducerAgent) entry);
      }
    }
    return agentList;
  }

}
