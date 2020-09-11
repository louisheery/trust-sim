package com.trustsim.storage;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.EdgeLabel;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.Namifiable;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JanusGraphManagerTest {

    protected static JanusGraphManager graphManager;
    protected static GraphTraversalSource gts;
    protected static JanusGraph graph;
    protected static JanusGraphManagement graphMgmt;

    @BeforeAll
    public static void setUpAll() throws Exception {
        graphManager = new JanusGraphManager();
        graph = graphManager.getGraph();
        gts = graph.traversal();
        graphMgmt = graph.openManagement();
        graphManager.eraseAndRestartJanusGraph(false);
        graphManager.deleteGraphItems(true,true);
    }

    @AfterAll
    public static void tearDownAll() {
        graphManager = null;
    }

    @Test
    public void checkSpecifyingJanusGraphConfiguration() throws Exception {

        JanusGraphManager customServerJanusGraphManager = new JanusGraphManager("127.0.0.1", 9042, "127.0.0.1", 9200);
        customServerJanusGraphManager.eraseAndRestartJanusGraph(false);
        customServerJanusGraphManager.deleteGraphItems(true,true);
        customServerJanusGraphManager.addAgents(2, 2,0);

        GraphTraversalSource gtsCustom = customServerJanusGraphManager.getGraph().traversal();

        // Testing Direct Graph Interface
        assertEquals(4L, gtsCustom.V().count().next().longValue());

    }


    @Test
    public void addAgentsToGraphTest() throws Exception {

        graphManager.eraseAndRestartJanusGraph(false);
        graphManager.deleteGraphItems(true,true);
        graphManager.addAgents(10, 12,0);

        List<Integer> agentExpectedIds = new ArrayList<>(Arrays.asList(0, 1));
        List<Integer> agentExpectedIdsA = new ArrayList<>(Arrays.asList(0, 1));

        // Testing Graph Manager Inbuilt Functions
        assertEquals(agentExpectedIdsA, agentExpectedIds);
        assertEquals(10, graphManager.getAgentIdsWithProperty("isConsumer", true).size());
        assertEquals(12, graphManager.getAgentIdsWithProperty("isProducer", true).size());
        assertEquals(22, graphManager.getAgentIds().size());
        assertEquals((Integer) 0,  Collections.min(graphManager.getAgentIds()));
        assertEquals((Integer) 21, Collections.max(graphManager.getAgentIds()));
        assertEquals((Integer) 0, Collections.min(graphManager.getAgentIdsWithProperty("isConsumer", true)));
        assertEquals((Integer) 9, Collections.max(graphManager.getAgentIdsWithProperty("isConsumer", true)));
        assertEquals((Integer) 10, Collections.min(graphManager.getAgentIdsWithProperty("isProducer", true)));
        assertEquals((Integer) 21, Collections.max(graphManager.getAgentIdsWithProperty("isProducer", true)));

        // Testing Direct Graph Interface
        assertEquals(22L, gts.V().count().next().longValue());
    }

    @Test
    public void checkAllGraphVertexEdgePropertyKeysPresent() throws Exception {

        final JanusGraph graph = graphManager.getGraph();
        JanusGraphManagement graphMgmt = graph.openManagement();

        graphManager.deleteGraphItems(true,true);

        // Initialise Graph
        Integer agent1Id = graphManager.addAgent(1, new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},true, false, false,0,0.0);
        Integer agent2Id = graphManager.addAgent(1,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},false,true, false,0,0.0);
        HashMap<Double, Boolean> transactionHistoryExample = new HashMap<>();
        transactionHistoryExample.put(1.0, true);
        transactionHistoryExample.put(2.0, false);
        graphManager.addConnection(agent1Id, agent2Id, 1.0, transactionHistoryExample,1,0);
        graphManager.modifyAgentProperty(agent1Id, "indirectTrustValue", 0.5, false);
        graphManager.modifyAgentProperty(agent1Id, "personalityTrustValue", 0.4, false);

        // Property Keys
        final List<String> definedPropertyKeys = Stream.of("agentId", "isConsumer", "isProducer", "isTrustedAgent", "timeCreated", "globalTrustValue", "transactionHistory", "directTrustValue", "successfulTransactions", "unsuccessfulTransactions", "indirectTrustValue", "personalityTrustValue", "agentAbilityValues", "abilitiesArray").collect(toList());
        final List<String> graphPropertyKeys = StreamSupport.stream(graphMgmt.getRelationTypes(PropertyKey.class).spliterator(), false).map(Namifiable::name).collect(toList());
        assertEquals(definedPropertyKeys.size(), graphPropertyKeys.size());
        assertTrue(graphPropertyKeys.containsAll(definedPropertyKeys));

        // Vertex Labels
        final List<String> definedVertexLabels = Stream.of("agent").collect(Collectors.toList());
        final List<String> graphVertexLabels = StreamSupport.stream(graphMgmt.getVertexLabels().spliterator(), false).map(Namifiable::name).collect(toList());
        assertTrue(graphVertexLabels.containsAll(definedVertexLabels));
        assertEquals(graphVertexLabels.size(), definedVertexLabels.size());


        // Edge Labels
        final EdgeLabel father = graphMgmt.getEdgeLabel("connection");
        assertTrue(father.isDirected());
        assertFalse(father.isUnidirected());
        assertEquals(Multiplicity.SIMPLE, father.multiplicity());


    }


    @Test
    public void checkAgentPropertyTest() throws Exception {

        graphManager.deleteGraphItems(true, true);
        Integer agent1Id = graphManager.addAgent(1, new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},true, false, false,0,0.0);

        double[] abilities = new double[]{1.0,2.0,3.0};
        assertTrue(graphManager.modifyAgentProperty(agent1Id, "isTrustedAgent", false, true));
        assertTrue(graphManager.modifyAgentProperty(agent1Id, "timeCreated", 123.4, true));
        assertTrue(graphManager.modifyAgentProperty(agent1Id, "abilitiesArray", abilities, true));
        assertTrue(graphManager.modifyAgentProperty(agent1Id, "globalTrustValue", 0.8, true));
        assertTrue(graphManager.modifyAgentProperty(agent1Id, "indirectTrustValue", 0.4, true));
        assertTrue(graphManager.modifyAgentProperty(agent1Id, "personalityTrustValue", 0.9, true));
        assertTrue(graphManager.modifyAgentProperty(agent1Id, "isConsumer", true, true));
        assertTrue(graphManager.modifyAgentProperty(agent1Id, "isProducer", false, true));
        assertFalse(graphManager.modifyAgentProperty(agent1Id, "notPresentProperty", 123, true));

        assertEquals(false, graphManager.getAgentProperty(agent1Id, "isTrustedAgent"));
        assertEquals(123.4, graphManager.getAgentProperty(agent1Id, "timeCreated"));
