package com.trustsim.simulator.simulationmanager;

import com.trustsim.MatrixAlgebra;
import com.trustsim.simulator.agent.AgentType;
import com.trustsim.simulator.trustmodel.DynamicTrustModel;
import com.trustsim.simulator.trustmodel.EigenTrustModel;
import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.synthesiser.AgentSystem;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CalculationSimulationManager implements SimulationManager {
  private final AgentSystem agentSystem;
  private final TrustModel trustModel;

  private double[] globalTrustVector;
  private final int numberOfAgents;
  private final int numberOfServiceRequests;
  private final int numberOfServiceTypes;
  private final int trustUpdateInterval;
  private final List<double[]> globalTrustVectorTimeSeries = new ArrayList<>();
  private final LinkedList<ServiceRequest> serviceRequestHistory = new LinkedList<>();
  private final Queue<ServiceRequest> serviceRequestQueue;

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
  public CalculationSimulationManager(
      AgentSystem agentSystem,
      TrustModel trustModel,
      int numberOfServiceRequests,
      int numberOfServiceTypes,
      int trustUpdateInterval,
      Queue<ServiceRequest> serviceRequestQueue) {
    this.agentSystem = agentSystem;
    this.trustModel = trustModel;
    this.numberOfServiceRequests = numberOfServiceRequests;
    this.numberOfServiceTypes = numberOfServiceTypes;
    this.trustUpdateInterval = trustUpdateInterval;
    this.numberOfAgents = agentSystem.getNumberOfAgents();
    this.globalTrustVector = new double[numberOfAgents];
    this.serviceRequestQueue = serviceRequestQueue;

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
   * Function used to initialise and start the simulation.
   *
   * @param simulationNumber replication number of the simulation to be run
   * @return agent system at the end of the simulation, containing results dataset of the simulation
   */
  @Override
  public AgentSystem runSimulation(int simulationNumber) {
    trustModel.initialiseTrustModel(
        0,
        0,
        0,
        0,
        0,
        2,
        0,
        0,
        numberOfAgents - 2,
        AgentType.REALUSER,
        AgentType.REALUSER,
        agentSystem.getNumberOfServiceRequests(),
        agentSystem.getNumberOfServiceRequestTypes());

    for (int simulationTime = 0; simulationTime < numberOfServiceRequests; simulationTime++) {

      runSimulationAtTime(simulationTime);

      if (simulationTime % trustUpdateInterval == 0.0) {
        updateGlobalTrustValues();
      }
    }

    // Save Data to Agent System
    trustModel.saveToAgentSystem(agentSystem, simulationNumber);
    agentSystem.setGlobalTrustVectorTimeSeries(-1, globalTrustVectorTimeSeries);
    agentSystem.setDiscreteServiceRequestHistory(-1, serviceRequestHistory);
    agentSystem.setNumberOfSimulations(simulationNumber + 1);
    agentSystem.setTrustUpdateInterval(10);
    agentSystem.setNumberOfServiceRequests(numberOfServiceRequests);
    agentSystem.setNumberOfServiceRequestTypes(1);
    agentSystem.setNumberOfAgents(numberOfAgents);

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

    if (!serviceRequestQueue.isEmpty()) {
      while (!serviceRequestQueue.isEmpty()
          && serviceRequestQueue.peek().getRequestTime() < simulationTime) {
        serviceRequestQueue.poll();
      }

      while (!serviceRequestQueue.isEmpty()
          && serviceRequestQueue.peek().getRequestTime() == simulationTime) {
        DiscreteServiceRequest newDiscreteRequest = new DiscreteServiceRequest(0);

        ServiceRequest request = serviceRequestQueue.poll();
        newDiscreteRequest.setRequestTime(request.getRequestTime());
        newDiscreteRequest.setCompletionTime(request.getCompletionTime());
        newDiscreteRequest.setTransactionOutcome(request.isCompleted());
        newDiscreteRequest.setConsumer(request.getConsumer());
        newDiscreteRequest.setProducer(request.getProducer());

        // Update Local Trust & transaction history matrix
        trustModel.updateLocalTrustValues(newDiscreteRequest);
        trustModel.updateTransactionHistoryMatrix(
            newDiscreteRequest, "DiscreteServiceRequest", true);

        // Add request to Queue
        serviceRequestHistory.add(newDiscreteRequest);
      }
    }
  }
}
