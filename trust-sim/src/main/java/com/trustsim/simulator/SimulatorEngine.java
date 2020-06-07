package com.trustsim.simulator;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.dispatchers.ServiceDispatcher;
import com.trustsim.simulator.storage.XStreamManager;

public class SimulatorEngine {

  private int frequency;
  private XStreamManager xStreamManager;
  private Graph agentGraph;

  public void initialiseSimulatorEngine() {
    //xStreamManager = new XStreamManager();
  }

  public void loadSimulationData(String graphName) {
    //xStreamManager.load(graphName);
  }
  // AgentFactory agentFactory = new AgentFactory();
  ServiceDispatcher serviceDispatcher = new ServiceDispatcher(10);

  public void runSimulation() {


  }


}
