package com.trustsim.simulator.agents;

import com.trustsim.synthesiser.TransactionalVectorList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

  private int id;
  private String graphName;
  private final Map<Agent, List<Edge>> agents = new HashMap<>();
  private Map<Agent, TrustVectorList> agentIndirectTrustScores = new HashMap<>();

  public Graph(int id, String graphName) {
    this.id = id;
    this.graphName = graphName;
  }

  public double getLambda() {
    return 1; // IMPLEMENT THIS //
  }

  public double getDirectPartialTrustValue(Agent agent1, Agent agent2) {
    double successfulTransactions = this.getTransactionalVector(agent1, agent2).getNumberOfSuccessfulTransactions();
    double totalTransactions = this.getTransactionalVector(agent1, agent2).getNumberOfTransactions();

    return successfulTransactions / totalTransactions;
  }

  public List<Agent> getDirectConnections(Agent agent) {
    List<Edge> agentEdges = getEdges(agent);
    List<Agent> agentConnections = new ArrayList<>();
    for (Edge edge : agentEdges) {
      agentConnections.add(edge.getDest());
    }
    return agentConnections;
  }

  public List<List<Agent>> getAllRoutesBetweenAgents(Agent recommender, Agent agentBeingEvaluated) {
    List<List<Agent>> routes = new ArrayList<>();
    // IMPLEMENT THIS // NEEDS A BFS OF THE GRAPH'
    return routes;
  }


  public static class Edge {
    Agent src;
    Agent dest;
    TrustVectorList edgeTrust;
    TransactionalVectorList edgeTransactional;

    public Edge(Agent srcValue, Agent destValue) {
      this.src = srcValue;
      this.dest = destValue;
    }

    public Edge(Agent srcValue, Agent destValue, TrustVectorList trustVector, TransactionalVectorList transactionalVector) {
      this.src = srcValue;
      this.dest = destValue;
      this.edgeTrust = trustVector;
      this.edgeTransactional = transactionalVector;
    }

    public Agent getDest() {
      return dest;
    }

    public Agent getSrc() {
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

      Edge otherAgent = (Edge) o;
      return this.getSrc() == otherAgent.getSrc() && this.getDest() == otherAgent.getDest();
    }

  }

  public void addAgent(Agent agent) {
    if (!agents.containsKey(agent)) {
      agents.put(agent, new ArrayList<>());
    }
  }

  public void removeAgent(Agent agent) {

    // remove agent entry itself
    agents.remove(agent);

    // remove agent in List<Agent> of another agent
    for (Map.Entry<Agent, List<Edge>> iter : agents.entrySet()) {
      List<Edge> adjacentEdges = iter.getValue();

      for (Edge edge : adjacentEdges) {
        if (edge.getDest() == agent) {
          iter.getValue().remove(edge);
        }
      }
    }
  }

  public boolean containsAgent(Agent agent) {
    return agents.containsKey(agent);
  }

  public void addEdge(Agent agent1, Agent agent2) {

    // If Agent1 not in Graph -> add Agent1 to Graph
    if (!agents.containsKey(agent1)) {
      this.addAgent(agent1);
    }

    // If Agent2 not in Graph -> add Agent2 to Graph
    if (!agents.containsKey(agent2)) {
      this.addAgent(agent2);
    }

    // If No Edge exists between Agent1 and Agent2 -> Add Edge
    if (!hasDirectConnection(agent1, agent2)) {
      agents.get(agent1).add(new Edge(agent1, agent2));
    }

  }

  public void addTrustEdge(Agent agent1, Agent agent2, TrustVectorList vectors) {

    // Checks if Edge exists between Agent1 and Agent2, else adds an Edge
    addEdge(agent1, agent2);

    List<Edge> adjacentEdges = agents.get(agent1);

    for (Edge edge : adjacentEdges) {
      if (edge.getDest() == agent2) {
        edge.setTrustEdge(vectors);
      }
    }
  }

  public void addTransactionalEdge(Agent agent1, Agent agent2, TransactionalVectorList vectors) {

    // Checks if Edge exists between Agent1 and Agent2, else adds an Edge
    addEdge(agent1, agent2);

    List<Edge> adjacentEdges = agents.get(agent1);

    for (Edge edge : adjacentEdges) {
      if (edge.getDest() == agent2) {
        edge.setTransactionalEdge(vectors);
      }
    }
  }

  public boolean removeBothEdge(Agent agent1, Agent agent2) {

    if (!hasDirectConnection(agent1, agent2)) {
      return false;
    }

    agents.get(agent1).remove(new Edge(agent1, agent2));
    return true;
  }

  public TrustVectorList getIndirectTrustVector(Agent agent) {
    return agentIndirectTrustScores.get(agent);
  }

  public void setIndirectTrustVector(Agent agent, TrustVectorList vectorList) {
    agentIndirectTrustScores.put(agent, vectorList);
  }

  public TrustVectorList getDirectTrustVector(Agent agent1, Agent agent2) {

    if (hasDirectConnection(agent1, agent2)) {
      List<Edge> adjacentEdges = agents.get(agent1);
      for (Edge edge : adjacentEdges) {
        if (edge.getDest() == agent2) {
          return edge.getTrustEdge();
        }
      }
    }
    return null;
  }

  public TransactionalVectorList getTransactionalVector(Agent agent1, Agent agent2) {
    if (hasDirectConnection(agent1, agent2)) {
      List<Edge> adjacentEdges = agents.get(agent1);
      for (Edge edge : adjacentEdges) {
        if (edge.getDest() == agent2) {
          return edge.getTransactionalEdge();
        }
      }
    }
    return null;
  }

  public boolean hasDirectConnection(Agent agent1, Agent agent2) {
    // OPTION 1
    //return agents.get(agent1).stream().anyMatch(o -> Objects.equals(o.getSrc(), agent2));

    // OPTION 2
    List<Edge> adjacentEdges = agents.get(agent1);

    if (adjacentEdges == null) {
      return false;
    }

    for (Edge edge : adjacentEdges) {
      if (edge.getDest() == agent2) {
        return true;
      }
    }

    return false;
  }

  public List<Edge> getEdges(Agent agent) {
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

  public List<ConsumerAgent> getAllConsumerAgents() {

    List<ConsumerAgent> agentList = new ArrayList<>();

    for (Agent entry : agents.keySet()) {
      if (entry instanceof ConsumerAgent) {
        agentList.add((ConsumerAgent) entry);
      }
    }
    return agentList;
  }

  public List<ProducerAgent> getAllProducerAgents() {

    List<ProducerAgent> agentList = new ArrayList<>();

    for (Agent entry : agents.keySet()) {
      if (entry instanceof ProducerAgent) {
        agentList.add((ProducerAgent) entry);
      }
    }
    return agentList;
  }

}
