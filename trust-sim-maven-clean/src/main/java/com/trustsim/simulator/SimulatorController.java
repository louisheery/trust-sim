package com.trustsim.simulator;

import static com.trustsim.UtilityFunction.checkValueOrReplace;

import com.trustsim.TrustSimSceneController;
import com.trustsim.simulator.simulationmanager.ContinuousSimulationManager;
import com.trustsim.simulator.simulationmanager.DiscreteSimulationManager;
import com.trustsim.simulator.simulationmanager.SimulationManager;
import com.trustsim.simulator.trustmodel.DynamicTrustModel;
import com.trustsim.simulator.trustmodel.EigenTrustModel;
import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.simulator.trustmodel.TrustModelEnum;
import com.trustsim.storage.SQLiteDatabaseManager;
import com.trustsim.synthesiser.AgentSystem;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class is Controller of the Simulator Scene of application.
 */
@SuppressWarnings("AccessStaticViaInstance")
public class SimulatorController implements Initializable {

  @FXML private TextField epsilonConstantElasticInput;
  @FXML private Button alphaElasticHelpButton;
  @FXML private TextField alphaConstantElasticInput;
  @FXML private Button epsilonElasticHelpButton;
  @FXML private TextField decayConstantElasticInput;
  @FXML private Button decayElasticHelpButton;
  @FXML private ImageView trustSimLogo;
  @FXML private TextField numberOfSimulationRepeatsTextField;
  @FXML private ComboBox<AgentSystem> agentSystemSelectorComboBox;
  @FXML private ComboBox<String> outputFormatComboBox;
  @FXML private HBox trustSimLogoHBox;
  @FXML private MenuItem toMenu;
  @FXML private MenuItem toAbout;
  @FXML private Button epsilonHelpButton;
  @FXML private Button alphaHelpButton;
  @FXML private Button eventTypeHelpButton;
  @FXML private Button numberServiceRequestHelpButton;
  @FXML private Button updateIntervalHelpButton;
  @FXML private Button simulationReplicationsHelpButton;
  @FXML private Button simulationOutputHelpButton;
  @FXML private Button agentSystemNameHelpButton;
  @FXML private TextField epsilonConstantInput;
  @FXML private TextField alphaConstantInput;
  @FXML private TextField numberOfServiceRequests;
  @FXML private TextField trustUpdateIntervalInput;
  @FXML private ComboBox<String> simulationEventTypeComboBox;
  @FXML private AnchorPane anchorPane2;
  @FXML private AnchorPane anchorPane3;
  @FXML private AnchorPane anchorPane4;
  @FXML private GridPane gridPane2;
  @FXML private GridPane gridPane3;
  @FXML private GridPane gridPane4;
  @FXML private Stage stage;

  private final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
  private final Map<String, TrustModel> trustModels = new HashMap<>();
  private AgentSystem selectedAgentSystem = null;

  public SimulatorController() {
  }

  /**
   * Function initialises controller for Simulator screen, by loading dataset from database
   * and initialising combo boxes.
   *
   * @param location specifies fxml file location
   * @param resources specifies resource bundle
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    anchorPane2.setBottomAnchor(gridPane2, .0);
    anchorPane2.setTopAnchor(gridPane2, .0);
    anchorPane2.setLeftAnchor(gridPane2, .0);
    anchorPane2.setRightAnchor(gridPane2, .0);
    anchorPane3.setBottomAnchor(gridPane3, .0);
    anchorPane3.setTopAnchor(gridPane3, .0);
    anchorPane3.setLeftAnchor(gridPane3, .0);
    anchorPane3.setRightAnchor(gridPane3, .0);
    anchorPane4.setBottomAnchor(gridPane4, .0);
    anchorPane4.setTopAnchor(gridPane4, .0);
    anchorPane4.setLeftAnchor(gridPane4, .0);
    anchorPane4.setRightAnchor(gridPane4, .0);


    trustSimLogoHBox.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    Image trustSimLogo = new Image(getClass().getResourceAsStream("/images/logo/trustsimlogo.png"));
    this.trustSimLogo.setImage(trustSimLogo);

    initialiseTrustModelObjects();
    initialiseSimulationEventTypeComboBox();
    initialiseOutputFormatComboBox();
    initialiseSimulationParameters();
  }

  /**
   * Function retrieves expected trust model objects from SQLite database, and if they are not
   * present, then they are added initialised with their default parameters and then added to the
   * Trust Model table of the database.
   */
  private void initialiseTrustModelObjects() {

    EigenTrustModel eigenTrustModel = new EigenTrustModel();
    eigenTrustModel.setAlphaConst(0.5);
    eigenTrustModel.setEpsilonConst(0.001);

    DynamicTrustModel dynamicTrustModel = new DynamicTrustModel(0.0025, 1000, 0, 0.5, 0.001);
    dynamicTrustModel.setAlphaConst(0.5);
    dynamicTrustModel.setEpsilonConst(0.001);

    trustModels.put("EigenTrustModel", eigenTrustModel);
    trustModels.put("DynamicTrustModel", dynamicTrustModel);
  }


