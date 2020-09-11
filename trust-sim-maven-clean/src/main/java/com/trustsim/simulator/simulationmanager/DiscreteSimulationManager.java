package com.trustsim.simulator.simulationmanager;

import com.trustsim.MatrixAlgebra;
import com.trustsim.simulator.agent.Agent;
import com.trustsim.simulator.trustmodel.DynamicTrustModel;
import com.trustsim.simulator.trustmodel.EigenTrustModel;
import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.synthesiser.AgentSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Simulation Engine which run Simulations where Transactions occur as Discrete events, that is
 * Service Requests are DiscreteServiceRequest objects which are made up of a single Service Request
 * events.
 */
public class DiscreteSimulationManager implements SimulationManager {

  private final AgentSystem agentSystem;
  private final TrustModel trustModel;
  private double[] globalTrustVector;
  private final int numberOfAgents;
  private final int numberOfServiceRequests;
  private final int numberOfServiceTypes;
  private final int trustUpdateInterval;
  private final List<double[]> globalTrustVectorTimeSeries = new ArrayList<>();
  private final LinkedList<ServiceRequest> serviceRequestHistory = new LinkedList<>();
  private HashMap<Integer, Agent> agentHashMap = new HashMap<>();

  /**
   * Constructor initialises Simulation Manager with Agent System used in the simulation as well as
   * the parameters which determine the length and characteristics of the simulation.
   *
   * @param agentSystem agent system containing agents which simulation will operate in
   * @param trustModel trust model used to calculate local and global trust values during the
   *     simulation
   * @param numberOfServiceRequests number of service request events in the simulation, denotes the
   *     total length of a simulation
   * @param numberOfServiceTypes number of different service request types in the simulation
   * @param trustUpdateInterval time interval, measured in number of service request events, at
   *     which the local and global trust values should be recomputed.
   */
  public DiscreteSimulationManager(
      AgentSystem agentSystem,
      TrustModel trustModel,
      int numberOfServiceRequests,
      int numberOfServiceTypes,
      int trustUpdateInterval) {
    this.agentSystem = agentSystem;
    this.trustModel = trustModel;
    this.numberOfServiceRequests = numberOfServiceRequests;
    this.numberOfServiceTypes = numberOfServiceTypes;
    this.trustUpdateInterval = trustUpdateInterval;
    this.numberOfAgents = agentSystem.getNumberOfAgents();

    this.globalTrustVector = new double[numberOfAgents];

    // Copy Trust Model Parameters to Agent System

    if (trustModel instanceof DynamicTrustModel) {
      this.agentSystem.setAlphaConstantValue(((EigenTrustModel) trustModel).getAlphaConst());
      this.agentSystem.setEpsilonConstantValue(((EigenTrustModel) trustModel).getEpsilonConst());
      this.agentSystem.setDecayConstantValue(((DynamicTrustModel) trustModel).getDecayConst());
    } else if (trustModel instanceof EigenTrustModel) {
      this.agentSystem.setAlphaConstantValue(((EigenTrustModel) trustModel).getAlphaConst());
      this.agentSystem.setEpsilonConstantValue(((EigenTrustModel) trustModel).getEpsilonConst());
    }

    this.agentSystem.setNumberOfServiceRequests(numberOfServiceRequests);
    this.agentSystem.setNumberOfServiceRequestTypes(numberOfServiceTypes);
    this.agentSystem.setTrustUpdateInterval(trustUpdateInterval);
  }

  /**
   * Function which initialises the Simulation Manager with the agents present in the agent system
   * object, for a particular simulation replication number.
   *
   * @param simulationNumber replication number of the simulation
   */
  private void initialiseSimulation(int simulationNumber) {

    agentHashMap =
        trustModel.initialiseTrustModel(
            agentSystem.getNumberOfVGoodAgents(),
            agentSystem.getNumberOfGoodAgents(),
            agentSystem.getNumberOfOkAgents(),
            agentSystem.getNumberOfBadAgents(),
            agentSystem.getNumberOfVBadAgents(),
            agentSystem.getNumberOfTrustedAgents(),
            agentSystem.getNumberOfMaliciousAgents(),
            agentSystem.getNumberOfElasticAgents(),
            0,
            agentSystem.getElasticAgentStartPersonality(),
            agentSystem.getElasticAgentEndPersonality(),
            agentSystem.getNumberOfServiceRequests(),
            agentSystem.getNumberOfServiceRequestTypes());
  }

