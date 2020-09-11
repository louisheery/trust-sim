package com.trustsim.simulator.trustmodel;

import static com.trustsim.simulator.agent.AgentType.BAD;
import static com.trustsim.simulator.agent.AgentType.GOOD;
import static com.trustsim.simulator.agent.AgentType.ISTRUSTED;
import static com.trustsim.simulator.agent.AgentType.OK;
import static com.trustsim.simulator.agent.AgentType.REALUSER;
import static com.trustsim.simulator.agent.AgentType.VBAD;
import static com.trustsim.simulator.agent.AgentType.VGOOD;

import com.trustsim.evaluator.AgentTypeWrapper;
import com.trustsim.simulator.agent.Agent;
import com.trustsim.simulator.agent.AgentType;
import com.trustsim.simulator.agent.MaliciousAgent;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import com.trustsim.synthesiser.AgentSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Trust Model object which represents the mathematical formulae of the EigenTrust Model; as
 * originally described in: Kamvar SD, Schlosser MT, Garcia-Molina H. The eigentrust algorithm for
 * reputation management in p2p networks. InProceedings of the 12th international conference on
 * World Wide Web 2003 May 20 (pp. 640-651).
 */
public class EigenTrustModel extends TrustModel {

  protected int numberOfAgents = 0;
  public double alphaConst = 0.5;
  public double epsilonConst = 0.001;
  private double[][] localTrustConnectionMatrix;
  private double[] agentPreTrustedVector;

  private final HashMap<Integer, Agent> agentHashMap = new HashMap<>();

  public EigenTrustModel() {}

  public EigenTrustModel(double alphaConst, double epsilonConst) {
    this.alphaConst = alphaConst;
    this.epsilonConst = epsilonConst;
  }

  public double getAlphaConst() {
    return alphaConst;
  }

  public double getEpsilonConst() {
    return epsilonConst;
  }

  public void setAlphaConst(double alphaConst) {
    this.alphaConst = alphaConst;
  }

