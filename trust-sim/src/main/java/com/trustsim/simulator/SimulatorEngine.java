package com.trustsim.simulator;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.dispatchers.AgentGraphFactory;
import com.trustsim.simulator.storage.XStreamManager;
import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.synthesiser.AgentSystem;

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

  public boolean startSimulation(AgentSystem selectedAgentSystem, TrustModel selectedTrustModel) {

    AgentGraphFactory graphFactory = new AgentGraphFactory();
    agentGraph = graphFactory.createGraph(1,1,1,1,1,1,1,1,1,1,1)

    if (selectedTrustModel.getName().equals("FCTrust")) {
      // Then use the FCTrustModel Agent Classes
    } else {
      // Then use some other Agent Classes
    }

    return true;
  }
}
