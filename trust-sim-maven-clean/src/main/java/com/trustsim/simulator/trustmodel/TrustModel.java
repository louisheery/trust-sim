package com.trustsim.simulator.trustmodel;

import com.trustsim.simulator.agent.Agent;
import com.trustsim.simulator.agent.AgentType;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import com.trustsim.synthesiser.AgentSystem;
import java.util.HashMap;

/**
 * Trust Model interface which can be implemented by a trust model that is used to calculate local
 * and global trust values of agents in a Trust System Simulation.
 */
public abstract class TrustModel {

  protected String trustModelName = "AbstractTrustModel";

  protected double[] globalTrustVector;
  protected double[][] successfulTransactionVector;
  protected double[][] unsuccessfulTransactionVector;

  public String getName() {
    return trustModelName;
  }

  /**
   * Function overrides default toString function.
   *
   * @return string representation of object
   */
  @Override
  public String toString() {
    return trustModelName;
  }

  /**
   * Function initialises Trust Model with properties of the Agent System which Trust Model is
   * performing calculations for.
   *
   * @param veryGoodAgents number of very good personality agents
   * @param goodAgents number of good personality agents
   * @param okAgents number of ok personality agents
   * @param badAgents number of bad personality agents
   * @param veryBadAgents number of very bad personality agents
   * @param trustedAgents number of trusted agents
   * @param maliciousAgents number of malicious agents
   * @param elasticAgents number of changing personality agents
   * @param realUserAgents number of real world user agents
   * @param elasticAgentStartPersonality starting personality of changing personality agents
   * @param elasticAgentEndPersonality ending personality of changing personality agents
   * @param numberOfEvents number of trust events in simulation
   * @param numberOfServiceTypes number of types of service requests in simulation
   * @return HashMap of Agent, Key = Agent Number, Value = Agent Object
   */
  public abstract HashMap<Integer, Agent> initialiseTrustModel(
      int veryGoodAgents,
      int goodAgents,
      int okAgents,
      int badAgents,
      int veryBadAgents,
      int trustedAgents,
      int maliciousAgents,
      int elasticAgents,
      int realUserAgents,
      AgentType elasticAgentStartPersonality,
      AgentType elasticAgentEndPersonality,
      int numberOfEvents,
      int numberOfServiceTypes);

  /**
   * Function updates a matrix array that stores the transaction history of successful and
   * unsuccessful transactions between agents in a simulation, based on the outcome of a new service
   * request object.
   *
   * @param request service request object
   * @param type service request type
   * @param subTransactionComplete whether child transaction of a continuous service request object
   *     is completed or not. Only applicable if the service request object is of type
   *     ContinuousServiceRequest
   */
  public abstract void updateTransactionHistoryMatrix(
      ServiceRequest request, String type, boolean subTransactionComplete);

  /**
   * Function updates a local trust values matrix that stores each agents local trust value in each
   * other agent in a simulation, based on the outcome of a new service request object.
   *
   * @param request service request object
   */
  public abstract void updateLocalTrustValues(ServiceRequest request);

  /**
   * Function which recalculates the global trust values vector using the values stored within the
   * local trust values matrix for a simulation.
   */
  public abstract double[] updateGlobalTrustValues();

  /**
   * Function returns convergence limit of Trust Model.
   *
   * @return convergence limit of trust model.
   */
  public abstract double getConvergenceLimit();

  /**
   * Function used to save Trust Model calculation data to an AgentSystem object.
   *
   * @param agentSystem agent system where calculation data should be saved to.
   * @param simulationNumber simulation number of the results being saved.
   */
  public abstract void saveToAgentSystem(AgentSystem agentSystem, int simulationNumber);
}
