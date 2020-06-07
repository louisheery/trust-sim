package com.trustsim.evaluator;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EvaluatorController implements Initializable {

  @FXML Pane homePane, evaluatorPane;
  public Stage stage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  @FXML
  public void toMainMenu(Event event) throws IOException {
    stage = (Stage) evaluatorPane.getScene().getWindow();
    homePane = FXMLLoader.load(getClass().getResource("/Home.fxml"));
    Scene scene = new Scene(homePane);
    stage.setScene(scene);
    stage.setTitle("Home - TrustSim");
    stage.show();
  }
}
