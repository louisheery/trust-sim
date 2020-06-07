package com.trustsim.simulator;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SimulatorController implements Initializable {

  public MenuItem toMenu;
  @FXML Pane homePane, simulatorPane;
  public Stage stage;


  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  @FXML
  public void toMainMenu(Event event) throws IOException {
    stage = (Stage) simulatorPane.getScene().getWindow();
    homePane = FXMLLoader.load(getClass().getResource("/Home.fxml"));
    Scene scene = new Scene(homePane);
    stage.setScene(scene);
    stage.setTitle("Home - TrustSim");
    stage.show();
  }

  

}
