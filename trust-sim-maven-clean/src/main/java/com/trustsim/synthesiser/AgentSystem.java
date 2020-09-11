package com.trustsim.synthesiser;

import com.trustsim.evaluator.AgentTypeWrapper;
import com.trustsim.simulator.agent.Agent;
import com.trustsim.simulator.agent.AgentType;
import com.trustsim.simulator.simulationmanager.ContinuousServiceRequest;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import com.trustsim.simulator.trustmodel.TrustModelEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

// import org.json.simple.JSONArray;
// import org.json.simple.JSONObject;

/**
 * Class that describes the attributes of, the agents within, and the history of a trust system,
 * that can be used to run a trust model simulation from, and then store the results of that
 * simulation for evaluation.
 */
public class AgentSystem implements Serializable {

  protected TrustModelEnum trustModel;
  protected String systemName;

  // Simulation Data
  private final List<HashMap<Integer, Agent>> agentHashMap = new ArrayList<>();
  private List<LinkedList<ServiceRequest>> serviceRequestHistory = new ArrayList<>();
  private final List<LinkedList<ContinuousServiceRequest>> continuousServiceRequestHistory =
      new ArrayList<>();

  // Shared Member Variables
  private final List<double[]> globalTrustVector = new ArrayList<>();
  private final List<List<double[]>> globalTrustVectorTimeSeries = new ArrayList<>();
  private final List<double[][]> localTrustConnectionMatrix = new ArrayList<>();
  private final List<double[][]> successfulTransactionVector = new ArrayList<>();
  private final List<double[][]> unsuccessfulTransactionVector = new ArrayList<>();
  private final List<double[]> agentPreTrustedVector = new ArrayList<>();

  private List<List<Map<Integer, Integer>>> successfulTransaction3DVector = new ArrayList<>();
  private List<List<Map<Integer, Integer>>> unsuccessfulTransaction3DVector = new ArrayList<>();

  // System Parameter Variables (default values)
  // EigenTrust
  public double alphaConst = 0.5;
  public double epsilonConst = 0.001;

  // DynamicTrust
  public double decayConst = 0.0025;

  // Simulation Parameter Variables (default values)
  private int numberOfSimulations = 1;
  private int numberOfAgents = 8;
  private int numberOfTrustedAgents = 3;
  private int numberOfVGoodAgents = 3;
  private int numberOfGoodAgents = 0;
  private int numberOfOkAgents = 0;
  private int numberOfBadAgents = 2;
  private int numberOfVBadAgents = 0;
  private int numberOfMaliciousAgents = 0;
  private int numberOfElasticAgents = 0;
  private AgentType elasticAgentStartPersonality = AgentType.VGOOD;
  private AgentType elasticAgentEndPersonality = AgentType.VGOOD;

  protected int numberOfServiceRequests = 1000;
  protected int trustUpdateInterval = 10;
  protected int numberOfServiceRequestTypes = 1;

  public AgentSystem(String systemName) {
    this.systemName = systemName;
  }

  /**
   * Constructor which initialises Agent System based on the number of each agent type which system
   * should contain.
   *
   * @param systemName agent system name
   * @param numberOfTrustedAgents number of trusted agents of system
   * @param numberOfVGoodAgents number of very good agents of system
   * @param numberOfGoodAgents number of good agents of system
   * @param numberOfOkAgents number of ok agents of system
   * @param numberOfBadAgents number of bad agents of system
   * @param numberOfVBadAgents number of very bad agents of system
   * @param numberOfMaliciousAgents number of malicious agents of system
   * @param numberOfElasticAgents number of elastic (changing personality) agents of system
   * @param elasticAgentStartPersonality start personality of elastic agents of system
   * @param elasticAgentEndPersonality end personality of elastic agents of system
   */
  public AgentSystem(
      String systemName,
      int numberOfTrustedAgents,
      int numberOfVGoodAgents,
      int numberOfGoodAgents,
      int numberOfOkAgents,
      int numberOfBadAgents,
      int numberOfVBadAgents,
      int numberOfMaliciousAgents,
      int numberOfElasticAgents,
      AgentType elasticAgentStartPersonality,
      AgentType elasticAgentEndPersonality) {

    this.systemName = systemName;
    this.numberOfTrustedAgents = numberOfTrustedAgents;
    this.numberOfVGoodAgents = numberOfVGoodAgents;
    this.numberOfGoodAgents = numberOfGoodAgents;
    this.numberOfOkAgents = numberOfOkAgents;
    this.numberOfBadAgents = numberOfBadAgents;
    this.numberOfVBadAgents = numberOfVBadAgents;
    this.numberOfMaliciousAgents = numberOfMaliciousAgents;
    this.numberOfAgents =
        numberOfTrustedAgents
            + numberOfVGoodAgents
            + numberOfGoodAgents
            + numberOfOkAgents
            + numberOfBadAgents
            + numberOfVBadAgents
            + numberOfMaliciousAgents
            + numberOfElasticAgents;
    this.numberOfElasticAgents = numberOfElasticAgents;
    this.elasticAgentStartPersonality = elasticAgentStartPersonality;
    this.elasticAgentEndPersonality = elasticAgentEndPersonality;
  }

