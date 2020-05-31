package com.trustsim.ingestor.controller;

import com.trustsim.TrustSimParentController;

public class IngestorController {

  private TrustSimParentController trustSimParentController;
  private static final String XMLFile = "com/trustsim/Ingestor.fxml";

  public void injectMainController(TrustSimParentController trustSimParentController) {
    this.trustSimParentController = trustSimParentController;
  }

  public static String getXMLFile() {
    return XMLFile;
  }
}
