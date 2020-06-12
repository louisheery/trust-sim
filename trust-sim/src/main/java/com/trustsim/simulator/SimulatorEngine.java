package com.trustsim.simulator;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.agents.TrustVectorList;
import com.trustsim.simulator.events.SimulationEventManager;
import com.trustsim.simulator.events.WangSimulationEventManager;
import com.trustsim.simulator.storage.XStreamManager;
import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.synthesiser.AgentGraphFactory;
import com.trustsim.synthesiser.AgentSystem;
import com.trustsim.synthesiser.TransactionalVectorList;
import com.trustsim.synthesiser.WangAgentGraphFactory;

public class SimulatorEngine {

  private int frequency;
  private XStreamManager xStreamManager;
  private Graph<TrustVectorList, TransactionalVectorList> agentGraph;

  public void initialiseSimulatorEngine() {
    //xStreamManager = new XStreamManager();
  }

  public void loadSimulationData(String graphName) {
    //xStreamManager.load(graphName);
  }

  private AgentGraphFactory graphFactory;
  private SimulationEventManager serviceDispatcher;

  public boolean startSimulation(AgentSystem selectedAgentSystem, TrustModel selectedTrustModel) {

    switch(selectedTrustModel.getName()) {
      case "WANG":
        graphFactory = new WangAgentGraphFactory();
        break;
      default:
        graphFactory = new WangAgentGraphFactory();
        break;
    }

    agentGraph = graphFactory.createGraph(10, 10);
    graphFactory.initialiseGraphTrust(agentGraph);


    serviceDispatcher = new WangSimulationEventManager(10,1000, agentGraph);

    return true;
  }
}
