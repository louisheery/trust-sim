package com.trustsim;

import com.trustsim.home.controller.HomeController;
import com.trustsim.home.model.HomeEngine;
import com.trustsim.home.view.HomeView;
import com.trustsim.ingestor.controller.IngestorController;
import com.trustsim.ingestor.model.IngestorEngine;
import com.trustsim.simulator.controller.SimulatorController;
import com.trustsim.simulator.model.SimulatorEngine;
import com.trustsim.synthesiser.controller.SynthesiserController;
import com.trustsim.synthesiser.model.SynthesiserEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class TrustSimParentController { //extends BorderPane {
/*
  //private Map<String, Node> screensMap = new HashMap<>();
  //private Map<String, ModuleLayer.Controller> controllersMap = new HashMap<>();
  private HomeController homeController;
  private IngestorController ingestorController;
  private SimulatorController simulatorController;
  private SynthesiserController synthesiserController;
  private HomeEngine homeEngine;
  private IngestorEngine ingestorEngine;
  private SimulatorEngine simulatorEngine;
  private SynthesiserEngine synthesiserEngine;
  private Parent homeScreen;
  private Parent ingestorScreen;
  private Parent simulatorScreen;
  private Parent synthesiserScreen;
  private String screen;


  public void setScreen(String screenName) {

    screen = screenName;
    if (!getChildren().isEmpty()) {
      getChildren().remove(0);
    }

    switch(screenName) {
      case "homeScreen":
        getChildren().add(0,homeScreen);
        break;
      case "ingestorScreen":
        getChildren().add(ingestorScreen);
        break;
      case "simulatorScreen":
        getChildren().add(simulatorScreen);
        break;
      case "synthesiserScreen":
        getChildren().add(synthesiserScreen);
        break;
      default:
        break;
    }
  }

  public void addAllScreens() {

    FXMLLoader homeFXML;
    homeScreen = null;
    homeEngine = new HomeEngine();

      //homeFXML = new FXMLLoader(getClass().getResource("/com/trustsim/Home.fxml"));
      //homeController = homeFXML.getController();
    homeController = new HomeController();
    //homeController.injectMainController(this);
      //homeScreen = homeFXML.load();
      HomeView homeView = new HomeView(homeController, homeEngine);
      homeScreen = homeView.returnParent();




    FXMLLoader ingestorFXML;
    ingestorScreen = null;
    try {
      ingestorFXML = new FXMLLoader(getClass().getResource("/com/trustsim/Ingestor.fxml"));
      ingestorScreen = ingestorFXML.load();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    ingestorController = ingestorFXML.getController();
    ingestorController.injectMainController(this);

    FXMLLoader simulatorFXML;
    simulatorScreen = null;
    try {
      simulatorFXML = new FXMLLoader(getClass().getResource("/com/trustsim/Simulator.fxml"));
      simulatorScreen = simulatorFXML.load();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    simulatorController = simulatorFXML.getController();
    simulatorController.injectMainController(this);

    FXMLLoader synthesiserFXML;
    synthesiserScreen = null;
    try {
      synthesiserFXML = new FXMLLoader(getClass().getResource("/com/trustsim/Synthesiser.fxml"));
      synthesiserScreen = synthesiserFXML.load();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    synthesiserController = synthesiserFXML.getController();
    synthesiserController.injectMainController(this);

  }
*/
}
