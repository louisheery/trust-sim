package com.trustsim.simulator.events;

import com.trustsim.simulator.agents.ServiceRequest;

public interface SimulationEventManager {
  void startSim();

  ServiceRequest generateRandomServiceRequest();

  void stopEarly();

//  void addToQueue(ServiceRequest serviceRequestsAtCurrentTimestep);
//
//  ServiceRequest retrieveFromQueue();
}
