package com.trustsim.simulator.agent;

import com.trustsim.evaluator.AgentTypeWrapper;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Derived Class of Agent, which represents an Agent who if exists along with other MaliciousAgent
 * objects, these agents will collude during a simulation, by always successfully carrying out
 * transactions with other malicious agents, while rarely successfully carrying out transactions
 * with non-malicious agents.
 */
public class MaliciousAgent extends Agent {

  List<Integer> otherMaliciousAgents = new ArrayList<>();

  /**
   * Constructor of Malicious Agent object.
   *
   * @param id unique identifier of agent
   */
  public MaliciousAgent(int id) {
    super(id, new AgentTypeWrapper(AgentType.MALICIOUS, 0));
  }

  public void setOtherMaliciousAgents(List<Integer> agents) {
    this.otherMaliciousAgents = agents;
  }

  /**
   * Function used when this agent object is acting as a producer in a transaction, that determines
   * whether or not this agent will successfully complete the transaction described in the service
   * request object.
   *
   * @param newRequest the service request object
   * @param simulationTime the time in the simulation that the transaction is required to be carried
   *     out.
   * @return whether or not the transaction is completed
   */
  @Override
  public boolean executeTransaction(ServiceRequest newRequest, int simulationTime) {

    if (otherMaliciousAgents.contains(newRequest.getConsumer())) {
      return true;
    }

    Random r = new Random();
    return Math.random()
        < ((r.nextGaussian() * AgentType.MALICIOUS.stDev) + AgentType.MALICIOUS.mean);
  }
}
