package com.trustsim.simulator;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.events.SimulationEventManager;
import com.trustsim.simulator.events.WangSimulationEventManager;
import com.trustsim.simulator.storage.XStreamManager;
import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.synthesiser.AgentGraphFactory;
import com.trustsim.synthesiser.AgentSystem;
import com.trustsim.synthesiser.WangAgentGraphFactory;

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

  private AgentGraphFactory graphFactory;
  private SimulationEventManager simulationEventManager;

  public boolean startSimulation(AgentSystem selectedAgentSystem, TrustModel selectedTrustModel) {

    switch(selectedTrustModel.getName()) {
//      case "WANG":
//        graphFactory = new WangAgentGraphFactory();
//        simulationEventManager = new WangSimulationEventManager(10, 1000, agentGraph);
//        break;
      default:
        graphFactory = new WangAgentGraphFactory();
        System.out.println("HI");
        agentGraph = graphFactory.createGraph(selectedAgentSystem.getNumberOfConsumersParameter(), selectedAgentSystem.getNumberOfProducersParameter());
        graphFactory.initialiseGraphTrust(agentGraph);
        simulationEventManager = new WangSimulationEventManager(selectedAgentSystem.getNumberOfServiceRequests(), agentGraph);
        break;
    }

    simulationEventManager.startSim();

    return true;
  }


  public void stopSimulation() {
    simulationEventManager.stopEarly();
  }

}