  /**
   * Function used to randomly generate a new Service Request object, which is assigned to a
   * particular consumer. The producer of the service request event is decided by the consumer agent
   * object.
   *
   * @param numberOfServiceTypes number of service request types which could be chosen from for this
   *     service request event.
   * @return generated discrete service request object
   */
  private DiscreteServiceRequest generateServiceRequest(int numberOfServiceTypes) {

    Integer serviceType = (int) (Math.random() * numberOfServiceTypes);
    DiscreteServiceRequest request = new DiscreteServiceRequest(serviceType);

    // Assign Random Consumer to Request
    request.setConsumer((int) (Math.random() * agentHashMap.values().size()));

    Integer producerAgent =
        agentHashMap.get(request.getConsumer()).assignRequestProducer(request, globalTrustVector);
    request.setProducer(producerAgent);
    return request;
  }

  /**
   * Function used to initialise and start the simulation.
   *
   * @param simulationNumber replication number of the simulation to be run
   * @return agent system at the end of the simulation, containing results dataset of the simulation
   */
  @Override
  public AgentSystem runSimulation(int simulationNumber) {

    initialiseSimulation(simulationNumber);
    // Service Requests
    for (int simulationTime = 0; simulationTime < numberOfServiceRequests; simulationTime++) {

      runSimulationAtTime(simulationTime);

      if (simulationTime % trustUpdateInterval == 0.0) {
        updateGlobalTrustValues();
      }
    }

    // Save Data to Agent System
    trustModel.saveToAgentSystem(agentSystem, simulationNumber);
    agentSystem.setGlobalTrustVectorTimeSeries(simulationNumber, globalTrustVectorTimeSeries);
    agentSystem.setAgentHashMap(simulationNumber, agentHashMap);
    agentSystem.setDiscreteServiceRequestHistory(simulationNumber, serviceRequestHistory);
    agentSystem.setNumberOfSimulations(simulationNumber + 1);

    return agentSystem;
  }

  /**
   * Function used to update the global trust values based on calling the trust model that has been
   * selected for this simulation.
   */
  private void updateGlobalTrustValues() {
    double[] trustPrevious = new double[numberOfAgents];
    double[] trustNew = new double[numberOfAgents];

    double difference = Double.MAX_VALUE;
    while (difference >= trustModel.getConvergenceLimit()) {
      trustNew = trustModel.updateGlobalTrustValues();
      difference = MatrixAlgebra.vectorDifferenceMagnitude(trustNew, trustPrevious);
      trustPrevious = trustNew;
    }

    globalTrustVector = trustNew;
    globalTrustVectorTimeSeries.add(globalTrustVector);
  }

  /**
   * Function called at each time step of the simulation, which manages the dispatch of new service
   * request events, check that ongoing continuous service request events are being completed
   * successfully, and updates local and global trust values.
   *
   * @param simulationTime current time in the simulation
   */
  private void runSimulationAtTime(int simulationTime) {
    DiscreteServiceRequest newRequest = generateServiceRequest(numberOfServiceTypes);
    newRequest.setRequestTime(simulationTime);

    Integer transactionProducer = newRequest.getProducer();

    newRequest.setTransactionOutcome(
        agentHashMap.get(transactionProducer).executeTransaction(newRequest, simulationTime));

    // Update Local Trust & transaction history matrix
    trustModel.updateLocalTrustValues(newRequest);
    trustModel.updateTransactionHistoryMatrix(newRequest, "DiscreteServiceRequest", true);

    // Add request to Queue
    serviceRequestHistory.add(newRequest);
  }
}