  public String getSystemName() {
    return systemName;
  }

  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

  /**
   * Function overrides default toString function.
   *
   * @return string representation of object
   */
  @Override
  public String toString() {
    return systemName;
  }

  public TrustModelEnum getTrustModel() {
    return trustModel;
  }

  public void setTrustModel(TrustModelEnum trustModel) {
    this.trustModel = trustModel;
  }

  public void setNumberOfServiceRequests(int numberOfServiceRequests) {
    this.numberOfServiceRequests = numberOfServiceRequests;
  }

  public void setNumberOfServiceRequestTypes(int numberOfServiceRequestTypes) {
    this.numberOfServiceRequestTypes = numberOfServiceRequestTypes;
  }

  public void setTrustUpdateInterval(int trustUpdateInterval) {
    this.trustUpdateInterval = trustUpdateInterval;
  }

  // EigenTrust Model
  public double getEpsilonConstantValue() {
    return epsilonConst;
  }

  public double getAlphaConstantValue() {
    return alphaConst;
  }

  public double getDecayConstantValue() {
    return decayConst;
  }

  public void setEpsilonConstantValue(double epsilonConst) {
    this.epsilonConst = epsilonConst;
  }

  public void setAlphaConstantValue(double alphaConst) {
    this.alphaConst = alphaConst;
  }

  public void setDecayConstantValue(double decayConst) {
    this.decayConst = decayConst;
  }

  // System Variables
  public int getNumberOfAgents() {
    return numberOfAgents;
  }

  public int getNumberOfTrustedAgents() {
    return numberOfTrustedAgents;
  }

  public int getNumberOfVGoodAgents() {
    return numberOfVGoodAgents;
  }

  public int getNumberOfGoodAgents() {
    return numberOfGoodAgents;
  }

  public int getNumberOfOkAgents() {
    return numberOfOkAgents;
  }

  public int getNumberOfBadAgents() {
    return numberOfBadAgents;
  }

  public int getNumberOfVBadAgents() {
    return numberOfVBadAgents;
  }

  public int getNumberOfMaliciousAgents() {
    return numberOfMaliciousAgents;
  }

  public int getNumberOfElasticAgents() {
    return numberOfElasticAgents;
  }

  public AgentType getElasticAgentStartPersonality() {
    return elasticAgentStartPersonality;
  }

  public AgentType getElasticAgentEndPersonality() {
    return elasticAgentEndPersonality;
  }

  public int getNumberOfServiceRequests() {
    return numberOfServiceRequests;
  }

  public int getNumberOfServiceRequestTypes() {
    return numberOfServiceRequestTypes;
  }

  public void setNumberOfSimulations(int numberOfSimulations) {
    this.numberOfSimulations = numberOfSimulations;
  }

  public int getNumberOfSimulations() {
    return numberOfSimulations;
  }

  public int getTrustUpdateInterval() {
    return trustUpdateInterval > 0 ? trustUpdateInterval : 1;
  }

  /**
   * Function returns a set of distinct AgentTypeWrappers which are contained within this agent
   * system object.
   *
   * @return set of agent type wrapper objects
   */
  public List<AgentTypeWrapper> getAgentTypes() {
    Set<AgentTypeWrapper> output = new HashSet<>();

    for (Map.Entry<Integer, Agent> agent : agentHashMap.get(0).entrySet()) {
      output.add(agent.getValue().getAgentTypes());
    }
    return new ArrayList<>(output);
  }

  public List<double[][]> getLocalTrustConnectionMatrixAll() {
    return localTrustConnectionMatrix;
  }

  public void setLocalTrustConnectionMatrix(
      int simulationNumber, double[][] localTrustConnectionMatrix) {
    this.localTrustConnectionMatrix.add(localTrustConnectionMatrix);
  }

  public List<double[]> getGlobalTrustVectorAll() {
    return globalTrustVector;
  }

  public void setGlobalTrustVector(int simulationNumber, double[] globalTrustVector) {
    this.globalTrustVector.add(globalTrustVector);
  }

  public List<List<double[]>> getGlobalTrustVectorTimeSeriesAll() {
    return globalTrustVectorTimeSeries;
  }

  public void clearGlobalTrustVectorTimeSeries() {
    this.globalTrustVectorTimeSeries.clear();
  }

