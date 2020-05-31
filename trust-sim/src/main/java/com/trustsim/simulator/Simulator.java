package com.trustsim.simulator;

public class Simulator {

  public static Graph agents;

  public static void main(String[] args) {

    // 1. Import Agents from Database into Graph
    importAgents();

    // 2. Run Simulation
    runSimulation();

  }

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
