package com.trustsim.simulator.simulationmanager;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a Continuous Service Request event, that is a Service Request which is made up
 * of multiple Service Requests, each with their own transaction time. The completion status of a
 * Continuous Service Request is dependent on the completion status of all of its child Service
 * Request objects.
 */
public class ContinuousServiceRequest extends ServiceRequest {

  private Map<Integer, ServiceRequest> events;
  private int unpaidPayments = 0;
  private final int transactionId;

  /**
   * Constructor of object, which initialises the properties of the Service Request, but does not
   * automatically generate its child service request events.
   *
   * @param serviceType type of the service requests
   * @param transactionId unique identifier of continuous service request
   * @param requestTime time which continuous service request is started in the simulation
   */
  public ContinuousServiceRequest(Integer serviceType, int transactionId, int requestTime) {
    super(serviceType);
    this.transactionId = transactionId;
    this.requestTime = requestTime;
  }

  /**
   * Function used to obtain the child service request object of this object that is carried out at
   * a particular transaction time during the simulation.
   *
   * @param transactionTime time during the simulation that this singular service request is carried
   *     out
   * @return service request object
   */
  public ServiceRequest getSingularServiceRequest(int transactionTime) {
    return events.get(transactionTime);
  }

  /**
   * Function populates this object with a collection of regularly spaced children service request
   * objects.
   *
   * @param lengthOfServiceRequest the total length that the continuous service request object
   *     exists for
   * @param spacingOfEvents the time interval between each child service request in the simulation
   */
  public void generateServiceRequestEvents(double lengthOfServiceRequest, double spacingOfEvents) {

    int numberOfEvents = (int) (lengthOfServiceRequest / spacingOfEvents);

    events = new HashMap<>();

    int time = requestTime;
    for (int i = 0; i < numberOfEvents; i++) {
      events.put(time, new ServiceRequest(serviceTrustCharacteristics, time));

      if (i + 1 == numberOfEvents) {
        completionTime = time;
      }
      time += spacingOfEvents;
    }
  }

  /**
   * Getter function to return whether this transaction has been successfully completed.
   *
   * @return whether or not transaction is completed.
   */
  public boolean isCompleted() {
    return isCompleted;
  }

  /**
   * Setter function used to specify the outcome of one of the child service request objects of this
   * object.
   *
   * @param transactionTime time at which the service request which is to be modified took place at
   * @param isCompleted whether or not this child service request was successfully completed
   */
  public void setEventTransactionOutcome(int transactionTime, boolean isCompleted) {
    if (events.containsKey(transactionTime)) {
      events.get(transactionTime).setTransactionOutcome(isCompleted);
    }

    if (transactionTime == this.completionTime) {
      this.setTransactionOutcome(isCompleted);
    }
  }

  /**
   * Function returns map of all child service requests of this object, with keyset corresponding to
   * the simulation times at which each of these service requests are to be carried out.
   *
   * @return map of service requests
   */
  public Map<Integer, ServiceRequest> getServiceRequestEvents() {
    return events;
  }

  public int getRequestTime() {
    return requestTime;
  }

  public int getCompletionTime() {
    return completionTime;
  }

  public void setContractStartTime(int startTime) {
    this.requestTime = startTime;
  }

  public void setContractEndTime(int endTime) {
    this.completionTime = endTime;
  }

  @Override
  public int compareTo(ServiceRequest o) {

    return this.transactionId - ((ContinuousServiceRequest) o).transactionId;
  }

  /**
   * Function increments the number of unpaid transactions within the Continuous Service Request
   * object.
   *
   * @return new value for number of unpaid transactions within the Continuous Service Request
   */
  public int addUnpaidPayment() {
    unpaidPayments++;
    return unpaidPayments;
  }

  /**
   * Function resets the number of unpaid transactions within the Continuous Service Request object
   * to zero.
   */
  public void clearUnpaidPayment() {
    unpaidPayments = 0;
  }
}
