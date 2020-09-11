package com.trustsim.simulator.trustmodel;

import com.trustsim.simulator.agent.Agent;
import com.trustsim.simulator.agent.AgentType;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import com.trustsim.synthesiser.AgentSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Trust Model object which represents the mathematical formulae of the DynamicTrust Model; a trust
 * model that extends the work of: Kamvar SD, Schlosser MT, Garcia-Molina H. The eigentrust
 * algorithm for reputation management in p2p networks. InProceedings of the 12th international
 * conference on World Wide Web 2003 May 20 (pp. 640-651).
 */
public class DynamicTrustModel extends EigenTrustModel {

  protected List<List<Map<Integer, Integer>>> successfulTransactionVectorElastic;
  protected List<List<Map<Integer, Integer>>> unsuccessfulTransactionVectorElastic;

  private double logisticDecayRate;
  private final double simulationLength;
  private final double logisticMidpoint;

  /**
   * Constructor which initialises DynamicTrustModel based on the hyper parameters of the model,
   * specifically, those values which describe the logistic function used within the model as well
   * as the alpha and epsilon constant values.
   *
   * @param logisticDecayRate decay rate of logistic function
   * @param simulationLength length of simulation
   * @param logisticMidpoint midpoint of logistic function
   * @param alphaConst describes weighting of opinion of trusted agents in calculation of local and
   *     global trust values
   * @param epsilonConst describes convergence limit of calculation of local trust values
   */
  public DynamicTrustModel(
      double logisticDecayRate,
      int simulationLength,
      int logisticMidpoint,
      double alphaConst,
      double epsilonConst) {
    super(alphaConst, epsilonConst);
    this.simulationLength = simulationLength;
    this.logisticDecayRate = logisticDecayRate;
    this.logisticMidpoint = 2.7;
    this.alphaConst = alphaConst;
    this.epsilonConst = epsilonConst;
  }

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
        trustedAgents
            + veryGoodAgents
            + goodAgents
            + okAgents
            + badAgents
            + veryBadAgents
            + maliciousAgents
            + elasticAgents
            + realUserAgents;

    successfulTransactionVectorElastic = new ArrayList<>();
    unsuccessfulTransactionVectorElastic = new ArrayList<>();

    // 0.1 Populate Successful & Unsuccessful Transactions Matrix with Zeros
    for (int i = 0; i < numberOfAgents; i++) {
      successfulTransactionVectorElastic.add(new ArrayList<>());
      unsuccessfulTransactionVectorElastic.add(new ArrayList<>());
    }
    for (int i = 0; i < numberOfAgents; i++) {
      for (int j = 0; j < numberOfAgents; j++) {
        successfulTransactionVectorElastic.get(i).add(new HashMap<>());
        unsuccessfulTransactionVectorElastic.get(i).add(new HashMap<>());
      }
    }

