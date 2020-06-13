package com.trustsim.simulator.agents;

import com.trustsim.simulator.agents.WangTrustModel.WangTrustConsumerAgent;
import com.trustsim.simulator.agents.WangTrustModel.WangTrustProducerAgent;
import com.trustsim.synthesiser.TransactionalVectorList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

  private int id;
  private String graphName;
  private final Map<Agent, List<Edge<Agent, TrustVectorList, TransactionalVectorList>>> agents = new HashMap<>();

  public Graph() {
  }

  public double getLambda() {
    return 1; // IMPLEMENT THIS //
  }


  public static class Edge<V, TrustVectorList, TransactionalVectorList> {
    V src;
    V dest;
    TrustVectorList edgeTrust;
    TransactionalVectorList edgeTransactional;

    public Edge(V srcValue, V destValue) {
      this.src = srcValue;
      this.dest = destValue;
    }

    public Edge(V srcValue, V destValue, TrustVectorList trustVector, TransactionalVectorList transactionalVector) {
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

    public TrustVectorList getTrustEdge() {
      return edgeTrust;
    }

    public TransactionalVectorList getTransactionalEdge() {
      return edgeTransactional;
    }

    public void setTrustEdge(TrustVectorList weights) {
      edgeTrust = weights;
    }

    public void setTransactionalEdge(TransactionalVectorList weights) {
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

      Edge<Agent, TrustVectorList, TransactionalVectorList> otherAgent = (Edge<Agent, TrustVectorList, TransactionalVectorList>) o;
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
    for (Map.Entry<Agent, List<Edge<Agent, TrustVectorList, TransactionalVectorList>>> iter : agents.entrySet()) {
      List<Edge<Agent, TrustVectorList, TransactionalVectorList>> adjacentEdges = iter.getValue();

      for (Edge<Agent, TrustVectorList, TransactionalVectorList> edge : adjacentEdges) {
        if (edge.getDest() == agent) {
          iter.getValue().remove(edge);
        }
      }
    }
  }

  public boolean containsAgent(Agent agent) {
    return agents.containsKey(agent);
  }

  public void addTrustEdge(Agent agent1, Agent agent2, TrustVectorList vectors) {

    if (!agents.containsKey(agent1)) {
      this.addAgent(agent1);
    }

    if (!agents.containsKey(agent2)) {
      this.addAgent(agent2);
    }

    List<Edge<Agent, TrustVectorList, TransactionalVectorList>> adjacentEdges = agents.get(agent1);

    for (Edge<Agent, TrustVectorList, TransactionalVectorList> edge : adjacentEdges) {
      if (edge.getSrc() == agent1) {
        edge.setTrustEdge(vectors);
      }
    }
  }

  public void addTransactionalEdge(Agent agent1, Agent agent2, TransactionalVectorList vectors) {

    if (!agents.containsKey(agent1)) {
      this.addAgent(agent1);
    }

    if (!agents.containsKey(agent2)) {
      this.addAgent(agent2);
    }

    List<Edge<Agent, TrustVectorList, TransactionalVectorList>> adjacentEdges = agents.get(agent1);

    for (Edge<Agent, TrustVectorList, TransactionalVectorList> edge : adjacentEdges) {
      if (edge.getSrc() == agent1) {
        edge.setTransactionalEdge(vectors);
      }
    }
  }

  public boolean removeBothEdge(Agent agent1, Agent agent2) {

    if (!hasDirectConnection(agent1, agent2)) {
      return false;
    }

    agents.get(agent1).remove(new Edge<Agent, TrustVectorList, TransactionalVectorList>(agent1, agent2));
    return true;
  }

  public TrustVectorList getTrustVector(Agent agent1, Agent agent2) {
    if (hasDirectConnection(agent1, agent2)) {
      List<Edge<Agent, TrustVectorList, TransactionalVectorList>> adjacentEdges = agents.get(agent1);
      for (Edge<Agent, TrustVectorList, TransactionalVectorList> edge : adjacentEdges) {
        if (edge.getDest() == agent2) {
          return edge.getTrustEdge();
        }
      }
    }
    return null;
  }

  public TransactionalVectorList getTransactionalVector(Agent agent1, Agent agent2) {
    if (hasDirectConnection(agent1, agent2)) {
      List<Edge<Agent, TrustVectorList, TransactionalVectorList>> adjacentEdges = agents.get(agent1);
      for (Edge<Agent, TrustVectorList, TransactionalVectorList> edge : adjacentEdges) {
        if (edge.getDest() == agent2) {
          return edge.getTransactionalEdge();
        }
      }
    }
    return null;
  }

  public boolean hasDirectConnection(Agent agent1, Agent agent2) {

    List<Edge<Agent, TrustVectorList, TransactionalVectorList>> adjacentEdges = agents.get(agent1);
    for (Edge<Agent, TrustVectorList, TransactionalVectorList> edge : adjacentEdges) {
      if (edge.getDest() == agent2) {
        return true;
      }
    }

    return false;
  }

  public List<Edge<Agent, TrustVectorList, TransactionalVectorList>> getEdges(Agent agent) {
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