  public void setGlobalTrustVectorTimeSeries(
      int simulationNumber, List<double[]> globalTrustVectorTimeSeries) {
    this.globalTrustVectorTimeSeries.add(globalTrustVectorTimeSeries);
  }

  /**
   * Function returns the index of agents of a particular type in this object. Where this index is
   * equal to the array index for each vector and matrix that stores local and global trust values
   * relating to that agent.
   *
   * @param agentType type of agent which agent indices should be returned for.
   * @return list of indices of agents which have type equal to that of agentType
   */
  public List<Integer> getAgentIndexOfType(AgentTypeWrapper agentType) {

    List<Integer> output = new ArrayList<>();

    for (Entry<Integer, Agent> agentIndex : agentHashMap.get(0).entrySet()) {

      if (agentIndex.getValue().getAgentTypes() == agentType) {
        output.add(agentIndex.getKey());
      }
    }
    return output;
  }

  public List<double[][]> getSuccessfulTransactionVectorAll() {
    return successfulTransactionVector;
  }

  public void setSuccessfulTransactionVector(
      int simulationNumber, double[][] successfulTransactionVector) {
    this.successfulTransactionVector.add(successfulTransactionVector);
  }

  public List<double[][]> getUnsuccessfulTransactionVectorAll() {
    return unsuccessfulTransactionVector;
  }

  public void setUnsuccessfulTransactionVector(
      int simulationNumber, double[][] unsuccessfulTransactionVector) {
    this.unsuccessfulTransactionVector.add(unsuccessfulTransactionVector);
  }

  public List<double[]> getAgentPreTrustedVectorAll() {
    return agentPreTrustedVector;
  }

  public void setAgentPreTrustedVector(int simulationNumber, double[] agentPreTrustedVector) {
    this.agentPreTrustedVector.add(agentPreTrustedVector);
  }

  public void setAgentHashMap(int simulationNumber, HashMap<Integer, Agent> agentHashMap) {
    this.agentHashMap.add(agentHashMap);
  }

  public List<HashMap<Integer, Agent>> getAgentHashMapAll() {
    return agentHashMap;
  }

  /**
   * Function returns a list of arrays of agent personality trust values during each agent system
   * simulation replication.
   *
   * @return list of arrays of agent personality trust value
   */
  public List<double[]> getAgentPersonalityTrustValuesAll() {

    List<double[]> result = new ArrayList<>();

    for (int i = 0; i < numberOfSimulations; i++) {
      double[] resultEach = new double[numberOfAgents];
      int j = 0;
      for (Map.Entry<Integer, Agent> agent : agentHashMap.get(i).entrySet()) {
        resultEach[j] = agent.getValue().getAgentPersonalityTrustValue(0);
        j++;
      }
      result.add(resultEach);
    }

    return result;
  }

  public void clearDiscreteServiceRequestHistory() {
    this.globalTrustVectorTimeSeries.clear();
  }

  public void setDiscreteServiceRequestHistory(
      int simulationNumber, LinkedList<ServiceRequest> serviceRequestHistory) {
    this.serviceRequestHistory.add(serviceRequestHistory);
  }