    return super.initialiseTrustModel(
        veryGoodAgents,
        goodAgents,
        okAgents,
        badAgents,
        veryBadAgents,
        trustedAgents,
        maliciousAgents,
        elasticAgents,
        realUserAgents,
        elasticAgentStartPersonality,
        elasticAgentEndPersonality,
        numberOfEvents,
        numberOfServiceTypes);
  }

  /**
   * Function updates a matrix array that stores the transaction history of successful and
   * unsuccessful transactions between agents in a simulation, based on the outcome of a new service
   * request object. Note that the time of each transaction is also stored, so that each transaction
   * outcome can be weighted based on the time since that transaction occurred when calculating
   * local and global trust values.
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
      if (request.isCompleted()) {
        successfulTransactionVectorElastic
            .get(request.getConsumer())
            .get(request.getProducer())
            .put(request.getRequestTime(), 1);
      } else {
        unsuccessfulTransactionVectorElastic
            .get(request.getConsumer())
            .get(request.getProducer())
            .put(request.getRequestTime(), 1);
      }

    } else if (type.equals("ContinuousServiceRequest")) {
      // makes a large change to the local trust values matrix
      if (request.isCompleted()) {
        successfulTransactionVectorElastic
            .get(request.getConsumer())
            .get(request.getProducer())
            .put(request.getRequestTime(), 10);
      } else {
        unsuccessfulTransactionVectorElastic
            .get(request.getConsumer())
            .get(request.getProducer())
            .put(request.getRequestTime(), 10);
      }

    } else {
      // makes small change to the local trust values matrix
      if (subTransactionComplete) {
        successfulTransactionVectorElastic
            .get(request.getConsumer())
            .get(request.getProducer())
            .put(request.getRequestTime(), 1);
      } else {
        unsuccessfulTransactionVectorElastic
            .get(request.getConsumer())
            .get(request.getProducer())
            .put(request.getRequestTime(), 1);
      }
    }
  }

  /**
   * Function updates a local trust values matrix that stores each agents local trust value in each
   * other agent in a simulation, based on the outcome of a new service request object. Note that
   * the time of each transaction is used to weight each transaction outcome based on the time since
   * that transaction occurred when calculating local trust values.
   *
   * @param request service request object
   */
  @Override
  public void updateLocalTrustValues(ServiceRequest request) {

    recalculateTransactionHistoryMatrix(request.getRequestTime());
    super.updateLocalTrustValues(request);
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
    recalculateTransactionHistoryMatrix(agentSystem.getNumberOfServiceRequests());
    super.saveToAgentSystem(agentSystem, simulationNumber);
  }

  /**
   * Function used to calculate the weighted values of a transaction history matrix, where the time
   * since each transaction (relative to the current simulation time) is used to weight each
   * transaction outcome based on a Logistic function.
   *
   * @param simulationTime simulation time that the transaction history matrix should be
   *     recalculated at
   */
  private void recalculateTransactionHistoryMatrix(int simulationTime) {

    for (int consumer = 0; consumer < numberOfAgents; consumer++) {
      for (int producer = 0; producer < numberOfAgents; producer++) {
        if (consumer == producer
            || consumer >= successfulTransactionVectorElastic.size()
            || producer >= successfulTransactionVectorElastic.get(consumer).size()) {
          continue;
        }

        double successfulTransactionsValue = 0.0;
        for (Map.Entry<Integer, Integer> transaction :
            successfulTransactionVectorElastic.get(consumer).get(producer).entrySet()) {
          double transactionTime = transaction.getKey();
          double logisticFunc =
              transaction.getValue()
                  * logisticFunction(0.9, logisticMidpoint, logisticDecayRate, transactionTime);
          double weightedTransactionTime = transactionTime * logisticFunc;
          successfulTransactionsValue += weightedTransactionTime;
        }
        successfulTransactionVector[consumer][producer] = successfulTransactionsValue;

        double unsuccessfulTransactionsValue = 0.0;
        for (Map.Entry<Integer, Integer> transaction :
            unsuccessfulTransactionVectorElastic.get(consumer).get(producer).entrySet()) {
          double transactionTime = transaction.getKey();
          double logisticFunc =
              transaction.getValue()
                  * logisticFunction(0.9, logisticMidpoint, logisticDecayRate, transactionTime);
          double weightedTransactionTime = transactionTime * logisticFunc;
          unsuccessfulTransactionsValue += weightedTransactionTime;
        }
        unsuccessfulTransactionVector[consumer][producer] = unsuccessfulTransactionsValue;
      }
    }
  }

  /**
   * Function calculates value of a logistic function described by the input parameters.
   *
   * @param maxValue maximum value of logistic function
   * @param x0 midpoint of logistic function
   * @param k decay rate of logistic function
   * @param x x value that logistic function is calculated at
   * @return y value of logistic function at the value of x
   */
  private double logisticFunction(double maxValue, double x0, double k, double x) {
    return 1 - (maxValue / (1 + Math.exp(-k * (x - x0))));
  }

  public double getDecayConst() {
    return logisticDecayRate;
  }

  public void setDecayConst(double decayConst) {
    this.logisticDecayRate = decayConst;
  }
}
