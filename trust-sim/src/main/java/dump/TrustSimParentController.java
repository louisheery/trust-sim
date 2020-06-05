package dump;

import com.trustsim.home.HomeController;
import com.trustsim.home.HomeEngine;
import com.trustsim.ingestor.IngestorController;
import com.trustsim.ingestor.IngestorEngine;
import com.trustsim.simulator.SimulatorController;
import com.trustsim.simulator.SimulatorEngine;
import com.trustsim.synthesiser.SynthesiserController;
import com.trustsim.synthesiser.SynthesiserEngine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class TrustSimParentController implements Initializable {

  @FXML Pane homePane, ingestorPane, simulatorPane, synthesiserPane, evaluatorPane;
  public Stage stage;

//
//  @FXML private HomeController homeController;
//  @FXML private IngestorController ingestorController;
//  @FXML private SimulatorController simulatorController;
//  @FXML private SynthesiserController synthesiserController;
//  private HomeEngine homeEngine;
//  private IngestorEngine ingestorEngine;
//  private SimulatorEngine simulatorEngine;
//  private SynthesiserEngine synthesiserEngine;
//  private Parent homeScreen;
//  private Parent ingestorScreen;
//  private Parent simulatorScreen;
//  private Parent synthesiserScreen;
//  private String screen;
//  private HashMap<String, Pane> screenMap = new HashMap<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  public void changeScene(String sceneName) throws IOException {

    switch (sceneName) {
      case "ingestor": {
        stage = (Stage) homePane.getScene().getWindow();
        ingestorPane = FXMLLoader.load(getClass().getResource("/com/trustsim/Ingestor.fxml"));
        Scene scene = new Scene(ingestorPane);
        stage.setScene(scene);
        stage.setTitle("Ingestor - TrustSim");
        stage.show();
        break;
      }
      case "simulator": {
        stage = (Stage) simulatorPane.getScene().getWindow();
        simulatorPane = FXMLLoader.load(getClass().getResource("/com/trustsim/Simulator.fxml"));
        Scene scene = new Scene(simulatorPane);
        stage.setScene(scene);
        stage.setTitle("Simulator - TrustSim");
        stage.show();
        break;
      }
      case "synthesiser": {
        stage = (Stage) synthesiserPane.getScene().getWindow();
        synthesiserPane = FXMLLoader.load(getClass().getResource("/com/trustsim/Synthesiser.fxml"));
        Scene scene = new Scene(synthesiserPane);
        stage.setScene(scene);
        stage.setTitle("Synthesiser - TrustSim");
        stage.show();
        break;
      }
      case "evaluator": {
        stage = (Stage) evaluatorPane.getScene().getWindow();
        evaluatorPane = FXMLLoader.load(getClass().getResource("/com/trustsim/Evaluator.fxml"));
        Scene scene = new Scene(evaluatorPane);
        stage.setScene(scene);
        stage.setTitle("Evaluator - TrustSim");
        stage.show();
        break;
      }
    }
  }


}