  public void setEpsilonConst(double epsilonConst) {
    this.epsilonConst = epsilonConst;
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
  @Override
  public HashMap<Integer, Agent> initialiseTrustModel(
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
      int numberOfServiceTypes) {

    this.numberOfAgents =
        veryGoodAgents
            + goodAgents
            + okAgents
            + badAgents
            + veryBadAgents
            + trustedAgents
            + maliciousAgents
            + elasticAgents
            + realUserAgents;

    globalTrustVector = new double[numberOfAgents];
    localTrustConnectionMatrix = new double[numberOfAgents][numberOfAgents];
    successfulTransactionVector = new double[numberOfAgents][numberOfAgents];
    unsuccessfulTransactionVector = new double[numberOfAgents][numberOfAgents];
    agentPreTrustedVector = new double[numberOfAgents];

    // 0.1 Populate Successful & Unsuccessful Transactions Matrix with Zeros
    for (int i = 0; i < numberOfAgents; i++) {
      for (int j = 0; j < numberOfAgents; j++) {
        successfulTransactionVector[i][j] = 0.0;
        unsuccessfulTransactionVector[i][j] = 0.0;
      }
    }

    // 0.2 Populate Agents PreTruthful Vector
    for (int i = 0; i < numberOfAgents; i++) {
      if (i < trustedAgents) {
        agentPreTrustedVector[i] = 1.0 / (double) trustedAgents;
      } else {
        agentPreTrustedVector[i] = 0.0;
      }
    }

    // 0.3 Initialise LocalTrustConnectionMatrix to equal Agent PreTruthful Vector
    for (int i = 0; i < numberOfAgents; i++) {
      System.arraycopy(agentPreTrustedVector, 0, localTrustConnectionMatrix[i], 0, numberOfAgents);
    }

    // 0.4 Populate Global Trust Scores Array with Zeros
    System.arraycopy(agentPreTrustedVector, 0, globalTrustVector, 0, numberOfAgents);

    // 0.5 Populate Personality Array
    int agentIndex = 0;

    for (int i = 0; i < trustedAgents; i++) {
      agentHashMap.put(agentIndex, new Agent(agentIndex, new AgentTypeWrapper(ISTRUSTED, 0)));
      agentIndex++;
    }

    for (int i = 0; i < veryGoodAgents; i++) {
      agentHashMap.put(agentIndex, new Agent(agentIndex, new AgentTypeWrapper(VGOOD, 0)));
      agentIndex++;
    }

    for (int i = 0; i < goodAgents; i++) {
      agentHashMap.put(agentIndex, new Agent(agentIndex, new AgentTypeWrapper(GOOD, 0)));
      agentIndex++;
    }

    for (int i = 0; i < okAgents; i++) {
      agentHashMap.put(agentIndex, new Agent(agentIndex, new AgentTypeWrapper(OK, 0)));
      agentIndex++;
    }

    for (int i = 0; i < badAgents; i++) {
      agentHashMap.put(agentIndex, new Agent(agentIndex, new AgentTypeWrapper(BAD, 0)));
      agentIndex++;
    }

    for (int i = 0; i < veryBadAgents; i++) {
      agentHashMap.put(agentIndex, new Agent(agentIndex, new AgentTypeWrapper(VBAD, 0)));
      agentIndex++;
    }

    for (int i = 0; i < elasticAgents; i++) {
      agentHashMap.put(
          agentIndex,
          new Agent(
              agentIndex,
              new AgentTypeWrapper(
                  new ArrayList<>(
                      Arrays.asList(elasticAgentStartPersonality, elasticAgentEndPersonality)),
                  new ArrayList<>(Arrays.asList(0, (int) (0.5 * numberOfEvents))))));
      agentIndex++;
    }

    for (int i = 0; i < realUserAgents; i++) {
      agentHashMap.put(agentIndex, new Agent(agentIndex, new AgentTypeWrapper(REALUSER, 0)));
      agentIndex++;
    }

    // Add Malicious Agents
    List<Integer> maliciousAgentsList = new ArrayList<>();
    for (int i = 0; i < maliciousAgents; i++) {
      maliciousAgentsList.add(agentIndex);
    }

    for (int i = 0; i < maliciousAgentsList.size(); i++) {
      MaliciousAgent newAgent = new MaliciousAgent(agentIndex);
      newAgent.setOtherMaliciousAgents(maliciousAgentsList);
      agentHashMap.put(agentIndex, newAgent);
      agentIndex++;
    }

    return agentHashMap;
  }

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
  @Override
  public void updateTransactionHistoryMatrix(
      ServiceRequest request, String type, boolean subTransactionComplete) {

    if (type.equals("DiscreteServiceRequest")) {
      // make small change to the local trust values matrix
      if (request.isCompleted()) {
        successfulTransactionVector[request.getConsumer()][request.getProducer()] += 1;
      } else {
        unsuccessfulTransactionVector[request.getConsumer()][request.getProducer()] += 1;
      }

    } else if (type.equals("ContinuousServiceRequest")) {
      // makes a large change to the local trust values matrix
      if (request.isCompleted()) {
        successfulTransactionVector[request.getConsumer()][request.getProducer()] += 10;
      } else {
        unsuccessfulTransactionVector[request.getConsumer()][request.getProducer()] += 10;
      }

    } else {
      // makes small change to the local trust values matrix
      if (subTransactionComplete) {
        successfulTransactionVector[request.getConsumer()][request.getProducer()] += 1;
      } else {
        unsuccessfulTransactionVector[request.getConsumer()][request.getProducer()] += 1;
      }
    }
  }

  /**
   * Function updates a local trust values matrix that stores each agents local trust value in each
   * other agent in a simulation, based on the outcome of a new service request object.
   *
   * @param request service request object
   */
  @Override
  public void updateLocalTrustValues(ServiceRequest request) {
    double totalNetTransactionsValue = 0.0;
    for (int i = 0; i < localTrustConnectionMatrix.length; i++) {
      totalNetTransactionsValue +=
          Math.max(
              successfulTransactionVector[request.getConsumer()][i]
                  - unsuccessfulTransactionVector[request.getConsumer()][i],
              0.0);
    }

    if (totalNetTransactionsValue == 0.0) {
      for (int j = 0; j < localTrustConnectionMatrix.length; j++) {
        localTrustConnectionMatrix[request.getConsumer()][j] =
            agentPreTrustedVector[request.getProducer()];
      }

    } else {
      for (int j = 0; j < localTrustConnectionMatrix.length; j++) {
        localTrustConnectionMatrix[request.getConsumer()][j] =
            Math.max(
                    successfulTransactionVector[request.getConsumer()][j]
                        - unsuccessfulTransactionVector[request.getConsumer()][j],
                    0.0)
                / totalNetTransactionsValue;
      }
    }
  }

  /**
   * Function which recalculates the global trust values vector using the values stored within the
   * local trust values matrix for a simulation.
   */
  @Override
  public double[] updateGlobalTrustValues() {

    double[] newGlobalTrustVector = new double[globalTrustVector.length];

    double totalGlobalTrustValue = 0.0;
    for (int i = 0; i < newGlobalTrustVector.length; i++) {

      newGlobalTrustVector[i] = 0.0;

      for (int j = 0; j < globalTrustVector.length; j++) {
        newGlobalTrustVector[i] += (localTrustConnectionMatrix[j][i] * globalTrustVector[j]);
      }

      newGlobalTrustVector[i] =
          ((1 - alphaConst) * newGlobalTrustVector[i]) + (alphaConst * agentPreTrustedVector[i]);
      totalGlobalTrustValue += newGlobalTrustVector[i];
    }

    if (totalGlobalTrustValue != 0.0) {
      for (int k = 0; k < globalTrustVector.length; k++) {
        newGlobalTrustVector[k] = newGlobalTrustVector[k] / totalGlobalTrustValue;
      }
    }

    globalTrustVector = newGlobalTrustVector;
    return newGlobalTrustVector;
  }

  /**
   * Function returns convergence limit of Trust Model.
   *
   * @return convergence limit of trust model.
   */
  @Override
  public double getConvergenceLimit() {
    return epsilonConst;
  }

  /**
   * Function saves the current contents of the Trust Model into an Agent System object, so that the
   * results of this simulation can be saved and read by the Evaluator.
   *
   * @param agentSystem agent system
   * @param simulationNumber simulation number which results correspond to
   */
  @Override
  public void saveToAgentSystem(AgentSystem agentSystem, int simulationNumber) {
    agentSystem.setGlobalTrustVector(simulationNumber, globalTrustVector);
    agentSystem.setLocalTrustConnectionMatrix(simulationNumber, localTrustConnectionMatrix);
    agentSystem.setSuccessfulTransactionVector(simulationNumber, successfulTransactionVector);
    agentSystem.setUnsuccessfulTransactionVector(simulationNumber, unsuccessfulTransactionVector);
    agentSystem.setAgentPreTrustedVector(simulationNumber, agentPreTrustedVector);
  }
}
