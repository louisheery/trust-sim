package com.trustsim.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;

/** Singleton Class used to interface with a JanusGraph Database. */
public class JanusGraphManager {

  public JanusGraph graph;
  public JanusGraphFactory.Builder builder;
  private int currentAgentId = 0;
  private static JanusGraphManager INSTANCE = null;

  /**
   * Constructor to initialise JanusGraphManager with default Cassandra and ElasticSearch local
   * server address.
   */
  public JanusGraphManager() {
    builder = JanusGraphFactory.build();
    builder.set("storage.hostname", "127.0.0.1");
    builder.set("storage.backend", "cql");
    builder.set("index.search.backend", "elasticsearch");
    builder.set("index.search.hostname", "127.0.0.1");
    builder.set("metrics.enabled", "false");
    graph = builder.open();
    initialiseJanusGraphSchema(false);
  }

  /**
   * Constructor to initialise JanusGraphManager with a specific Cassandra and ElasticSearch server
   * address.
   *
   * @param cassandraHostname cassandra host name
   * @param cassandraPort cassandra port
   * @param elasticsearchHostname elastic search host name
   * @param elasticsearchPort elastic search port
   */
  public JanusGraphManager(
      String cassandraHostname,
      int cassandraPort,
      String elasticsearchHostname,
      int elasticsearchPort) {

    builder = JanusGraphFactory.build();
    builder.set("storage.hostname", cassandraHostname + ":" + cassandraPort);
    builder.set("storage.backend", "cql");
    builder.set("index.search.backend", "elasticsearch");
    builder.set("index.search.hostname", elasticsearchHostname + ":" + elasticsearchPort);
    builder.set("metrics.enabled", "false");
    builder.set("metrics.jmx.enabled", "false");
    graph = builder.open();

    initialiseJanusGraphSchema(false);
  }