  /**
   * Function initialises contents of the combo box used to specify the output format of the
   * results obtained from the simulation(s).
   */
  private void initialiseOutputFormatComboBox() {
    ObservableList<AgentSystem> agentSystemData = sqLiteDatabaseManager
        .retrieveAllSystemObjectsTableData("agentSystems");

    List<String> outputFormatList = new ArrayList<>();
    outputFormatList.add("Visualisation & JSON");
    ObservableList<String> outputFormatData = FXCollections.observableList(outputFormatList);

    outputFormatComboBox.getItems().clear();
    outputFormatComboBox.getItems().addAll(outputFormatData);
    outputFormatComboBox.getSelectionModel().selectFirst();

    agentSystemSelectorComboBox.getItems().clear();
    agentSystemSelectorComboBox.getItems().addAll(agentSystemData);
    agentSystemSelectorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          selectedAgentSystem = newValue;
        }
    );
    agentSystemSelectorComboBox.getSelectionModel().selectFirst();

    numberOfSimulationRepeatsTextField.setText(String.valueOf(5));
  }

  /**
   * Function used to start the simulation, by instantiating the applicable SimulationManager
   * object and calling that objects run simulation method.
   * @param event stores data related to caller of event
   */
  @FXML
  public void startSimulation(Event event) {

    // Stops simulation from running if no agent system is selected
    if (selectedAgentSystem == null) {
      return;
    }

    try {
      SimulationManager simulationManager;
      switch (outputFormatComboBox.getSelectionModel().getSelectedItem()) {
        case "Visualisation & JSON" -> {

          // Update Trust Model Parameters
          ((EigenTrustModel) trustModels.get("EigenTrustModel")).setAlphaConst(checkValueOrReplace(Double.parseDouble(alphaConstantInput.getText()), 0, 1.0, 0.5));
          ((EigenTrustModel) trustModels.get("EigenTrustModel")).setEpsilonConst(checkValueOrReplace(Double.parseDouble(epsilonConstantInput.getText()), 0.0000001, 1.0, 0.001));

          ((DynamicTrustModel) trustModels.get("DynamicTrustModel")).setAlphaConst(checkValueOrReplace(Double.parseDouble(alphaConstantElasticInput.getText()), 0, 1.0, 0.5));
          ((DynamicTrustModel) trustModels.get("DynamicTrustModel")).setEpsilonConst(checkValueOrReplace(Double.parseDouble(epsilonConstantElasticInput.getText()), 0.0000001, 1.0, 0.001));
          ((DynamicTrustModel) trustModels.get("DynamicTrustModel")).setDecayConst(checkValueOrReplace(Double.parseDouble(decayConstantElasticInput.getText()), -100, 100, 0.0025));


          String originalName = selectedAgentSystem.getSystemName();
          String timeStampString = (new Timestamp((new Date()).getTime())).toString();

          for (String trustModel : trustModels.keySet()) {

            AgentSystem resettedSystem = selectedAgentSystem;
            resettedSystem.setSystemName(originalName);
            resettedSystem.resetAgentSystem(true, true, true);

            if (simulationEventTypeComboBox.getSelectionModel().getSelectedItem()
                .equals("Discrete Simulation Events")) {

              simulationManager = new DiscreteSimulationManager(resettedSystem, trustModels.get(trustModel),
                  checkValueOrReplace(Integer.parseInt(numberOfServiceRequests.getText()), 10, 50000, 1000),
                  checkValueOrReplace(1, 1, 5, 1),
                  checkValueOrReplace(Integer.parseInt(trustUpdateIntervalInput.getText()), 1, 50000, 10));
            } else {
              simulationManager = new ContinuousSimulationManager(resettedSystem, trustModels.get(trustModel),
                  checkValueOrReplace(Integer.parseInt(numberOfServiceRequests.getText()),10, 50000, 1000),
                  checkValueOrReplace(1, 1, 5, 1),
              checkValueOrReplace(Integer.parseInt(trustUpdateIntervalInput.getText()), 1, 50000, 10));
            }
            AgentSystem agentSystemResult = simulationManager.runSimulation(0);


            for (int i = 1; i < checkValueOrReplace(Integer.parseInt(numberOfSimulationRepeatsTextField.getText()), 1, 100, 5); i++) {
              if (simulationEventTypeComboBox.getSelectionModel().getSelectedItem()
                  .equals("Discrete Simulation Events")) {
                simulationManager = new DiscreteSimulationManager(agentSystemResult, trustModels.get(trustModel),
                    checkValueOrReplace(Integer.parseInt(numberOfServiceRequests.getText()), 10, 50000, 1000),
                        checkValueOrReplace(1, 1, 5, 1),
                            checkValueOrReplace(Integer.parseInt(trustUpdateIntervalInput.getText()), 1, 50000, 10));
              } else {
                simulationManager = new ContinuousSimulationManager(agentSystemResult, trustModels.get(trustModel),
                    checkValueOrReplace(Integer.parseInt(numberOfServiceRequests.getText()), 10, 50000, 1000),
                        checkValueOrReplace(1, 1, 5, 1),
                            checkValueOrReplace(Integer.parseInt(trustUpdateIntervalInput.getText()), 1, 50000, 10));
              }
              agentSystemResult = simulationManager.runSimulation(i);
            }


            if (trustModel.equals("EigenTrustModel")) agentSystemResult.setTrustModel(TrustModelEnum.EigenTrustModel);
            if (trustModel.equals("DynamicTrustModel")) agentSystemResult.setTrustModel(TrustModelEnum.DynamicTrustModel);

            agentSystemResult.setSystemName(agentSystemResult.getSystemName() + " " + timeStampString);

            sqLiteDatabaseManager.addObjectToTable("simulationResults", agentSystemResult.getSystemName() + agentSystemResult.getTrustModel().toString(), agentSystemResult);

          }


        }
      }

      Alert alert = new Alert(AlertType.INFORMATION);
      if (true) {
        alert.setTitle("Simulation Completed Successfully");
        alert.setHeaderText("Simulation Results can be Analysed in the Evaluator");
      } else {
        alert.setTitle("Simulation Failed");
        alert.setHeaderText("Please see Stack Trace");
      }
      alert.showAndWait();
    } catch (IllegalArgumentException e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("IllegalArgumentException: Cassandra Database is not setup properly on 127.0.0.1:9200");
      alert.setHeaderText("Please start up Cassandra & ElasticSearch ");
      alert.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  /**
   * Function initialises the parameters of the simulation based on the values inputted by the
   * user in the JavaFX text fields.
   */
  protected void initialiseSimulationParameters() {

    // EigenTrustModel
    epsilonConstantInput.setText(String.valueOf(((EigenTrustModel) trustModels.get("EigenTrustModel")).getEpsilonConst()));
    alphaConstantInput.setText(String.valueOf(((EigenTrustModel) trustModels.get("EigenTrustModel")).getAlphaConst()));

    // DynamicTrustModel
    epsilonConstantElasticInput.setText(String.valueOf(((EigenTrustModel) trustModels.get("DynamicTrustModel")).getEpsilonConst()));
    alphaConstantElasticInput.setText(String.valueOf(((EigenTrustModel) trustModels.get("DynamicTrustModel")).getAlphaConst()));
    decayConstantElasticInput.setText(String.valueOf(((DynamicTrustModel) trustModels.get("DynamicTrustModel")).getDecayConst()));


    // Simulation Parameters
    numberOfServiceRequests.setText(
        String.valueOf(1000));
    trustUpdateIntervalInput.setText(String.valueOf(10));

    initialiseSimulationEventTypeComboBox();
    initialiseOutputFormatComboBox();
  }

  /**
   * Function initialises the contents of the combo box used to specify the type of simulation
   * events of the simulation(s) to be run.
   */
  private void initialiseSimulationEventTypeComboBox() {

    List<String> simulationTypeList = new ArrayList<>();
    simulationTypeList.add("Discrete Simulation Events");
    simulationTypeList.add("Continuous Simulation Events");
    ObservableList<String> simulationTypeData = FXCollections.observableList(simulationTypeList);

    simulationEventTypeComboBox.getItems().clear();
    simulationEventTypeComboBox.getItems().addAll(simulationTypeData);
    simulationEventTypeComboBox.getSelectionModel().selectFirst();
  }

  /**
   * Function used as event dispatcher for scene's buttons, that for displaying tooltip information
   * objects.
   * @param event stores data related to caller of event
   */
  public void helpOnClicked(ActionEvent event) {

    Object eventSource = event.getSource();

    Point2D p = ((Button) event.getSource()).localToScene(20.0, 20.0);
    final Tooltip helpTooltip = new Tooltip();
    helpTooltip.setShowDelay(new Duration(0.1));

    if (eventSource.equals(epsilonHelpButton) || eventSource.equals(epsilonElasticHelpButton)) {
      helpTooltip.setText("Convergence limit of model (Allowed Value: 0.0000001 to 1.0)");
    } else if (eventSource.equals(alphaHelpButton) || eventSource.equals(alphaElasticHelpButton)) {
      helpTooltip.setText("Weighting of Trusted Agents in calculating local trust values (Allowed Value: 0.0 to 1.0)");
    } else if (eventSource.equals(decayElasticHelpButton)) {
      helpTooltip.setText("Trust attenuation decay rate in weighting transaction events (Allowed Value: -100 to 100)");
    } else if (eventSource.equals(eventTypeHelpButton)) {
      helpTooltip.setText("Type of Simulation Trust Events");
    } else if (eventSource.equals(numberServiceRequestHelpButton)) {
      helpTooltip.setText("Number of Trust Events in a Simulation (Allowed Value: 1 to 50000)");
    } else if (eventSource.equals(updateIntervalHelpButton)) {
      helpTooltip.setText("Number of Trust Events between each Trust Value Recalculation (Allowed Value: 1 to 50000)");
    } else if (eventSource.equals(simulationReplicationsHelpButton)) {
      helpTooltip.setText("Number of Repeats of Simulation (Allowed Value: 1 to 100)");
    } else if (eventSource.equals(simulationOutputHelpButton)) {
      helpTooltip.setText("Output format of Simulation Results");
    } else if (eventSource.equals(agentSystemNameHelpButton)) {
      helpTooltip.setText("Agent System used for Simulation");
    }

    ((Button) event.getSource()).setTooltip(helpTooltip);
    helpTooltip.setAutoHide(true);
    helpTooltip.show(stage, p.getX()
        + ((Button) event.getSource()).getScene().getX() + ((Button) event.getSource()).getScene().getWindow().getX(), p.getY()
        + ((Button) event.getSource()).getScene().getY() + ((Button) event.getSource()).getScene().getWindow().getY());

  }

  /**
   * Function used externally to set the stage object that this evaluator controller's view
   * is displayed within.
   *
   * @param stage JavaFX stage object
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * Function used to call on the TrustSim scene controller class to change the currently displayed
   * scene.
   * @param event stores data related to caller of event
   */
  @FXML
  public void changeScene(Event event) {

    Object eventSource = event.getSource();

    TrustSimSceneController sceneController = TrustSimSceneController.getInstance(null);
    try {
      if (eventSource.equals(toMenu)) {
        sceneController.changeScene("Home");
      } else if (eventSource.equals(toAbout)) {
        sceneController.changeScene("About");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
