package com.trustsim.simulator;

import com.trustsim.simulator.subclasses.Graph;

public class Simulator {

  public static Graph agents;

  /**
   * Imports Agents stored in Agent Database store, and puts them in agentsGraph
   */
  private static void importAgents() {
    importDummyAgents();
  }

  /**
   * Initialises a Dummy agentsGraph for testing
   */
  private static void importDummyAgents() {
      agents = new Graph();

  }

  /**
   * Runs the simulation
   */
  private static void runSimulation() {
  }
}
