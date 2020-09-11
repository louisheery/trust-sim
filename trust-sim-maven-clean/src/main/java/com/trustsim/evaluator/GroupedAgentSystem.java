package com.trustsim.evaluator;

import com.trustsim.simulator.agent.AgentType;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import com.trustsim.synthesiser.AgentSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A data storage object used to store 1 or more Agent System objects, which can be concurrently
 * displayed on a graph or table pane.
 */
public class GroupedAgentSystem {

  private final String name;
  private final Map<String, AgentSystem> agentSystems = new HashMap<>();

  /**
   * Constructor which initialises object with a single agent system object, and the corresponding
   * name of the trust model used to obtain the simulation results stored within that agent system
   * object.
   *
   * @param agentSystem stores simulation results, parameters and agents
   * @param trustModelName name of the trust model used in the simulation
   */
  public GroupedAgentSystem(AgentSystem agentSystem, String trustModelName) {
    this.agentSystems.put(trustModelName, agentSystem);
    this.name = agentSystems.get(trustModelName).getSystemName();
  }

  /**
   * Function used to add an additional agent system to the object.
   *
   * @param agentSystem stores simulation results, parameters and agents
   * @param trustModelName name of the trust model used in the simulation
   */
  public void addAgentSystem(AgentSystem agentSystem, String trustModelName) {
    if (!agentSystems.containsKey(trustModelName)) {
      agentSystems.put(trustModelName, agentSystem);
    }
  }

  /**
   * Function used to obtain the map data structure which stores all agent system objects as values,
   * and their corresponding trust model name as the keys.
   *
   * @return map object of trust model names and agent system simulation result objects
   */
  public Map<String, AgentSystem> getAgentSystems() {
    return agentSystems;
  }

  /**
   * Function used to obtain an agent system object given the name of the trust model used to
   * generate it.
   *
   * @return agent system simulation result object
   */
  public AgentSystem getAgentSystem(String trustModelName) {
    return agentSystems.get(trustModelName);
  }

  /**
   * Function to get name of grouped object system.
   *
   * @return name of object
   */
  public String getSystemName() {
    return this.name;
  }

  /**
   * Function to return number of trusted agents used in the agent system simulation.
   *
   * @return number of trusted agents
   */
  public int getNumberOfTrustedAgents() {
    return this.agentSystems
        .get(agentSystems.keySet().iterator().next())
        .getNumberOfTrustedAgents();
  }

