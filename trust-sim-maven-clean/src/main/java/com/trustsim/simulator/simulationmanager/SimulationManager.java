package com.trustsim.simulator.simulationmanager;

import com.trustsim.synthesiser.AgentSystem;

/**
 * Simulation Manager interface which can be implemented by a simulation manager that runs a Trust
 * System Simulation.
 */
public interface SimulationManager {
  AgentSystem runSimulation(int simulationNumber);
}
