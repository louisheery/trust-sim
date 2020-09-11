package com.trustsim.simulator;

import static com.trustsim.simulator.trustmodel.TrustModelEnum.DynamicTrustModel;
import static com.trustsim.simulator.trustmodel.TrustModelEnum.EigenTrustModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.trustsim.evaluator.AgentTypeWrapper;
import com.trustsim.simulator.agent.Agent;
import com.trustsim.simulator.agent.AgentType;
import com.trustsim.synthesiser.AgentSystem;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

public class AgentSystemTest {

  @Test
  public void agentSystemTest() {

      // Check agent system trust model
    AgentSystem agentSystem =
        new AgentSystem("TEST_SYSTEM", 1, 2, 3, 4, 5, 6, 7, 8, AgentType.VGOOD, AgentType.BAD);
    assertEquals("TEST_SYSTEM", agentSystem.getSystemName());
    assertEquals("TEST_SYSTEM", agentSystem.toString());
    agentSystem.setTrustModel(EigenTrustModel);
    assertEquals(EigenTrustModel, agentSystem.getTrustModel());
    agentSystem.setTrustModel(DynamicTrustModel);
    assertEquals(DynamicTrustModel.toString(), agentSystem.getTrustModel().toString());
    agentSystem.setDecayConstantValue(0.0025);

    // Check agent system member variables
    assertEquals(0.001, agentSystem.getEpsilonConstantValue());
    assertEquals(0.5, agentSystem.getAlphaConstantValue());
    assertEquals(0.0025, agentSystem.getDecayConstantValue());
    assertEquals(1000, agentSystem.getNumberOfServiceRequests());
    assertEquals(1, agentSystem.getNumberOfTrustedAgents());
    assertEquals(2, agentSystem.getNumberOfVGoodAgents());
    assertEquals(3, agentSystem.getNumberOfGoodAgents());
    assertEquals(4, agentSystem.getNumberOfOkAgents());
    assertEquals(5, agentSystem.getNumberOfBadAgents());
    assertEquals(6, agentSystem.getNumberOfVBadAgents());
    assertEquals(7, agentSystem.getNumberOfMaliciousAgents());
    assertEquals(8, agentSystem.getNumberOfElasticAgents());
    assertEquals(AgentType.VGOOD, agentSystem.getElasticAgentStartPersonality());
    assertEquals(AgentType.BAD, agentSystem.getElasticAgentEndPersonality());

    // Agent HashMap Setup
    HashMap<Integer, Agent> agentMap = new HashMap<>();
    agentMap.put(0, new Agent(0, new AgentTypeWrapper(AgentType.VGOOD, 0)));
    agentMap.put(1, new Agent(0, new AgentTypeWrapper(AgentType.ISTRUSTED, 0)));
    agentMap.put(2, new Agent(0, new AgentTypeWrapper(AgentType.BAD, 0)));
    agentMap.put(3, new Agent(0, new AgentTypeWrapper(AgentType.VBAD, 0)));
    agentSystem.setAgentHashMap(0, agentMap);

    agentSystem.setNumberOfSimulations(1);
    assertEquals(1, agentSystem.getNumberOfSimulations());

    assertEquals(10, agentSystem.getTrustUpdateInterval());
    assertEquals(DynamicTrustModel.toString(), agentSystem.getTrustModelName());

    // Check JSON Generated
    agentSystem.setDiscreteServiceRequestHistory(0, new LinkedList<>());
    agentSystem.setLocalTrustConnectionMatrix(0, new double[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    agentSystem.setGlobalTrustVector(0, new double[] {1, 2, 3});
    agentSystem.setSuccessfulTransactionVector(0, new double[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    agentSystem.setUnsuccessfulTransactionVector(
        0, new double[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    agentSystem.setAgentPreTrustedVector(0, new double[] {1, 2, 3});

    System.out.println(agentSystem.toJsonString(0));
    assertEquals(
        "{\"Number of Bad Agents\":5,\"Agent System Name\":\"TEST_SYSTEM\",\"Unsuccessful Transaction Vector\":[[1,2,3],[4,5,6],[7,8,9]],\"Number of Malicious Agents\":7,\"Number of Trusted Agents\":1,\"Trust Model\":\"DynamicTrustModel\",\"Number of Service Requests\":1000,\"Number of Very Good Agents\":2,\"Number of Ok Agents\":4,\"Local Trust Connection Matrix\":[[1,2,3],[4,5,6],[7,8,9]],\"Successful Transaction Vector\":[[1,2,3],[4,5,6],[7,8,9]],\"Trust Update Interval\":10,\"Global Trust Vector\":[1,2,3],\"Service Request History\":[],\"Number of Good Agents\":3,\"Agent Personality Trust Vector\":[0.9,1,0.25,0],\"Number of Agents\":36,\"Number of Very Bad Agents\":6,\"Agent Pre Trusted Vector\":[1,2,3]}",
        agentSystem.toJsonString(0));

    // Check manipulation functions
    agentSystem.resetAgentSystem(false, false, false);
  }
}