  /**
   * Singleton instance getter function.
   *
   * @return Reference to Singleton instance of this class
   */
  public static JanusGraphManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new JanusGraphManager();
    }
    return INSTANCE;
  }

  /**
   * Function to change JanusGraph database which Manager is connected to.
   *
   * @param cassandraHostname cassandra host name
   * @param cassandraPort cassandra port
   * @param elasticsearchHostname elastic search host name
   * @param elasticsearchPort elastic search port
   */
  public void setCassandraElasticSearchServerAndInitialise(
      String cassandraHostname,
      int cassandraPort,
      String elasticsearchHostname,
      int elasticsearchPort) {

    builder = JanusGraphFactory.build();
    builder.set("storage.hostname", cassandraHostname + ":" + cassandraPort);
    builder.set("storage.backend", "cql");
    builder.set("index.search.backend", "elasticsearch");
    builder.set("index.search.hostname", elasticsearchHostname + ":" + elasticsearchPort);
    graph = builder.open();

    initialiseJanusGraphSchema(false);
  }

  public JanusGraph getGraph() {
    return (JanusGraph) graph;
  }

  /**
   * Function to initialise JanusGraph with Schema used to store Trust System.
   *
   * @param forceRebuildSchema whether schema should overwrite an existing schema
   * @return whether schema initialisation was successful
   */
  public boolean initialiseJanusGraphSchema(boolean forceRebuildSchema) {

    JanusGraphManagement graphMgmt = graph.openManagement();
    graph.tx().rollback();
    try {
      if (!forceRebuildSchema
          && graphMgmt.getRelationTypes(RelationType.class).iterator().hasNext()) {
        graphMgmt.rollback();
        return true;
      }
      // DEFINE GRAPH PROPERTIES
      final PropertyKey agentId =
          graphMgmt.makePropertyKey("agentId").dataType(Integer.class).make();
      final PropertyKey isTrustedAgent =
          graphMgmt.makePropertyKey("isTrustedAgent").dataType(Boolean.class).make();
      final PropertyKey isConsumer =
          graphMgmt.makePropertyKey("isConsumer").dataType(Boolean.class).make();
      final PropertyKey isProducer =
          graphMgmt.makePropertyKey("isProducer").dataType(Boolean.class).make();
      final PropertyKey timeCreated =
          graphMgmt.makePropertyKey("timeCreated").dataType(Double.class).make();
      final PropertyKey successfulTransactions =
          graphMgmt.makePropertyKey("successfulTransactions").dataType(Integer.class).make();
      final PropertyKey unsuccessfulTransactions =
          graphMgmt.makePropertyKey("unsuccessfulTransactions").dataType(Integer.class).make();
      final PropertyKey transactionHistory =
          graphMgmt.makePropertyKey("transactionHistory").dataType(HashMap.class).make();
      final PropertyKey abilitiesArray =
          graphMgmt.makePropertyKey("abilitiesArray").dataType(double[].class).make();
      final PropertyKey directTrustValue =
          graphMgmt.makePropertyKey("directTrustValue").dataType(Double.class).make();
      final PropertyKey globalTrustValue =
          graphMgmt.makePropertyKey("globalTrustValue").dataType(Double.class).make();
      final PropertyKey indirectTrustValue =
          graphMgmt.makePropertyKey("indirectTrustValue").dataType(Double.class).make();
      final PropertyKey personalityTrustValue =
          graphMgmt.makePropertyKey("personalityTrustValue").dataType(Double.class).make();

      // DEFINE COMPOSITE INDEXES
      JanusGraphManagement.IndexBuilder agentIdIndexBuilder =
          graphMgmt.buildIndex("agentId", Vertex.class).addKey(agentId);

      agentIdIndexBuilder.unique();

      JanusGraphIndex agentIdIndex = agentIdIndexBuilder.buildCompositeIndex();
      graphMgmt.setConsistency(agentIdIndex, ConsistencyModifier.LOCK);

      // DEFINE VERTEX LABELS
      graphMgmt.makeVertexLabel("agent").make();

      // DEFINE EDGE LABELS
      graphMgmt.makeEdgeLabel("connection").multiplicity(Multiplicity.SIMPLE).make();

      graphMgmt.commit();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Function to add an Agent Node to JanusGraph database.
   *
   * @param personalityTrustValue
   * @param agentAbilities
   * @param isConsumerAgent
   * @param isProducerAgent
   * @param isTrustedAgent
   * @param timeCreated
   * @param globalTrustValue
   * @return id of agent added to database
   * @throws Exception thrown by JanusGraph
   */
  public int addAgent(
      double personalityTrustValue,
      double[] agentAbilities,
      boolean isConsumerAgent,
      boolean isProducerAgent,
      boolean isTrustedAgent,
      double timeCreated,
      double globalTrustValue)
      throws Exception {

    JanusGraphTransaction tx = null;
    tx = graph.newTransaction();

    try {
      int i = currentAgentId;
      tx.addVertex(
          T.label,
          "agent",
          "agentId",
          i,
          "globalTrustValue",
          0,
          "isTrustedAgent",
          isTrustedAgent,
          "isConsumer",
          isConsumerAgent,
          "isProducer",
          isProducerAgent,
          "timeCreated",
          timeCreated,
          "personalityTrustValue",
          personalityTrustValue,
          "agentAbilityValues",
          agentAbilities,
          "globalTrustValue",
          globalTrustValue);

      tx.commit();
      currentAgentId++;
      return i;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * Function to add multiple Agent Nodes to JanusGraph database.
   *
   * @param numberOfConsumers
   * @param numberOfProducers
   * @param timeCreated
   * @throws Exception thrown by JanusGraph
   */
  public void addAgents(int numberOfConsumers, int numberOfProducers, double timeCreated)
      throws Exception {

    for (int j = 0; j < numberOfConsumers; j++) {
      addAgent(
          Math.random(),
          new double[] {Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},
          true,
          false,
          true,
          timeCreated,
          0);
    }
    for (int k = 0; k < numberOfProducers; k++) {
      addAgent(
          Math.random(),
          new double[] {Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},
          false,
          true,
          false,
          timeCreated,
          0);
    }
  }

  /**
   * Function to add Edge connection between two Agent Nodes in JanusGraph database.
   *
   * @param agentId1
   * @param agentId2
   * @param directTrustValue
   * @param transactionHistory
   * @param successfulTransactions
   * @param unsuccessfulTransactions
   * @return whether edge connection was added successfully
   * @throws Exception thrown by JanusGraphManagement
   */
  public boolean addConnection(
      Integer agentId1,
      Integer agentId2,
      Double directTrustValue,
      Object transactionHistory,
      Integer successfulTransactions,
      Integer unsuccessfulTransactions)
      throws Exception {

    JanusGraphManagement graphMgmt = graph.openManagement();
    GraphTraversalSource gts = graph.traversal();

    Vertex primaryNode = null;
    Vertex receiverNode = null;

    try {
      primaryNode = gts.V().has("agentId", agentId1).next();
      receiverNode = gts.V().has("agentId", agentId2).next();
    } catch (NoSuchElementException e) {
      return false;
    }

    if (primaryNode != null && receiverNode != null) {
      Edge connection = primaryNode.addEdge("connection", receiverNode);
      if (directTrustValue != null) {
        connection.property("directTrustValue", directTrustValue);
      }
      if (transactionHistory != null) {
        connection.property("transactionHistory", transactionHistory);
      }
      if (successfulTransactions != null) {
        connection.property("successfulTransactions", successfulTransactions);
      }
      if (unsuccessfulTransactions != null) {
        connection.property("unsuccessfulTransactions", unsuccessfulTransactions);
      }

      graphMgmt.commit();
      gts.tx().commit();
      gts.close();

      return true;

    } else {
      graphMgmt.commit();
      gts.tx().commit();
      gts.close();

      return false;
    }
  }

  /**
   * Function to get list of ids of all agents in JanusGraph database.
   *
   * @return list of agent ids
   */
  public List<Integer> getAgentIds() {

    GraphTraversalSource g = graph.traversal();
    List<Object> agentIds = g.V().values("agentId").toList();
    return agentIds.stream()
        .map(object -> Integer.parseInt(Objects.toString(object, null)))
        .collect(Collectors.toList());
  }

  /**
   * Function to get list of ids of all agents which have a specific property value for a specific
   * property type.
   *
   * @param propertyType
   * @param propertyValue
   * @return list of agent ids
   */
  public List<Integer> getAgentIdsWithProperty(String propertyType, Object propertyValue) {

    GraphTraversalSource g = graph.traversal();
    List<Object> agentIds = new ArrayList<>();

    if (propertyType.equals("isTrustedAgent")
        || propertyType.equals("timeCreated")
        || propertyType.equals("transactionHistory")
        || propertyType.equals("abilitiesArray")
        || propertyType.equals("directTrustValue")
        || propertyType.equals("globalTrustValue")
        || propertyType.equals("indirectTrustValue")
        || propertyType.equals("personalityTrustValue")
        || propertyType.equals("isConsumer")
        || propertyType.equals("isProducer")
        || propertyType.equals("successfulTransactions")
        || propertyType.equals("unsuccessfulTransactions")) {

      agentIds = g.V().has(propertyType, propertyValue).values("agentId").toList();

      return agentIds.stream()
          .map(object -> Integer.parseInt(Objects.toString(object, null)))
          .collect(Collectors.toList());
    } else {
      return null;
    }
  }

  /**
   * Function returns either the TransactionHistory or DirectTrustValue between AgentId1 of
   * AgentId2.
   *
   * @param agentId1 transmitting agent id
   * @param agentId2 receiving agent id
   * @param propertyType property type of edge
   * @return value of edge property
   */
  public Object getConnectionProperty(Integer agentId1, Integer agentId2, String propertyType) {

    GraphTraversalSource g = graph.traversal();

    try {
      GraphTraversal<Vertex, Edge> node =
          g.V().has("agentId", agentId1).outE().where(__.inV().has("agentId", agentId2));

      if (propertyType.equals("directTrustValue")
          || propertyType.equals("transactionHistory")
          || propertyType.equals("successfulTransactions")
          || propertyType.equals("unsuccessfulTransactions")) {
        List<Object> values = node.values(propertyType).toList();
        if (values.size() > 0) {
          return values.get(0);
        } else {
          return null;
        }
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Function to change the value of a property of a connection between two agents in the graph.
   *
   * @param agentId1
   * @param agentId2
   * @param propertyType
   * @param propertyValue
   * @param appendTrueReplaceFalse
   * @return whether connection was successfully modified
   * @throws Exception thrown by JanusGraphManagement
   */
  public boolean modifyConnectionProperty(
      Integer agentId1,
      Integer agentId2,
      String propertyType,
      Object propertyValue,
      boolean appendTrueReplaceFalse)
      throws Exception {

    JanusGraphManagement graphMgmt = graph.openManagement();
    GraphTraversalSource gts = graph.traversal();

    Vertex primaryNode;
    Vertex receiverNode;

    try {
      primaryNode = gts.V().has("agentId", agentId1).next();
      receiverNode = gts.V().has("agentId", agentId2).next();
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      return false;
    }

    if (primaryNode != null && receiverNode != null) {

      if (!areAgentsConnected(agentId1, agentId2)) {
        addConnection(
            agentId1,
            agentId2,
            propertyType.equals("directTrustValue") ? (Double) propertyValue : null,
            propertyType.equals("transactionHistory") ? propertyValue : null,
            propertyType.equals("successfulTransactions") ? (Integer) propertyValue : 0,
            propertyType.equals("unsuccessfulTransactions") ? (Integer) propertyValue : 0);
      }

      if (propertyType.equals("directTrustValue") && propertyValue != null) {
        gts.V()
            .has("agentId", agentId1)
            .outE()
            .where(__.inV().has("agentId", agentId2))
            .property("directTrustValue", propertyValue)
            .next();
      } else if (propertyType.equals("globalTrustValue") && propertyValue != null) {
        gts.V()
            .has("agentId", agentId1)
            .outE()
            .where(__.inV().has("agentId", agentId2))
            .property("globalTrustValue", propertyValue)
            .next();
      } else if (propertyType.equals("successfulTransactions") && propertyType != null) {
        Integer previousSuccessfulTransactions =
            (Integer) getConnectionProperty(agentId1, agentId2, propertyType);
        Integer newSuccessfulTransactions =
            previousSuccessfulTransactions + (Integer) propertyValue;
        gts.V()
            .has("agentId", agentId1)
            .outE()
            .where(__.inV().has("agentId", agentId2))
            .property("successfulTransactions", newSuccessfulTransactions)
            .next();
      } else if (propertyType.equals("unsuccessfulTransactions") && propertyType != null) {
        Integer previousUnsuccessfulTransactions =
            (Integer) getConnectionProperty(agentId1, agentId2, propertyType);
        Integer newUnsuccessfulTransactions =
            previousUnsuccessfulTransactions + (Integer) propertyValue;
        gts.V()
            .has("agentId", agentId1)
            .outE()
            .where(__.inV().has("agentId", agentId2))
            .property("unsuccessfulTransactions", newUnsuccessfulTransactions)
            .next();
      } else if (propertyType.equals("transactionHistory") && propertyValue != null) {
        if (appendTrueReplaceFalse) {
          HashMap<Double, Boolean> transactionHistory =
              (HashMap<Double, Boolean>) getConnectionProperty(agentId1, agentId2, propertyType);
          if (transactionHistory == null) {
            transactionHistory = new HashMap<Double, Boolean>();
          }
          for (Map.Entry<Double, Boolean> entry :
              ((HashMap<Double, Boolean>) propertyValue).entrySet()) {
            transactionHistory.put(entry.getKey(), entry.getValue());
          }

          gts.V()
              .has("agentId", agentId1)
              .outE()
              .where(__.inV().has("agentId", agentId2))
              .property("transactionHistory", transactionHistory)
              .iterate();
        } else {
          gts.V()
              .has("agentId", agentId1)
              .outE()
              .where(__.inV().has("agentId", agentId2))
              .property("transactionHistory", propertyValue)
              .iterate();
        }
      }
    }

    graphMgmt.commit();
    gts.tx().commit();
    gts.close();

    return true;
  }

  /**
   * Function to get value of an agent property in the database.
   *
   * @param agentId agent id
   * @param propertyType property type to be obtained
   * @return agent property value
   */
  public Object getAgentProperty(Integer agentId, String propertyType) {

    GraphTraversalSource gts = graph.traversal();
    Vertex agent = gts.V().has("agentId", agentId).next();

    if (agent != null
        && (propertyType.equals("isTrustedAgent")
            || propertyType.equals("timeCreated")
            || propertyType.equals("abilitiesArray")
            || propertyType.equals("globalTrustValue")
            || propertyType.equals("indirectTrustValue")
            || propertyType.equals("personalityTrustValue")
            || propertyType.equals("isConsumer")
            || propertyType.equals("isProducer"))) {
      return gts.V().has("agentId", agentId).next().value(propertyType);
    } else {
      return null;
    }
  }

  /**
   * Function to modify the property of a particular agent.
   *
   * @param agentId agent id
   * @param propertyType property type to be modified
   * @param propertyValue new property value for this property type
   * @param appendTrueReplaceFalse whether property value should be appended or replace existing
   *     value.
   * @return whether agent property was successfully modified
   * @throws Exception thrown by JanusGraphManagement
   */
  public boolean modifyAgentProperty(
      Integer agentId, String propertyType, Object propertyValue, boolean appendTrueReplaceFalse)
      throws Exception {

    JanusGraphManagement graphMgmt = graph.openManagement();
    GraphTraversalSource gts = graph.traversal();

    Vertex agent = null;

    try {
      agent = gts.V().has("agentId", agentId).next();
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      return false;
    }

    if (agent == null || propertyType == null) {
      graphMgmt.commit();
      gts.tx().commit();
      gts.close();
      return false;
    }

    if (propertyType.equals("isTrustedAgent")
        || propertyType.equals("timeCreated")
        || propertyType.equals("abilitiesArray")
        || propertyType.equals("globalTrustValue")
        || propertyType.equals("indirectTrustValue")
        || propertyType.equals("personalityTrustValue")
        || propertyType.equals("isConsumer")
        || propertyType.equals("isProducer")) {
      agent.property(propertyType, propertyValue);

      graphMgmt.commit();
      gts.tx().commit();
      gts.close();

      return true;
    } else {
      graphMgmt.commit();
      gts.tx().commit();
      gts.close();

      return false;
    }
  }

  /**
   * Function to clear all data currently stored in JanusGraph database.
   *
   * @param forceRebuildSchema whether graph schema should also be reinitialised
   * @throws Exception thrown by JanusGraphManager
   */
  public void eraseAndRestartJanusGraph(boolean forceRebuildSchema) throws Exception {
    graph = null;
    graph = builder.open();
    currentAgentId = 0;

    initialiseJanusGraphSchema(forceRebuildSchema);
  }

  /**
   * Function to delete one or both of the vertices or edges in the database.
   *
   * @param deleteVertices whether vertices should be deleted
   * @param deleteEdges whether edges should be deleted
   * @throws Exception thrown by JanusGraphManager
   */
  public void deleteGraphItems(boolean deleteVertices, boolean deleteEdges) throws Exception {
    GraphTraversalSource g = graph.traversal();
    if (deleteVertices) {
      g.V().drop().iterate();
    }

    if (deleteEdges) {
      g.E().drop().iterate();
    }

    g.tx().commit();
    g.close();
  }

  // Only used for testing purposes

  /**
   * Function to get the value of a property for all agents in the database.
   *
   * @param propertyType property type to be obtained
   * @return HashMap of Agent Id as Key, and Property Value of Property Type as Value
   */
  public HashMap<Integer, Object> getAgentPropertyOfAllAgents(String propertyType) {

    HashMap<Integer, Object> result = new HashMap<>();

    if (propertyType.equals("isTrustedAgent")
        || propertyType.equals("timeCreated")
        || propertyType.equals("transactionHistory")
        || propertyType.equals("abilitiesArray")
        || propertyType.equals("directTrustValue")
        || propertyType.equals("globalTrustValue")
        || propertyType.equals("indirectTrustValue")
        || propertyType.equals("personalityTrustValue")
        || propertyType.equals("isConsumer")
        || propertyType.equals("isProducer")
        || propertyType.equals("successfulTransactions")
        || propertyType.equals("unsuccessfulTransactions")) {
      List<Integer> allAgents = getAgentIds();

      if (allAgents.size() > 0) {
        for (Integer agent : allAgents) {
          result.put(agent, getAgentProperty(agent, propertyType));
        }
        return result;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Function returns whether two agents are directly connected to each other.
   *
   * @param agentId1 agent id of first agent
   * @param agentId2 agent id of second agent
   * @return whether agents are connected
   */
  public boolean areAgentsConnected(Integer agentId1, Integer agentId2) {
    return getAgentOutgoingConnectionsId(agentId1).contains(agentId2);
  }

  /**
   * Function returns ids of all incoming connections to an agent.
   *
   * @param agentId agent id of destination agent
   * @return list of incoming connection agent ids
   */
  public List<Integer> getAgentIncomingConnectionsIds(Integer agentId) {
    GraphTraversalSource gts = graph.traversal();

    try {
      Object id = gts.V().has("agentId", agentId).next();

      if (id != null) {
        List<Object> agentIds = gts.V(id).inE().otherV().values("agentId").toList();
        return agentIds.stream()
            .map(object -> Integer.parseInt(object.toString()))
            .collect(Collectors.toList());
      } else {
        return null;
      }
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Function returns ids of all outgoing connections to an agent.
   *
   * @param agentId agent id of originating agent
   * @return list of outgoing connection agent ids
   */
  public List<Integer> getAgentOutgoingConnectionsId(Integer agentId) {

    GraphTraversalSource gts = graph.traversal();

    try {
      Object id = gts.V().has("agentId", agentId).next();

      if (id != null) {
        List<Object> agentIds = gts.V(id).outE().otherV().values("agentId").toList();
        return agentIds.stream()
            .map(object -> Integer.parseInt(object.toString()))
            .collect(Collectors.toList());
      } else {
        return null;
      }
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      return null;
    }
  }
}