  /**
   * Function to return number of very good personality agents used in the agent system simulation.
   *
   * @return number of very good agents
   */
  public int getNumberOfVGoodAgents() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfVGoodAgents();
  }

  /**
   * Function to return number of good personality agents used in the agent system simulation.
   *
   * @return number of good agents
   */
  public int getNumberOfGoodAgents() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfGoodAgents();
  }

  /**
   * Function to return number of ok personality agents used in the agent system simulation.
   *
   * @return number of ok agents
   */
  public int getNumberOfOkAgents() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfOkAgents();
  }

  /**
   * Function to return number of bad personality agents used in the agent system simulation.
   *
   * @return number of bad agents
   */
  public int getNumberOfBadAgents() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfBadAgents();
  }

  /**
   * Function to return number of very bad personality agents used in the agent system simulation.
   *
   * @return number of very bad agents
   */
  public int getNumberOfVBadAgents() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfVBadAgents();
  }

  /**
   * Function to return number of malicious agents used in the agent system simulation.
   *
   * @return number of malicious agents
   */
  public int getNumberOfMaliciousAgents() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfMaliciousAgents();
  }

  /**
   * Function to return number of elastic (changing personality) agents used in the agent system
   * simulation.
   *
   * @return number of elastic agents
   */
  public int getNumberOfElasticAgents() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfElasticAgents();
  }

  /**
   * Function to return initial personality of the elastic (changing personality) agents used in the
   * agent system simulation.
   *
   * @return start personality agentType enum
   */
  public AgentType getElasticAgentStartPersonality() {
    return agentSystems
        .get(agentSystems.keySet().iterator().next())
        .getElasticAgentStartPersonality();
  }

  /**
   * Function to return final personality of the elastic (changing personality) agents used in the
   * agent system simulation.
   *
   * @return end personality agentType enum
   */
  public AgentType getElasticAgentEndPersonality() {
    return agentSystems
        .get(agentSystems.keySet().iterator().next())
        .getElasticAgentEndPersonality();
  }

  /**
   * Function to return alpha constant, a parameter used within the Eigen Trust Model that
   * determines the weighting of the personality of trusted agents in the calculation of global
   * trust values of other agents in the agent system simulation. Only returned if there is an Agent
   * System present in this object which was run using a simulation that used the EigenTrust Model
   * to calculate trust values of the simulation agents.
   *
   * @return alpha constant
   */
  public double getAlphaConstantValue() {
    if (agentSystems.containsKey("EigenTrustModel")) {
      return agentSystems.get("EigenTrustModel").getAlphaConstantValue();
    }
    return 0.0;
  }

  /**
   * Function to return epsilon constant, a parameter used within the Eigen Trust Model that
   * determines the convergence limit of the global trust values. Once the change in global trust
   * values between iterative updates of its value falls below this value, the global trust values
   * will stop being iteratively calculated.
   *
   * @return epsilon constant
   */
  public double getEpsilonConstantValue() {
    if (agentSystems.containsKey("EigenTrustModel")) {
      return agentSystems.get("EigenTrustModel").getEpsilonConstantValue();
    }
    return 0.0;
  }

  /**
   * Function to return number of service requests (transaction events) used in the agent system
   * simulation.
   *
   * @return number of service requests
   */
  public int getNumberOfServiceRequests() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfServiceRequests();
  }

  /**
   * Function to return number of types of different service requests used in the agent system
   * simulation.
   *
   * @return number of service request types
   */
  public int getNumberOfServiceRequestTypes() {
    return agentSystems
        .get(agentSystems.keySet().iterator().next())
        .getNumberOfServiceRequestTypes();
  }

  /**
   * Function to return number of simulation replications performed for each of the agent systems
   * stored within this object.
   *
   * @return number of simulation replications performed
   */
  public int getNumberOfSimulations() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfSimulations();
  }

  /**
   * Function to the frequency at which the local and global trust values are recalculated during a
   * simulation. Value specifies the number of service requests between each recalculation.
   *
   * @return trust update interval size
   */
  public int getTrustUpdateInterval() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getTrustUpdateInterval();
  }

  /**
   * Function returns the index of agents of a particular type in a simulation's results dataset.
   * Where this index is equal to the array index for each vector and matrix that stores local and
   * global trust values relating to that agent.
   *
   * @param agentType type of agent which agent indices should be returned for.
   * @return list of indices of agents which have type equal to that of agentType
   */
  public List<Integer> getAgentIndexOfType(AgentTypeWrapper agentType) {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getAgentIndexOfType(agentType);
  }

  /**
   * Function returns all distinct agent types found in a simulation results dataset. Where each
   * agent type denotes a distinct agent personality, that is the personality of the agent during
   * the simulation.
   *
   * @return list of distinct agentTypeWrapper objects
   */
  public List<AgentTypeWrapper> getAgentTypes() {
    return agentSystems.get(agentSystems.keySet().iterator().next()).getAgentTypes();
  }

  /**
   * Function returns map containing a set of service request histories for agent system
   * simulations. Keys of map corresponds to the trust model used to obtain these simulation
   * results, values of map correspond to a list of linked lists objects (with each linked list
   * consisting of a series of service requests which took place in a particular simulation). Each
   * list of linked list contains a single linked list object for each simulation replication.
   *
   * @return map of service request histories and the trust model used to obtain them
   */
  public Map<String, List<LinkedList<ServiceRequest>>> getServiceRequestHistoryAll() {
    Map<String, List<LinkedList<ServiceRequest>>> output = new HashMap<>();

    for (Map.Entry<String, AgentSystem> agentSystem : agentSystems.entrySet()) {
      output.put(agentSystem.getKey(), agentSystem.getValue().getServiceRequestHistoryAll());
    }
    return output;
  }

  /**
   * Function returns map containing a set of arrays of agent personality trust values for agent
   * system simulations. Keys of map corresponds to the trust model used to obtain these simulation
   * results, values of map correspond to a list of double[] objects (with each double[] consisting
   * of a series of agent personality trust values during a particular simulation). Each list of
   * double[] contains a single double[] object for each simulation replication.
   *
   * @return map of agent personality trust values and the trust model used to obtain them
   */
  public Map<String, List<double[]>> getAgentPersonalityTrustValuesAll() {
    Map<String, List<double[]>> output = new HashMap<>();

    for (Map.Entry<String, AgentSystem> agentSystem : agentSystems.entrySet()) {
      output.put(agentSystem.getKey(), agentSystem.getValue().getAgentPersonalityTrustValuesAll());
    }
    return output;
  }

  /**
   * Function returns map containing a set of arrays of agent global trust values for agent system
   * simulations at the end of the simulation. Keys of map corresponds to the trust model used to
   * obtain these simulation results, values of map correspond to a list of double[] objects (with
   * each double[] consisting of a series of agent global trust values at the end of a particular
   * simulation). Each list of double[] contains a single double[] object for each simulation
   * replication.
   *
   * @return map of final agent global trust values and the trust model used to obtain them
   */
  public Map<String, List<double[]>> getGlobalTrustVectorAll() {
    Map<String, List<double[]>> output = new HashMap<>();
    for (Map.Entry<String, AgentSystem> agentSystem : agentSystems.entrySet()) {
      output.put(agentSystem.getKey(), agentSystem.getValue().getGlobalTrustVectorAll());
    }
    return output;
  }

  /**
   * Function returns map containing a set of arrays of agent global trust values for agent system
   * simulations at each global trust value update interval in the simulation. Keys of map
   * corresponds to the trust model used to obtain these simulation results, values of map
   * correspond to a list of List< double[] > objects (with each List< double[] > consisting of a
   * time series of how the agent global trust values evolve during a particular simulation. Each
   * list of List< double[] > contains a single List< double[] > object for each simulation
   * replication.
   *
   * @return map of list of changing agent global trust values and the trust model used to obtain
   *     them
   */
  public Map<String, List<List<double[]>>> getGlobalTrustVectorTimeSeriesAll() {
    Map<String, List<List<double[]>>> output = new HashMap<>();

    for (Map.Entry<String, AgentSystem> agentSystem : agentSystems.entrySet()) {
      output.put(agentSystem.getKey(), agentSystem.getValue().getGlobalTrustVectorTimeSeriesAll());
    }
    return output;
  }

  /**
   * Function returns map containing a set of matrices of agent local trust values for agent system
   * simulations at the end of the simulation. Keys of map corresponds to the trust model used to
   * obtain these simulation results, values of map correspond to a list of double[][] objects (with
   * each double[][] consisting of a series of agent-agent local trust values at the end of a
   * particular simulation). Each list of double[][] contains a single double[][] object for each
   * simulation replication.
   *
   * @return map of final agent local trust values and the trust model used to obtain them
   */
  public Map<String, List<double[][]>> getLocalTrustConnectionMatrixAll() {
    Map<String, List<double[][]>> output = new HashMap<>();
    for (Map.Entry<String, AgentSystem> agentSystem : agentSystems.entrySet()) {
      output.put(agentSystem.getKey(), agentSystem.getValue().getLocalTrustConnectionMatrixAll());
    }
    return output;
  }

  /**
   * Function returns a list of all discrete trust model names used in the simulations' who's
   * results are contained within this object. Corresponds to the keySet of the map containing each
   * agent system object within this object.
   *
   * @return list of trust model names
   */
  public List<String> getTrustModelNames() {
    List<String> output = new ArrayList<>();
    for (Map.Entry<String, AgentSystem> agentSystem : agentSystems.entrySet()) {
      output.add(agentSystem.getKey());
    }
    return output;
  }

  /**
   * Function overrides default toString function.
   *
   * @return string representation of object
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Function returns number of agents in Agent Systems in GroupedAgentSystem.
   *
   * @return number of agents in agent system
   */
  public int getNumberOfAgents() {
    return this.agentSystems.get(agentSystems.keySet().iterator().next()).getNumberOfAgents();
  }
}
