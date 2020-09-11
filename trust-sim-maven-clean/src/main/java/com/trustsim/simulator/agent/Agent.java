package com.trustsim.simulator.agent;

import com.trustsim.evaluator.AgentTypeWrapper;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class which represents a Trust System Agent, which describes the Agent's AgentTypeWrapper (which
 * represents its trustworthiness and personality at different points in time during a simulation).
 */
public class Agent {

  private final int id;
  protected AgentTypeWrapper agentTypes;

  /**
   * Constructor of Agent object.
   *
   * @param id unique identifier of agent
   * @param agentTypes agent's personality object
   */
  public Agent(int id, AgentTypeWrapper agentTypes) {
    this.id = id;
    this.agentTypes = agentTypes;
  }

  public AgentTypeWrapper getAgentTypes() {
    return agentTypes;
  }

  /**
   * Function used to request that this agent object determine which producer agent should carry out
   * a transaction that this agent has requested. This producer agent is then assigned to the
   * ServiceRequest object.
   *
   * @param request object of the service request of a transaction
   * @param globalTrustVector vector representing the current global trust values of each agent in a
   *     simulation
   * @return index of producer chosen to carry out the transaction
   */
  public Integer assignRequestProducer(ServiceRequest request, double[] globalTrustVector) {

    // Assign Random Producer to Request
    int producerAgent = 0; // default value

    // Compute total trust value and number of zero trust agents
    double globalTrustValueTotal = 0.0;
    double zeroTrustAgentsCount = 0.0;
    for (double globalTrustValue : globalTrustVector) {
      globalTrustValueTotal += globalTrustValue;
      if (globalTrustValue == 0.0) {
        zeroTrustAgentsCount++;
      }
    }

    // Assign probability to each agent being picked based on their global trust value
    double[] agentProbabilities = new double[globalTrustVector.length];
    if (zeroTrustAgentsCount == 0.0) {
      for (int k = 0; k < globalTrustVector.length; k++) {
        if (k == id) {
          agentProbabilities[k] = 0.0;
        } else {
          agentProbabilities[k] = globalTrustVector[k] / globalTrustValueTotal;
        }
      }
    } else {
      for (int k = 0; k < globalTrustVector.length; k++) {
        if (k == id) {
          agentProbabilities[k] = 0.0;
        } else {
          if (globalTrustVector[k] == 0.0) {
            agentProbabilities[k] = 0.1 / zeroTrustAgentsCount;
          } else {
            agentProbabilities[k] = 0.9 * globalTrustVector[k] / globalTrustValueTotal;
          }
        }
      }
    }

    // Calculate sum of probabilities, to check it sums to 1.0
    double sumOfProbabilities = 0.0;
    for (double v : agentProbabilities) {
      sumOfProbabilities += v;
    }

    // Randomly select a Producer based on their probabilities
    double randomProbability = Math.random() * sumOfProbabilities;
    while (randomProbability > 0.0) {
      for (int q = 0; q < agentProbabilities.length; q++) {
        randomProbability -= agentProbabilities[q];
        if (randomProbability <= 0.0) {
          producerAgent = q;
          break;
        }
      }
    }

    return producerAgent;
  }

  /**
   * Function used to obtain the personality trust value of this agent object at a particular point
   * in time in a simulation.
   *
   * @param simulationTime the time for which agent personality should be returned
   * @return the agent personality at this point in time
   */
  public double getAgentPersonalityTrustValue(int simulationTime) {

    List<Integer> sortedChangeTimes = new ArrayList<>(agentTypes.getTypeChangeTimes());
    Collections.sort(sortedChangeTimes);

    AgentType currentType = agentTypes.getAgentTypesMap().get(0);
    for (Integer changeTime : sortedChangeTimes) {
      if (changeTime > simulationTime) {
        currentType = agentTypes.getAgentTypesMap().get(changeTime);
      }
    }
    return currentType.mean;
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
  public boolean executeTransaction(ServiceRequest newRequest, int simulationTime) {

    List<Integer> sortedChangeTimes = new ArrayList<>(agentTypes.getTypeChangeTimes());
    Collections.sort(sortedChangeTimes);

    AgentType currentType = agentTypes.getAgentTypesMap().get(0);
    for (Integer changeTime : sortedChangeTimes) {
      if (changeTime < simulationTime) {
        currentType = agentTypes.getAgentTypesMap().get(changeTime);
      }
    }

    Random r = new Random();
    double transaction = ((r.nextGaussian() * currentType.stDev) + currentType.mean);
    double random = Math.random();
    return random < transaction;
  }
}
