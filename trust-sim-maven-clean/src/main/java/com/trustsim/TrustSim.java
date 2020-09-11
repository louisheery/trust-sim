package com.trustsim;

import com.trustsim.storage.SQLiteDatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;

/** JavaFX Application Main Class, which instantiates the TrustSim application. */
public class TrustSim extends Application {

  protected TrustSimSceneController sceneController;
  private final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();

  public TrustSim() {}

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    sceneController = TrustSimSceneController.getInstance(primaryStage);
    sceneController.changeScene("Home");

    sqLiteDatabaseManager.addTable("agentSystems");
    sqLiteDatabaseManager.addTable("trustModels");
    sqLiteDatabaseManager.addTable("simulationResults");
    sqLiteDatabaseManager.addTable("agentSystemsCalculator");
  }
}
