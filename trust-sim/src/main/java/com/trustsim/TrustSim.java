package com.trustsim;

import com.trustsim.home.HomeController;
import com.trustsim.home.HomeEngine;
import com.trustsim.ingestor.IngestorController;
import com.trustsim.ingestor.IngestorEngine;
import com.trustsim.simulator.SimulatorController;
import com.trustsim.simulator.SimulatorEngine;
import com.trustsim.synthesiser.SynthesiserController;
import com.trustsim.synthesiser.SynthesiserEngine;
import dump.TrustSimParentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TrustSim extends Application {

  private static TrustSim instance;
  private Stage primaryStage;
  private Parent root;
  private TrustSimParentController mainController;
  private HomeController homeController;
  private IngestorController ingestorController;
  private SimulatorController simulatorController;
  private SynthesiserController synthesiserController;
  private HomeEngine homeEngine;
  private IngestorEngine ingestorEngine;
  private SimulatorEngine simulatorEngine;
  private SynthesiserEngine synthesiserEngine;

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

    Parent root = FXMLLoader.load(getClass().getResource("/com/trustsim/Home.fxml"));
    Scene scene = new Scene(root);

    primaryStage.setScene(scene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.setTitle("Home - TrustSim");
    primaryStage.show();

  }

}
