package com.trustsim.simulator.agents;

import com.trustsim.simulator.agents.WangTrustModel.WangTrustConsumerAgent;
import com.trustsim.simulator.agents.WangTrustModel.WangTrustProducerAgent;

import java.util.*;

public class Graph<E1, E2> {

  private int id;
  private String graphName;
  private final Map<Agent, List<Edge<Agent, E1, E2>>> agents = new HashMap<>();

  public Graph() {
  }

  public static class Edge<V, E1, E2> {
    V src;
    V dest;
    E1 edgeTrust;
    E2 edgeTransactional;

    public Edge(V srcValue, V destValue) {
      this.src = srcValue;
      this.dest = destValue;
    }

    public Edge(V srcValue, V destValue, E1 trustVector, E2 transactionalVector) {
      this.src = srcValue;
      this.dest = destValue;
      this.edgeTrust = trustVector;
      this.edgeTransactional = transactionalVector;
    }

    public V getDest() {
      return dest;
    }

    public V getSrc() {
      return src;
    }

    public E1 getTrustEdge() {
      return edgeTrust;
    }

    public E2 getTransactionalEdge() {
      return edgeTransactional;
    }

    public void setTrustEdge(E1 weights) {
      edgeTrust = weights;
    }

    public void setTransactionalEdge(E2 weights) {
      edgeTransactional = weights;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Edge<Agent, E1, E2> otherAgent = (Edge<Agent, E1, E2>) o;
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
    for (Map.Entry<Agent, List<Edge<Agent, E1, E2>>> iter : agents.entrySet()) {
      List<Edge<Agent, E1, E2>> adjacentEdges = iter.getValue();

      for (Edge<Agent, E1, E2> edge : adjacentEdges) {
        if (edge.getDest() == agent) {
          iter.getValue().remove(edge);
        }
      }
    }
  }

  public boolean containsAgent(Agent agent) {
    return agents.containsKey(agent);
  }

  public void addTrustEdge(Agent agent1, Agent agent2, E1 vectors) {

    if (!agents.containsKey(agent1)) {
      this.addAgent(agent1);
    }

    if (!agents.containsKey(agent2)) {
      this.addAgent(agent2);
    }

    List<Edge<Agent, E1, E2>> adjacentEdges = agents.get(agent1);

    for (Edge<Agent, E1, E2> edge : adjacentEdges) {
      if (edge.getSrc() == agent1) {
        edge.setTrustEdge(vectors);
      }
    }
  }

  public void addTransactionalEdge(Agent agent1, Agent agent2, E2 vectors) {

    if (!agents.containsKey(agent1)) {
      this.addAgent(agent1);
    }

    if (!agents.containsKey(agent2)) {
      this.addAgent(agent2);
    }

    List<Edge<Agent, E1, E2>> adjacentEdges = agents.get(agent1);

    for (Edge<Agent, E1, E2> edge : adjacentEdges) {
      if (edge.getSrc() == agent1) {
        edge.setTransactionalEdge(vectors);
      }
    }
  }

  public boolean removeBothEdge(Agent agent1, Agent agent2) {

    if (!hasDirectConnection(agent1, agent2)) {
      return false;
    }

    agents.get(agent1).remove(new Edge<Agent, E1, E2>(agent1, agent2));
    return true;
  }

  public E1 getTrustVector(Agent agent1, Agent agent2) {
    if (hasDirectConnection(agent1, agent2)) {
      List<Edge<Agent, E1, E2>> adjacentEdges = agents.get(agent1);
      for (Edge<Agent, E1, E2> edge : adjacentEdges) {
        if (edge.getDest() == agent2) {
          return edge.getTrustEdge();
        }
      }
    }
    return null;
  }

  public E2 getTransactionalVector(Agent agent1, Agent agent2) {
    if (hasDirectConnection(agent1, agent2)) {
      List<Edge<Agent, E1, E2>> adjacentEdges = agents.get(agent1);
      for (Edge<Agent, E1, E2> edge : adjacentEdges) {
        if (edge.getDest() == agent2) {
          return edge.getTransactionalEdge();
        }
      }
    }
    return null;
  }

  public boolean hasDirectConnection(Agent agent1, Agent agent2) {

    List<Edge<Agent, E1, E2>> adjacentEdges = agents.get(agent1);
    for (Edge<Agent, E1, E2> edge : adjacentEdges) {
      if (edge.getDest() == agent2) {
        return true;
      }
    }

    return false;
  }

  public List<Edge<Agent, E1, E2>> getEdges(Agent agent) {
    return agents.get(agent);
  }

//  public int getIndirectTrustPathway(Agent agent1, Agent agent2) {
//    // implement this;
//    // depth-first search of the graph -> to find the maximum trust score route between agent1 and agent2
//    return 1;
//  }

  public List<Agent> getAllAgents() {
    return new ArrayList<>(agents.keySet());
  }

  public List<Agent> getAllConsumerAgents() {

    List<Agent> agentList = new ArrayList<>();

    for (Agent entry : agents.keySet()) {
      if (entry instanceof WangTrustConsumerAgent) {
        agentList.add(entry);
      }
    }
    return agentList;
  }

  public List<ProducerAgent> getAllProducerAgents() {

    List<ProducerAgent> agentList = new ArrayList<>();

    for (Agent entry : agents.keySet()) {
      if (entry instanceof WangTrustProducerAgent) {
        agentList.add((ProducerAgent) entry);
      }
    }
    return agentList;
  }

}
