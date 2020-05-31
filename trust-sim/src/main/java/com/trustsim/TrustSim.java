package com.trustsim;

import com.trustsim.home.controller.HomeController;
import com.trustsim.home.model.HomeEngine;
import com.trustsim.home.view.HomeView;
import com.trustsim.ingestor.controller.IngestorController;
import com.trustsim.ingestor.model.IngestorEngine;
import com.trustsim.ingestor.view.IngestorView;
import com.trustsim.simulator.Simulator;
import com.trustsim.simulator.controller.SimulatorController;
import com.trustsim.simulator.model.SimulatorEngine;
import com.trustsim.simulator.view.SimulatorView;
import com.trustsim.synthesiser.controller.SynthesiserController;
import com.trustsim.synthesiser.model.SynthesiserEngine;
import com.trustsim.synthesiser.view.SynthesiserView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class TrustSim extends Application {

  private static TrustSim instance;
  private Stage primaryStage;
  private Parent root;
  private TrustSimParentController mainController;
  // private SimulationEngine model;
  // private SimulationController controller;
  // private TrustSimDisplay view;
  private HomeController homeController;
  private IngestorController ingestorController;
  private SimulatorController simulatorController;
  private SynthesiserController synthesiserController;
  private HomeEngine homeEngine;
  private IngestorEngine ingestorEngine;
  private SimulatorEngine simulatorEngine;
  private SynthesiserEngine synthesiserEngine;
  private HomeView homeView;
  private IngestorView ingestorView;
  private SimulatorView simulatorView;
  private SynthesiserView synthesiserView;
  private Scene homeScene;
  private Scene ingestorScene;
  private Scene simulatorScene;
  private Scene synthesiserScene;

  public TrustSim() {

    instance = this;
    // model = new SimulationEngine();
    // controller = new SimulationController(model);
    // view = new TrustSimGui(controller);

  }

  public static void main(String[] args) {
    launch(args);
  }

  public static TrustSim getInstance() {
    if (instance == null) {
      instance = new TrustSim();
    }
    return instance;
  }


  @Override
  public void start(Stage primaryStage) throws Exception {

    homeEngine = new HomeEngine();
    homeController = new HomeController();
    homeView = new HomeView(homeController, homeEngine);

    ingestorEngine = new IngestorEngine();
    ingestorController = new IngestorController();
    ingestorView = new IngestorView(ingestorController, ingestorEngine);

    simulatorEngine = new SimulatorEngine();
    simulatorController = new SimulatorController();
    simulatorView = new SimulatorView(simulatorController, simulatorEngine);

    synthesiserEngine = new SynthesiserEngine();
    synthesiserController = new SynthesiserController();
    synthesiserView = new SynthesiserView(synthesiserController, synthesiserEngine);


    //mainController = new TrustSimParentController();
    //mainController.addAllScreens();
    //mainController.setScreen("homeScreen");

    //Group root = new Group(mainController);
    homeScene = new Scene(homeView.returnParent(), 1000, 700);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.setTitle("TrustSim");
    primaryStage.setScene(homeScene);
    primaryStage.show();
  }

  private void setStylesheets() {
    //scene.getStylesheets().setAll();
  }

  public void setScene(String screenName) {

    switch(screenName) {
      case "homeScene":
        homeScene = new Scene(homeView.returnParent(), 1000, 700);
        break;
      case "ingestorScene":
        homeScene = new Scene(ingestorView.returnParent(), 1000, 700);
        break;
      case "simulatorScene":
        homeScene = new Scene(simulatorView.returnParent(), 1000, 700);
        break;
      case "synthesiserScene":
        homeScene = new Scene(synthesiserView.returnParent(), 1000, 700);
        break;
      default:
        break;
    }
  }


}
