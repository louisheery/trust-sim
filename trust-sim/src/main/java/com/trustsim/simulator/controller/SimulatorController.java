package com.trustsim.simulator.controller;

import com.trustsim.TrustSimParentController;

public class SimulatorController {

  private TrustSimParentController trustSimParentController;
  private static final String XMLFile = "com/trustsim/Simulator.fxml";

  public void injectMainController(TrustSimParentController trustSimParentController) {
    this.trustSimParentController = trustSimParentController;
  }

  public static String getXMLFile() {
    return XMLFile;
  }
}
