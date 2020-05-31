package com.trustsim.synthesiser.controller;

import com.trustsim.TrustSimParentController;
import com.trustsim.home.controller.HomeController;

public class SynthesiserController extends HomeController {

  private TrustSimParentController trustSimParentController;
  private static final String XMLFile = "com/trustsim/Synthesiser.fxml";

  public void injectMainController(TrustSimParentController trustSimParentController) {
    this.trustSimParentController = trustSimParentController;
  }

  public static String getXMLFile() {
    return XMLFile;
  }
  
}
