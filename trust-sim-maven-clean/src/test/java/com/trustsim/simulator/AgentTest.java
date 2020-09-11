package com.trustsim.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.trustsim.evaluator.AgentTypeWrapper;
import com.trustsim.simulator.agent.Agent;
import com.trustsim.simulator.agent.AgentType;
import com.trustsim.simulator.agent.MaliciousAgent;
import org.junit.jupiter.api.Test;

public class AgentTest {

  @Test
  public void eigenAgentBehaviourTest() {
    Agent trustedAgent = new Agent(1, new AgentTypeWrapper(AgentType.VGOOD, 0));
    Agent goodAgent = new Agent(2, new AgentTypeWrapper(AgentType.GOOD, 0));
    Agent badAgent = new Agent(3, new AgentTypeWrapper(AgentType.BAD, 0));
    Agent maliciousAgent = new MaliciousAgent(4);

    assertEquals(0.9, trustedAgent.getAgentPersonalityTrustValue(0));
    assertEquals(0.75, goodAgent.getAgentPersonalityTrustValue(0));
    assertEquals(0.25, badAgent.getAgentPersonalityTrustValue(0));
    assertEquals(0.0, maliciousAgent.getAgentPersonalityTrustValue(0));
  }
}
