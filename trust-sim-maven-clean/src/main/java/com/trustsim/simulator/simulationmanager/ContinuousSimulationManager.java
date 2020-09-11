package com.trustsim.simulator.simulationmanager;

import com.trustsim.MatrixAlgebra;
import com.trustsim.simulator.agent.Agent;
import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.synthesiser.AgentSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javafx.util.Pair;

/**
 * Simulation Engine which run Simulations where Transactions occur as Continuous events, that is
 * Service Requests are ContinuousServiceRequest objects which are made up of multiple, regularly
 * spaced, Service Request events.
 */
public class ContinuousSimulationManager implements SimulationManager {

  private final AgentSystem agentSystem;
  private final TrustModel trustModel;
  private double[] globalTrustVector;
  private int numberOfContinuousServiceRequests = 0;
  private final int numberOfAgents;
  private final int numberOfIterations;
  private final int numberOfServiceTypes;
  private final int trustUpdateInterval;
  private final List<double[]> globalTrustVectorTimeSeries = new ArrayList<>();
  private final LinkedList<ContinuousServiceRequest> serviceRequestHistory = new LinkedList<>();
  private final LinkedList<ServiceRequest> standardServiceRequestHistory = new LinkedList<>();
  private HashMap<Integer, Agent> agentHashMap = new HashMap<>();
  private final Queue<Pair<Integer, ContinuousServiceRequest>> eventToCorrespondingRequestQueue =
      new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));

  /**
   * Constructor initialises Simulation Manager with Agent System used in the simulation as well as
   * the parameters which determine the length and characteristics of the simulation.
   *
   * @param agentSystem agent system containing agents which simulation will operate in
   * @param trustModel trust model used to calculate local and global trust values during the
   *     simulation
   * @param numberOfServiceRequests number of service request events in the simulation, denotes the
   *     total length of a simulation since continuous service requests are only dispatched every 10
   *     events.
   * @param numberOfServiceTypes number of different service request types in the simulation
   * @param trustUpdateInterval time interval, measured in number of service request events, at
   *     which the local and global trust values should be recomputed.
   */
  public ContinuousSimulationManager(
      AgentSystem agentSystem,
      TrustModel trustModel,
      int numberOfServiceRequests,
      int numberOfServiceTypes,
      int trustUpdateInterval) {
    this.agentSystem = agentSystem;
    this.trustModel = trustModel;
    this.numberOfIterations = numberOfServiceRequests;
    this.numberOfServiceTypes = numberOfServiceTypes;
    this.trustUpdateInterval = trustUpdateInterval;
    this.numberOfAgents = agentSystem.getNumberOfAgents();

    this.globalTrustVector = new double[numberOfAgents];
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
   * Function used to add a new service request to the simulation event queue.
   *
   * @param request service request
   */
  private void addServiceRequestEventsToQueue(ContinuousServiceRequest request) {

    Set<Integer> serviceRequestEvents = request.getServiceRequestEvents().keySet();

    for (Integer event : serviceRequestEvents) {
      eventToCorrespondingRequestQueue.add(new Pair<>(event, request));
    }
  }

  /**
   * Function used to randomly generate a new Service Request object, which is assigned to a
   * particular consumer. The producer of the service request event is decided by the consumer agent
   * object.
   *
   * @param agentHashMap map containing each agent in the simulation environment
   * @param numberOfServiceTypes number of service request types which could be chosen from for this
   *     service request event.
   * @param serviceRequestStartTime start time denoting when the service request should be
   *     dispatched at
   * @return generated continuous service request object
   */
  private ContinuousServiceRequest generateServiceRequest(
      HashMap<Integer, Agent> agentHashMap, int numberOfServiceTypes, int serviceRequestStartTime) {

    int serviceType = (int) (Math.random() * numberOfServiceTypes);
    ContinuousServiceRequest request =
        new ContinuousServiceRequest(
            serviceType, numberOfContinuousServiceRequests++, serviceRequestStartTime);

    // Populate ContinuousServiceRequest
    double lengthOfServiceRequest = (int) (Math.random() * 0.1 * numberOfIterations);
    request.generateServiceRequestEvents(lengthOfServiceRequest, trustUpdateInterval);

    // Assign Random Consumer to Request
    Integer consumerAgent = (int) (Math.random() * agentHashMap.size());
    request.setConsumer(consumerAgent);

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
    for (int simulationTime = 0; simulationTime < numberOfIterations; simulationTime++) {

      runSimulationAtTime(simulationTime);

      if (simulationTime % trustUpdateInterval == 0.0) {
        updateGlobalTrustValues();
      }
    }

    // Save Data to Agent System
    trustModel.saveToAgentSystem(agentSystem, simulationNumber);
    agentSystem.setGlobalTrustVectorTimeSeries(simulationNumber, globalTrustVectorTimeSeries);
    agentSystem.setAgentHashMap(simulationNumber, agentHashMap);
    agentSystem.setDiscreteServiceRequestHistory(simulationNumber, standardServiceRequestHistory);
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
    if (simulationTime % 10 == 0) {
      ContinuousServiceRequest newRequest =
          generateServiceRequest(agentHashMap, numberOfServiceTypes, simulationTime);
      newRequest.setRequestTime(simulationTime);
      addServiceRequestEventsToQueue(newRequest);
    }

    while (eventToCorrespondingRequestQueue.size() > 0
        && eventToCorrespondingRequestQueue.peek().getKey() <= simulationTime) {

      while (eventToCorrespondingRequestQueue.size() > 0
          && eventToCorrespondingRequestQueue.peek().getKey() < simulationTime) {
        eventToCorrespondingRequestQueue.poll();
      }

      ContinuousServiceRequest request =
          Objects.requireNonNull(eventToCorrespondingRequestQueue.poll()).getValue();
      Integer transactionProducer = request.getProducer();

      boolean doesEventTransactionComplete =
          agentHashMap
              .get(transactionProducer)
              .executeTransaction(
                  request.getSingularServiceRequest(simulationTime), simulationTime);
      request.setEventTransactionOutcome(simulationTime, doesEventTransactionComplete);

      if (!request.getSingularServiceRequest(simulationTime).isCompleted()) {
        // if transaction not completed -> then add 1 strike to unpaid transaction
        if (request.addUnpaidPayment() > 3) {
          // then mark as failed ContinuousTransaction
          request.setEventTransactionOutcome(simulationTime, false);
          // remove future events from eventToCorrespondingRequest queue
          eventToCorrespondingRequestQueue.removeIf(i -> i.getValue() == request);

          // Add failed request to standardServiceRequestHistory
          ServiceRequest standardRequest =
              new ServiceRequest(
                  request.getServiceTrustCharacteristics(), request.getRequestTime());
          standardRequest.setConsumer(request.getConsumer());
          standardRequest.setProducer(request.getProducer());
          standardRequest.setTransactionOutcome(request.isCompleted());
          standardServiceRequestHistory.add(standardRequest);
        }
      } else {
        request.clearUnpaidPayment();
      }

      if (request.isCompleted()) {
        trustModel.updateTransactionHistoryMatrix(request, "ContinuousServiceRequest", true);

        // add successful request to standardServiceRequestHistory
        ServiceRequest standardRequest =
            new ServiceRequest(request.getServiceTrustCharacteristics(), request.getRequestTime());
        standardRequest.setConsumer(request.getConsumer());
        standardRequest.setProducer(request.getProducer());
        standardRequest.setTransactionOutcome(request.isCompleted());
        standardServiceRequestHistory.add(standardRequest);
      } else {
        trustModel.updateTransactionHistoryMatrix(
            request,
            "ServiceRequest",
            request.getSingularServiceRequest(simulationTime).isCompleted());
      }
      trustModel.updateLocalTrustValues(request);

      // Add request to Queue
      serviceRequestHistory.add(request);
    }
  }
}