  /**
   * Function used to generate a JSON string representation of this object, for use in exportation
   * to JSON file; for a particular simulation number.
   *
   * @param simulationNumber simulation number of results from agent system to be exported
   * @return JSON object
   */
  public String toJsonString(int simulationNumber) {

    JSONObject output = new JSONObject();
    output.put("Agent System Name", getSystemName());
    output.put("Trust Model", trustModel.toString());

    JSONArray serviceRequestHistoryList = new JSONArray();
    for (ServiceRequest v : serviceRequestHistory.get(simulationNumber)) {
      serviceRequestHistoryList.put(v.toJsonObject());
    }
    output.put("Service Request History", serviceRequestHistoryList);

    JSONArray localTrustConnectionMatrixList = new JSONArray();
    for (int i = 0; i < localTrustConnectionMatrix.get(simulationNumber).length; i++) {
      JSONArray localTrustConnectionMatrixListRow = new JSONArray();
      for (int j = 0; j < localTrustConnectionMatrix.get(simulationNumber)[i].length; j++) {
        localTrustConnectionMatrixListRow.put(
            localTrustConnectionMatrix.get(simulationNumber)[i][j]);
      }
      localTrustConnectionMatrixList.put(localTrustConnectionMatrixListRow);
    }
    output.put("Local Trust Connection Matrix", localTrustConnectionMatrixList);

    JSONArray globalTrustVectorList = new JSONArray();
    for (double v : globalTrustVector.get(simulationNumber)) {
      globalTrustVectorList.put(v);
    }
    output.put("Global Trust Vector", globalTrustVectorList);

    JSONArray successfulTransactionVectorList = new JSONArray();
    for (int i = 0; i < successfulTransactionVector.get(simulationNumber).length; i++) {
      JSONArray successfulTransactionVectorListRow = new JSONArray();
      for (int j = 0; j < successfulTransactionVector.get(simulationNumber)[i].length; j++) {
        successfulTransactionVectorListRow.put(
            successfulTransactionVector.get(simulationNumber)[i][j]);
      }
      successfulTransactionVectorList.put(successfulTransactionVectorListRow);
    }
    output.put("Successful Transaction Vector", successfulTransactionVectorList);

    JSONArray unsuccessfulTransactionVectorList = new JSONArray();
    for (int i = 0; i < unsuccessfulTransactionVector.get(simulationNumber).length; i++) {
      JSONArray unsuccessfulTransactionVectorListRow = new JSONArray();
      for (int j = 0; j < unsuccessfulTransactionVector.get(simulationNumber)[i].length; j++) {
        unsuccessfulTransactionVectorListRow.put(
            unsuccessfulTransactionVector.get(simulationNumber)[i][j]);
      }
      unsuccessfulTransactionVectorList.put(unsuccessfulTransactionVectorListRow);
    }
    output.put("Unsuccessful Transaction Vector", unsuccessfulTransactionVectorList);

    JSONArray agentPreTrustedVectorList = new JSONArray();
    for (double v : agentPreTrustedVector.get(simulationNumber)) {
      agentPreTrustedVectorList.put(v);
    }
    output.put("Agent Pre Trusted Vector", agentPreTrustedVectorList);

    JSONArray agentPersonalityVectorList = new JSONArray();
    for (Agent agent : agentHashMap.get(simulationNumber).values()) {
      agentPersonalityVectorList.put(agent.getAgentPersonalityTrustValue(0));
    }
    output.put("Agent Personality Trust Vector", agentPersonalityVectorList);

    output.put("Number of Agents", numberOfAgents);
    output.put("Number of Service Requests", numberOfServiceRequests);
    output.put("Number of Trusted Agents", numberOfTrustedAgents);
    output.put("Number of Very Good Agents", numberOfVGoodAgents);
    output.put("Number of Good Agents", numberOfGoodAgents);
    output.put("Number of Ok Agents", numberOfOkAgents);
    output.put("Number of Bad Agents", numberOfBadAgents);
    output.put("Number of Very Bad Agents", numberOfVBadAgents);
    output.put("Number of Malicious Agents", numberOfMaliciousAgents);
    output.put("Trust Update Interval", trustUpdateInterval);

    return output.toString();
  }

  /**
   * Function returns list containing a the service request histories for the agent system during
   * each simulation of the agent system. Where each linked list consisting of a series of service
   * requests which took place in a particular simulation). Each list of linked list contains a
   * single linked list object for each simulation replication.
   *
   * @return list service request histories for each simulation of the agent system
   */
  public List<LinkedList<ServiceRequest>> getServiceRequestHistoryAll() {
    return serviceRequestHistory;
  }

  public List<LinkedList<ContinuousServiceRequest>> getContinuousServiceRequestHistoryAll() {
    return continuousServiceRequestHistory;
  }

  /**
   * Function returns trust model name of trust model assigned to the agent system.
   *
   * @return trust model name
   */
  public String getTrustModelName() {
    if (trustModel == null) {
      return "unassigned";
    }
    return trustModel.toString();
  }

  public void setContinuousServiceRequestHistory(
      int simulationNumber, LinkedList<ContinuousServiceRequest> simulationServiceRequestHistory) {
    continuousServiceRequestHistory.add(simulationServiceRequestHistory);
  }

  /**
   * Function used to reset the agent system, by removing any data resulting from an agent system
   * simulation, while retaining all data on the agents contained within the system.
   */
  public void resetAgentSystem(
      boolean resetAgentHashMap,
      boolean resetAgentPreTrustedVector,
      boolean resetServiceRequestHistory) {
    // Simulation Data
    if (resetAgentHashMap) {
      agentHashMap.clear();
    }
    if (resetServiceRequestHistory) {
      serviceRequestHistory.clear();
    }
    continuousServiceRequestHistory.clear();

    // EIGENAGENTSYSTEM
    globalTrustVector.clear();
    globalTrustVectorTimeSeries.clear();
    localTrustConnectionMatrix.clear();
    successfulTransactionVector.clear();
    unsuccessfulTransactionVector.clear();
    if (resetAgentPreTrustedVector) {
      agentPreTrustedVector.clear();
    }
  }

  public void setNumberOfAgents(int numberOfAgents) {
    this.numberOfAgents = numberOfAgents;
  }
}
