package com.trustsim.home.controller;


import com.trustsim.TrustSimParentController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

  private TrustSimParentController trustSimParentController;
  private static final String XMLFile = "com/trustsim/Home.fxml";

  public void injectMainController(TrustSimParentController trustSimParentController) {
    this.trustSimParentController = trustSimParentController;
  }

  public static String getXMLFile() {
    return XMLFile;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }
}