//    assertEquals(abilities, graphManager.getAgentProperty(agent1Id, "abilitiesArray"));
        assertEquals(0.8, graphManager.getAgentProperty(agent1Id, "globalTrustValue"));
        assertEquals(0.4, graphManager.getAgentProperty(agent1Id, "indirectTrustValue"));
        assertEquals(0.9, graphManager.getAgentProperty(agent1Id, "personalityTrustValue"));
        assertEquals(true, graphManager.getAgentProperty(agent1Id, "isConsumer"));
        assertEquals(false, graphManager.getAgentProperty(agent1Id, "isProducer"));
        assertNull(graphManager.getAgentProperty(agent1Id, "nonExistantProperty"));
        assertNull(graphManager.getAgentProperty(12891712, "globalTrustValue"));
    }

    @Test
    public void checkAgentConnectionPropertyTest() throws Exception {

        graphManager.deleteGraphItems(true,true);

        // Initialise Graph
        Integer agent1Id = graphManager.addAgent(1, new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},true, false, false,0,0.0);
        Integer agent2Id = graphManager.addAgent(1,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},false,true, false,0,0.0);
        HashMap<Double, Boolean> transactionHistoryExample = new HashMap<>();
        transactionHistoryExample.put(1.0, true);
        transactionHistoryExample.put(2.0, false);
        graphManager.addConnection(agent1Id, agent2Id, 1.0, transactionHistoryExample,1,0);

        // Agent Properties
        graphManager.modifyConnectionProperty(agent1Id, agent2Id,"directTrustValue", 0.5, false);
        assertEquals(0.5, graphManager.getConnectionProperty(agent1Id,agent2Id, "directTrustValue"));
        graphManager.modifyConnectionProperty(agent1Id, agent2Id, "globalTrustValue", 0.4, false);

        Map<Double, Boolean> transactionMap = new HashMap<>();
        transactionMap.put(12.0,true);
        transactionMap.put(24.0,false);
        graphManager.modifyConnectionProperty(agent1Id, agent2Id, "transactionHistory", transactionMap, false);
        assertEquals(transactionMap, graphManager.getConnectionProperty(agent1Id, agent2Id, "transactionHistory"));

        Map<Double, Boolean> transactionMapTwo = new HashMap<>();
        transactionMap.put(36.0, false);
        transactionMapTwo.put(36.0, false);
        graphManager.modifyConnectionProperty(agent1Id, agent2Id, "transactionHistory", transactionMapTwo, true);
        assertEquals(transactionMap, graphManager.getConnectionProperty(agent1Id, agent2Id, "transactionHistory"));

        // Transaction  Values
        graphManager.modifyConnectionProperty(agent1Id, agent2Id, "successfulTransactions", 123, false);
        graphManager.modifyConnectionProperty(agent1Id, agent2Id, "unsuccessfulTransactions", 456, false);
        assertEquals(124, graphManager.getConnectionProperty(agent1Id, agent2Id, "successfulTransactions"));
        assertEquals(456, graphManager.getConnectionProperty(agent1Id, agent2Id, "unsuccessfulTransactions"));
        assertNull(graphManager.getConnectionProperty(agent1Id, agent2Id, "nonExistantProperty"));
        assertNull(graphManager.getConnectionProperty(1234567, 123456777, "successfulTransactions"));

        // Modify Transaction Values
        assertFalse(graphManager.modifyConnectionProperty(1234567, 123456777, "successfulTransactions", 1, false));


    }

    @SuppressWarnings("unchecked")
    @Test
    public void agentTransactionHistoryTest() throws Exception {

        graphManager.deleteGraphItems(true,true);

        graphManager.addAgents(3, 3,0);
        List<Integer> allConsumerAgentId = graphManager.getAgentIdsWithProperty("isConsumer",true);
        List<Integer> allProducerAgentId =  graphManager.getAgentIdsWithProperty("isProducer",true);

        // Transactions Between Consumer Agent 1 and Producer Agent 2
        int consumerAgentId1 = allConsumerAgentId.get(0);
        int producerAgentId1 = allProducerAgentId.get(0);
        assertNull(graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory"));

        HashMap<Double, Boolean> newTransactions = new HashMap<>();
        newTransactions.put(1.0, true);
        graphManager.modifyConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory", newTransactions, true);
        HashMap<Double, Boolean> transactionHistory = (HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory");
        assertEquals(newTransactions, transactionHistory);

        newTransactions.clear();
        newTransactions.put(2.0, true);
        newTransactions.put(3.0, false);
        newTransactions.put(4.0, true);
        graphManager.modifyConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory", newTransactions, true);

        assertEquals(4, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).values().size());
        assertEquals(true, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).get(1.0));
        assertEquals(true, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).get(2.0));
        assertEquals(false, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).get(3.0));
        assertEquals(true, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).get(4.0));

        // Transactions Between Consumer Agent 2 and Producer Agent 2
        int consumerAgentId2 = allConsumerAgentId.get(1);
        int producerAgentId2 = allProducerAgentId.get(1);
        newTransactions.clear();
        newTransactions.put(1.0,true);
        graphManager.modifyConnectionProperty(consumerAgentId2, producerAgentId2, "transactionHistory", newTransactions, true);
        assertEquals(1, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId2, producerAgentId2, "transactionHistory")).values().size());
        assertEquals(true, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId2, producerAgentId2, "transactionHistory")).get(1.0));

        // Check Transactions Between Consumer Agent 1 and Producer Agent 2; Recheck Transactions between Consumer Agent 1 and Producer Agent 1
        newTransactions.clear();
        newTransactions.put(1.0,false);
        newTransactions.put(2.0,true);
        graphManager.modifyConnectionProperty(consumerAgentId1, producerAgentId2, "transactionHistory", newTransactions, true);
        assertEquals(2, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId2, "transactionHistory")).values().size());
        assertEquals(false, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId2, "transactionHistory")).get(1.0));
        assertEquals(true, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId2, "transactionHistory")).get(2.0));

        assertEquals(4, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).values().size());
        assertEquals(true, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).get(1.0));
        assertEquals(true, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).get(2.0));
        assertEquals(false, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).get(3.0));
        assertEquals(true, ((HashMap<Double, Boolean>) graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory")).get(4.0));



    }

    @Test
    public void agentDirectTrustValueTest() throws Exception {

        graphManager.deleteGraphItems(true,true);

        graphManager.addAgents(3, 3,0);
        List<Integer> allConsumerAgentId = graphManager.getAgentIdsWithProperty("isConsumer", true);
        List<Integer> allProducerAgentId = graphManager.getAgentIdsWithProperty("isProducer", true);

        // Transactions Between Consumer Agent 1 and Producer Agent 2
        int consumerAgentId1 = allConsumerAgentId.get(0);
        int consumerAgentId2 = allConsumerAgentId.get(1);
        int producerAgentId1 = allProducerAgentId.get(0);
        assertNull(graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "directTrustValue"));

        // Direct Trust Value
        graphManager.modifyConnectionProperty(consumerAgentId1, producerAgentId1, "directTrustValue", 0.5, false);
        assertEquals(0.5, graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "directTrustValue"));

        graphManager.modifyConnectionProperty(consumerAgentId1, producerAgentId1, "directTrustValue", 0.25, false);
        assertEquals(0.25, graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "directTrustValue"));

        graphManager.modifyConnectionProperty(consumerAgentId2, producerAgentId1, "directTrustValue", 0.23, false);
        assertEquals(0.23, graphManager.getConnectionProperty(consumerAgentId2, producerAgentId1, "directTrustValue"));

        graphManager.modifyConnectionProperty(producerAgentId1, consumerAgentId1, "directTrustValue", 0.9, false);
        assertEquals(0.9, graphManager.getConnectionProperty(producerAgentId1, consumerAgentId1, "directTrustValue"));

    }


    @Test
    public void agentConnectedToMultipleOtherAgentsConnectionsTest() throws Exception {

        graphManager.deleteGraphItems(true,true);

        graphManager.addAgents(3, 3,0);
        List<Integer> allConsumerAgentId = graphManager.getAgentIdsWithProperty("isConsumer", true);
        List<Integer> allProducerAgentId = graphManager.getAgentIdsWithProperty("isProducer", true);

        int consumerAgentId1 = allConsumerAgentId.get(0);
        int consumerAgentId2 = allConsumerAgentId.get(1);
        int consumerAgentId3 = allProducerAgentId.get(2);
        int producerAgentId1 = allProducerAgentId.get(0);

        // Check no connections before we add a connection
        assertNull(graphManager.getConnectionProperty(consumerAgentId1, producerAgentId1, "transactionHistory"));

        // Connections between different types of agents
        graphManager.addConnection(consumerAgentId1, consumerAgentId2,null,null,0,0);
        assertTrue(graphManager.areAgentsConnected(consumerAgentId1,consumerAgentId2));

        graphManager.addConnection(consumerAgentId1, producerAgentId1,null,null,0,0);
        assertTrue(graphManager.areAgentsConnected(consumerAgentId1,producerAgentId1));

        // Connections created by transactions being added
        HashMap<Double, Boolean> initialTransactions = new HashMap<>();
        initialTransactions.put(1.0,false);
        initialTransactions.put(2.0,true);
        graphManager.modifyConnectionProperty(consumerAgentId1, consumerAgentId3, "transactionHistory",initialTransactions,true);
        assertTrue(graphManager.areAgentsConnected(consumerAgentId1,consumerAgentId3));

    }

    @Test
    public void checkAgentPropertyHashMap() throws Exception {

        graphManager.deleteGraphItems(true,true);
        assertNull(graphManager.getAgentPropertyOfAllAgents("directTrustValue"));

        graphManager.addAgent(1,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()}
            ,true,true,false, 0,0.0);
        assertNotNull(graphManager.getAgentPropertyOfAllAgents("directTrustValue"));
        assertEquals(1, graphManager.getAgentPropertyOfAllAgents("directTrustValue").size());

        graphManager.addAgent(0.3,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()}
            ,true,true,false, 0,0.0);
        assertNotNull(graphManager.getAgentPropertyOfAllAgents("directTrustValue"));
        assertEquals(2, graphManager.getAgentPropertyOfAllAgents("directTrustValue").size());

        assertNull(graphManager.getAgentPropertyOfAllAgents("nonExistantProperty"));
    }

    @Test
    public void indirectRecommendationCalculationAndAutoUpdateTest() throws Exception {

        graphManager.deleteGraphItems(true,true);

        graphManager.addAgents(3, 3,0);
        List<Integer> allConsumerAgentId = graphManager.getAgentIdsWithProperty("isConsumer", true);
        List<Integer> allProducerAgentId = graphManager.getAgentIdsWithProperty("isProducer", true);

        int consumerAgentId1 = allConsumerAgentId.get(0);
        int consumerAgentId2 = allConsumerAgentId.get(1);
        int consumerAgentId3 = allProducerAgentId.get(2);
        int producerAgentId1 = allProducerAgentId.get(0);
    }

    @Test
    public void checkIncomingOutgoingAgentConnections() throws Exception {

        graphManager.deleteGraphItems(true,true);

        Integer agent1Id = graphManager.addAgent(1,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()}
            ,true,true,false, 0,0.0);

        // Outgoing Connections
        Integer agent2Id = graphManager.addAgent(0.22,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},true,true,false,0,0.0);
        graphManager.addConnection(agent1Id, agent2Id, 0.222, null,0,0);
        Integer agent3Id = graphManager.addAgent(0.33,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},true,true,false,0,0.0);
        graphManager.addConnection(agent1Id, agent3Id, 0.333, null,0,0);
        Integer agent4Id = graphManager.addAgent(0.44,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},true,true,false,0,0.0);
        graphManager.addConnection(agent1Id, agent4Id, 0.444, null,0,0);

        List<Integer> actualIncomingConnections = graphManager.getAgentOutgoingConnectionsId(agent1Id);
        List<Integer> expectedIncomingConnections = new ArrayList<>(Arrays.asList(agent2Id, agent3Id, agent4Id));
        System.out.println(actualIncomingConnections + " -- " + expectedIncomingConnections);
        assertEquals(actualIncomingConnections.size(), expectedIncomingConnections.size());
        assertTrue(actualIncomingConnections.containsAll(expectedIncomingConnections));

        assertNotNull(graphManager.getAgentOutgoingConnectionsId(agent1Id));
        assertNull(graphManager.getAgentOutgoingConnectionsId(1279371279));

        // Incoming Connections
        Integer agent5Id = graphManager.addAgent(0.55,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},true,true,false,0,0.0);
        graphManager.addConnection(agent5Id, agent1Id, 0.555, null,0,0);
        Integer agent6Id = graphManager.addAgent(0.66,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},true,true,false,0,0.0);
        graphManager.addConnection(agent6Id, agent1Id, 0.666, null,0,0);
        Integer agent7Id = graphManager.addAgent(0.77,new double[]{Math.random(), Math.random(), Math.random(), Math.random(), Math.random()},true,true,false,0,0.0);
        graphManager.addConnection(agent7Id, agent1Id, 0.777, null,0,0);

        List<Integer> actualOutgoingConnections = graphManager.getAgentIncomingConnectionsIds(agent1Id);
        List<Integer> expectedOutgoingConnections = new ArrayList<>(Arrays.asList(agent5Id, agent6Id, agent7Id));
        System.out.println(actualOutgoingConnections + " -- " + expectedOutgoingConnections);
        assertEquals(actualOutgoingConnections.size(), expectedOutgoingConnections.size());
        assertTrue(actualOutgoingConnections.containsAll(expectedOutgoingConnections));

        assertNotNull(graphManager.getAgentIncomingConnectionsIds(agent1Id));
        assertNull(graphManager.getAgentIncomingConnectionsIds(1279371279));

    }


}
