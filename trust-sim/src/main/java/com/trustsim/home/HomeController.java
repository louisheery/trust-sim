package com.trustsim.home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

  @FXML Pane homePane;
  @FXML Pane ingestorPane;
  @FXML Pane simulatorPane;
  @FXML Pane synthesiserPane;
  @FXML Pane evaluatorPane;
  @FXML private Button ingestorButton;
  @FXML private Button synthesiserButton;
  @FXML private Button simulatorButton;
  @FXML private Button evaluatorButton;

  public Stage stage;


  @FXML
  private void onMouseClicked(MouseEvent event) throws IOException {

    Object eventSource = event.getSource();

    if (eventSource == ingestorButton) {
      stage = (Stage) homePane.getScene().getWindow();
      ingestorPane = FXMLLoader.load(getClass().getResource("/com/trustsim/Simulator.fxml"));
      Scene scene = new Scene(ingestorPane);
      stage.setScene(scene);
      stage.setTitle("Ingestor - TrustSim");
      stage.show();
    } else if (eventSource == simulatorButton) {
      stage = (Stage) homePane.getScene().getWindow();
      simulatorPane = FXMLLoader.load(getClass().getResource("/com/trustsim/Simulator.fxml"));
      Scene scene = new Scene(simulatorPane);
      stage.setScene(scene);
      stage.setTitle("Simulator - TrustSim");
      stage.show();
    } else if (eventSource == synthesiserButton) {
      stage = (Stage) homePane.getScene().getWindow();
      synthesiserPane = FXMLLoader.load(getClass().getResource("/com/trustsim/Synthesiser.fxml"));
      Scene scene = new Scene(synthesiserPane);
      stage.setScene(scene);
      stage.setTitle("Synthesiser - TrustSim");
      stage.show();
    } else if (eventSource == evaluatorButton) {
      stage = (Stage) homePane.getScene().getWindow();
      evaluatorPane = FXMLLoader.load(getClass().getResource("/com/trustsim/Evaluator.fxml"));
      Scene scene = new Scene(evaluatorPane);
      stage.setScene(scene);
      stage.setTitle("Evaluator - TrustSim");
      stage.show();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

}
