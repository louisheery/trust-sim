package com.trustsim.simulator;

import com.trustsim.simulator.storage.SQLiteDatabaseManager;
import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.simulator.trustmodel.WangTrustModel;
import com.trustsim.synthesiser.AgentSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SimulatorController implements Initializable {

  private final SimulatorEngine simulatorEngine = new SimulatorEngine();

  public MenuItem toMenu;
  public Button startSimulationButton;
  public ComboBox<TrustModel> trustModelSelectorComboBox;
  public ComboBox<AgentSystem> agentSystemSelectorComboBox;
  @FXML Pane homePane, simulatorPane;
  public Stage stage;

  private ObservableList<AgentSystem> agentSystemData;
  private ObservableList<TrustModel> trustModelData;
  private final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
  private AgentSystem selectedAgentSystem;
  private TrustModel selectedTrustModel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeComboBox();
  }

  private void initializeComboBox() {
    agentSystemData = sqLiteDatabaseManager.retrieveSystemTableData("agentSystems");

    // HARD CODED VERSION OF TRUST MODELS
    List<TrustModel> trustModelList = new ArrayList<>();
    trustModelList.add(new WangTrustModel());
    trustModelData = FXCollections.observableList(trustModelList);
    // SQL DATABASE VERSION OF TRUST MODELS
    // trustModelData = sqLiteDatabaseManager.retrieveModelTableData("trustModels");

    agentSystemSelectorComboBox.getItems().clear();
    agentSystemSelectorComboBox.getItems().addAll(agentSystemData);


    trustModelSelectorComboBox.getItems().clear();
    trustModelSelectorComboBox.getItems().addAll(trustModelData);


    agentSystemSelectorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          selectedAgentSystem = newValue;
        }
    );

    trustModelSelectorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          selectedTrustModel = newValue;
        }
    );

    agentSystemSelectorComboBox.getSelectionModel().selectFirst();
    trustModelSelectorComboBox.getSelectionModel().selectFirst();
  }

  @FXML
  public void startSimulation(Event event) {
    System.out.println("H11");
    boolean simulationResult = simulatorEngine.startSimulation(selectedAgentSystem, selectedTrustModel);

    Alert alert = new Alert(Alert.AlertType.ERROR);
    if (simulationResult) {
      alert.setTitle("Simulation Completed Successfully");
      alert.setHeaderText("Simulation Results can be Analysed in the Evaluator");
    } else {
      alert.setTitle("Simulation Failed");
      alert.setHeaderText("Please see Stack Trace");
    }
    alert.showAndWait();
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
