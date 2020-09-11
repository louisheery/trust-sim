package com.trustsim.simulator.simulationmanager;

/**
 * Class representing a Discrete Service Request event, that is a Service Request which is made up
 * of 1 Service Request. The completion status of a Discrete Service Request is dependent on the
 * completion status of this child Service Request object.
 */
public class DiscreteServiceRequest extends ServiceRequest {

  public DiscreteServiceRequest(Integer serviceType) {
    super(serviceType);
  }
}
