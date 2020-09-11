package com.trustsim.importer;

import static com.trustsim.UtilityFunction.checkValueOrReplace;

import com.trustsim.TrustSimSceneController;
import com.trustsim.evaluator.AgentTypeWrapper;
import com.trustsim.simulator.agent.Agent;
import com.trustsim.simulator.agent.AgentType;
import com.trustsim.simulator.simulationmanager.CalculationSimulationManager;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import com.trustsim.simulator.simulationmanager.SimulationManager;
import com.trustsim.simulator.trustmodel.EigenTrustModel;
import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.simulator.trustmodel.TrustModelEnum;
import com.trustsim.storage.JanusGraphManager;
import com.trustsim.storage.SQLiteDatabaseManager;
import com.trustsim.synthesiser.AgentSystem;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.janusgraph.core.JanusGraph;

/** Class is Controller of the Importer Scene of application. */
public class ImporterController implements Initializable {

  @FXML private MenuItem toMenu;
  @FXML private MenuItem toAbout;
  @FXML private HBox trustSimLogoHBox;
  @FXML private TextField cassandraHostNameTextField;
  @FXML private TextField cassandraPortTextField;
  @FXML private TextField elasticsearchHostNameTextField;
  @FXML private TextField elasticsearchPortTextField;
  @FXML private ImageView trustSimLogoView;
  @FXML private Button epsilonHelpButton;
  @FXML private Button alphaHelpButton;
  @FXML private TextField epsilonConstantInput;
  @FXML private TextField alphaConstantInput;
  @FXML private Button cassandraHostnameHelpButton;
  @FXML private Button cassandraPortHelpButton;
  @FXML private Button elasticSearchHostnameHelpButton;
  @FXML private Button elasticSearchPortHelpButton;
  @FXML private Button initialiseGraphHelpButton;
  @FXML private Button importGraphHelpButton;

  private Stage stage;

  private final Map<String, TrustModel> trustModels = new HashMap<>();
  private AgentSystem agentSystem;
  private final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
  private JanusGraphManager janusGraphManager;
  private int maxServiceRequestTime;
  private HashMap<Integer, Agent> agentHashMap;
  private double[] agentPreTrustedVector;

  /**
   * Function initialises controller for Ingestor screen.
   *
   * @param location specifies fxml file location
   * @param resources specifies resource bundle
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    trustSimLogoHBox.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    Image trustSimLogo = new Image(getClass().getResourceAsStream("/images/logo/trustsimlogo.png"));
    trustSimLogoView.setImage(trustSimLogo);

    initialiseSimulationParameters();
  }

  /**
   * Function used to initialise a JanusGraph NoSQL Database with an example Graph Database Schema
   * and Agent & Agent Transactional History dataset.
   *
   * @param event stores data related to caller of event
   */
  @FXML
  public void initialiseJanusGraphSchema(Event event) {

    try {
      janusGraphManager = JanusGraphManager.getInstance();
      janusGraphManager.setCassandraElasticSearchServerAndInitialise(
          cassandraHostNameTextField.getText(),
          checkValueOrReplace(Integer.parseInt(cassandraPortTextField.getText()), 0, 99999, 9042),
          elasticsearchHostNameTextField.getText(),
          checkValueOrReplace(
              Integer.parseInt(elasticsearchPortTextField.getText()), 0, 99999, 9200));

      final JanusGraph graph = janusGraphManager.getGraph();
      graph.openManagement();
      janusGraphManager.deleteGraphItems(true, true);

      janusGraphManager.addAgents(6, 0, 0);
      List<Integer> allConsumerAgentId =
          janusGraphManager.getAgentIdsWithProperty("isConsumer", true);
      List<Integer> allProducerAgentId =
          janusGraphManager.getAgentIdsWithProperty("isProducer", true);
      int consumerAgentId1 = allConsumerAgentId.get(0);
      int consumerAgentId2 = allConsumerAgentId.get(1);
      int consumerAgentId3 = allConsumerAgentId.get(2);
      int consumerAgentId4 = allConsumerAgentId.get(3);
      int consumerAgentId5 = allConsumerAgentId.get(4);
      int consumerAgentId6 = allConsumerAgentId.get(5);

      // Set Agent Types
      janusGraphManager.modifyAgentProperty(consumerAgentId1, "isTrustedAgent", true, false);
      janusGraphManager.modifyAgentProperty(consumerAgentId5, "isTrustedAgent", true, false);

      janusGraphManager.modifyAgentProperty(consumerAgentId2, "isTrustedAgent", false, false);
      janusGraphManager.modifyAgentProperty(consumerAgentId3, "isTrustedAgent", false, false);
      janusGraphManager.modifyAgentProperty(consumerAgentId4, "isTrustedAgent", false, false);
      janusGraphManager.modifyAgentProperty(consumerAgentId5, "isTrustedAgent", false, false);

      // Transactions 1
      HashMap<Double, Boolean> newTransactions = new HashMap<>();
      for (double i = 1.0; i <= 1000.0; i++) {
        boolean isComplete = new Random().nextInt(100) < 90;
        newTransactions.put(i, isComplete);
      }

      // Transactions 2
      HashMap<Double, Boolean> newTransactions2 = new HashMap<>();
      newTransactions.put(1.0, false);
      newTransactions.put(2.0, true);
      newTransactions.put(3.0, false);
      newTransactions.put(4.0, true);
      for (double i = 1.0; i <= 500.0; i++) {
        boolean isComplete = new Random().nextInt(100) < 20;
        newTransactions.put(i, isComplete);
      }
      for (double i = 501; i <= 1000.0; i++) {
        boolean isComplete = new Random().nextInt(100) < 50;
        newTransactions.put(i, isComplete);
      }

      // Transactions 3
      HashMap<Double, Boolean> newTransactions3 = new HashMap<>();
      newTransactions.put(1.0, false);
      newTransactions.put(2.0, true);
      newTransactions.put(3.0, false);
      newTransactions.put(4.0, true);
      for (double i = 1.0; i <= 1000.0; i++) {
        boolean isComplete = new Random().nextInt(100) < 50;
        newTransactions.put(i, isComplete);
      }

      // Transactions 4
      HashMap<Double, Boolean> newTransactions4 = new HashMap<>();
      newTransactions.put(1.0, true);
      newTransactions.put(2.0, false);
      newTransactions.put(3.0, false);
      newTransactions.put(4.0, true);
      for (double i = 1.0; i <= 500.0; i++) {
        boolean isComplete = new Random().nextInt(100) < 80;
        newTransactions.put(i, isComplete);
      }
      for (double i = 501; i <= 1000.0; i++) {
        boolean isComplete = new Random().nextInt(100) < 50;
        newTransactions.put(i, isComplete);
      }

      // Transactions 5
      HashMap<Double, Boolean> newTransactions5 = new HashMap<>();
      newTransactions.put(1.0, true);
      newTransactions.put(2.0, true);
      newTransactions.put(3.0, true);
      newTransactions.put(4.0, true);
      for (double i = 1.0; i <= 500.0; i++) {
        boolean isComplete = new Random().nextInt(100) < 40;
        newTransactions.put(i, isComplete);
      }
      for (double i = 501; i <= 1000.0; i++) {
        boolean isComplete = new Random().nextInt(100) < 50;
        newTransactions.put(i, isComplete);
      }

      // Transactions 6
      HashMap<Double, Boolean> newTransactions6 = new HashMap<>();
      newTransactions.put(1.0, false);
      newTransactions.put(2.0, true);
      newTransactions.put(3.0, false);
      newTransactions.put(4.0, false);
      for (double i = 1.0; i <= 1000.0; i++) {
        boolean isComplete = new Random().nextInt(100) < 60;
        newTransactions.put(i, isComplete);
      }

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId2, consumerAgentId1, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId6, consumerAgentId2, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId4, consumerAgentId3, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId3, consumerAgentId4, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId5, consumerAgentId5, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId1, consumerAgentId6, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId1, consumerAgentId2, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId2, consumerAgentId4, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId3, consumerAgentId1, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId4, consumerAgentId3, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId5, consumerAgentId5, "transactionHistory", newTransactions, true);

      janusGraphManager.modifyConnectionProperty(
          consumerAgentId6, consumerAgentId6, "transactionHistory", newTransactions, true);

    } catch (Exception e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Please Start JanusGraph Cassandra Server");
      alert.setHeaderText("Expected IP & Port: 127.0.0.1:9042");
      alert.setContentText(Arrays.toString(e.getStackTrace()));
      alert.showAndWait();
      e.printStackTrace();
    }

    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("JanusGraph Schema Initialisation & Agent Added Completed Successfully");
    alert.setHeaderText("");
  }

  /**
   * Function used to import a JanusGraph NoSQL Database with a Graph Database Schema into an Agent
   * System object, including the databases' Agent & Agent Transactional History dataset.
   *
   * @param event stores data related to caller of event
   */
  @FXML
  public void startImport(Event event) {

    Queue<ServiceRequest> serviceRequestQueue = new PriorityQueue<>();

    janusGraphManager = JanusGraphManager.getInstance();

    janusGraphManager.setCassandraElasticSearchServerAndInitialise(
        cassandraHostNameTextField.getText(),
        checkValueOrReplace(Integer.parseInt(cassandraPortTextField.getText()), 0, 99999, 9042),
        elasticsearchHostNameTextField.getText(),
        checkValueOrReplace(
            Integer.parseInt(elasticsearchPortTextField.getText()), 0, 99999, 9200));

    List<Integer> allAgentIds = janusGraphManager.getAgentIds();
    int numberOfAgents = allAgentIds.size();

    agentSystem = new AgentSystem("JanusGraphEigenSystem1");

    maxServiceRequestTime = 0;

    for (int i = 0; i < allAgentIds.size(); i++) {
      for (int j = 0; j < allAgentIds.size(); j++) {
        if (i != j) {

          HashMap<Double, Boolean> transactionHistory =
              (HashMap<Double, Boolean>)
                  janusGraphManager.getConnectionProperty(
                      allAgentIds.get(i), allAgentIds.get(j), "transactionHistory");

          if (transactionHistory != null) {

            for (Map.Entry<Double, Boolean> transaction : transactionHistory.entrySet()) {
              ServiceRequest newRequest = new ServiceRequest(0);
              newRequest.setConsumer(i);
              newRequest.setProducer(j);
              newRequest.setRequestTime(transaction.getKey().intValue());
              newRequest.setCompletionTime(transaction.getKey().intValue());
              if (transaction.getValue()) {
                newRequest.setTransactionOutcome(true);
              } else {
                newRequest.setTransactionOutcome(false);
              }

              maxServiceRequestTime =
                  Math.max(maxServiceRequestTime, newRequest.getCompletionTime());
              serviceRequestQueue.add(newRequest);
            }
          }
        }
      }
    }

    agentHashMap = new HashMap<>();
    agentPreTrustedVector = new double[numberOfAgents];

    int numberOfPreTrustedAgents = 0;
    for (int i = 0; i < allAgentIds.size(); i++) {
      if ((boolean) janusGraphManager.getAgentProperty(allAgentIds.get(i), "isTrustedAgent")) {
        agentPreTrustedVector[i] = 1.0;
        numberOfPreTrustedAgents++;
        agentHashMap.put(
            allAgentIds.get(i),
            new Agent(allAgentIds.get(i), new AgentTypeWrapper(AgentType.ISTRUSTED, 0)));
      } else {
        agentPreTrustedVector[i] = 0.0;
        agentHashMap.put(
            allAgentIds.get(i),
            new Agent(allAgentIds.get(i), new AgentTypeWrapper(AgentType.REALUSER, 0)));
      }
    }

    if (numberOfPreTrustedAgents > 0) {
      for (int i = 0; i < allAgentIds.size(); i++) {
        agentPreTrustedVector[i] /= numberOfPreTrustedAgents;
      }
    }

    agentSystem.setNumberOfAgents(numberOfAgents);
    agentSystem.setAgentHashMap(0, agentHashMap);
    agentSystem.setAgentPreTrustedVector(0, agentPreTrustedVector);
    agentSystem.setNumberOfServiceRequests(maxServiceRequestTime);
    agentSystem.setNumberOfServiceRequestTypes(1);

    runCalculationUsingTrustModels(serviceRequestQueue);

    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Agent System Import & Trust Model Calculations Completed Successfully");
    alert.setHeaderText("Results can be analysed in the Calculator Evaluator");
  }

  private void runCalculationUsingTrustModels(Queue<ServiceRequest> serviceRequestQueue) {
    initialiseTrustModelObjects();

    try {

      String originalName = agentSystem.getSystemName();
      String timeStampString = (new Timestamp((new Date()).getTime())).toString();

      for (String trustModel : trustModels.keySet()) {

        AgentSystem resettedSystem = agentSystem;

        resettedSystem.setSystemName(originalName);

        SimulationManager simulationManager =
            new CalculationSimulationManager(
                resettedSystem,
                trustModels.get(trustModel),
                maxServiceRequestTime,
                1,
                10,
                serviceRequestQueue);

        AgentSystem agentSystemResult = simulationManager.runSimulation(0);

        if (trustModel.equals("EigenTrustModel")) {
          agentSystemResult.setTrustModel(TrustModelEnum.EigenTrustModel);
        }

        agentSystemResult.setSystemName(agentSystemResult.getSystemName() + " " + timeStampString);
        sqLiteDatabaseManager.addObjectToTable(
            "agentSystemsCalculator",
            agentSystemResult.getSystemName() + agentSystemResult.getTrustModel().toString(),
            agentSystemResult);
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Function initialises the parameters of the simulation based on the values inputted by the user
   * in the JavaFX text fields.
   */
  protected void initialiseSimulationParameters() {

    // EigenTrustModel
    epsilonConstantInput.setText(String.valueOf(0.5));
    alphaConstantInput.setText(String.valueOf(0.001));

    // Source Database Settings
    cassandraHostNameTextField.setText("127.0.0.1");
    cassandraPortTextField.setText(String.valueOf(9042));
    elasticsearchHostNameTextField.setText("127.0.0.1");
    elasticsearchPortTextField.setText(String.valueOf(9200));
  }

  /**
   * Function creates Trust model object and initialises them with initialised with their default=
   * parameters.
   */
  private void initialiseTrustModelObjects() {

    // Update Trust Model Parameters

    EigenTrustModel eigenTrustModel = new EigenTrustModel();
    eigenTrustModel.setAlphaConst(
        checkValueOrReplace(Double.parseDouble(alphaConstantInput.getText()), 0, 1.0, 0.5));
    eigenTrustModel.setEpsilonConst(
        checkValueOrReplace(
            Double.parseDouble(epsilonConstantInput.getText()), 0.0000001, 1.0, 0.001));

    trustModels.put("EigenTrustModel", eigenTrustModel);
  }

  /**
   * Function used externally to set the stage object that this evaluator controller's view is
   * displayed within.
   *
   * @param stage JavaFX stage object
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * Function used to call on the TrustSim scene controller class to change the currently displayed
   * scene.
   *
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

  /**
   * Function used as event dispatcher for scene's buttons, that for displaying tooltip information
   * objects.
   *
   * @param event stores data related to caller of event
   */
  public void helpOnClicked(ActionEvent event) {

    Object eventSource = event.getSource();

    final Tooltip helpTooltip = new Tooltip();
    helpTooltip.setShowDelay(new Duration(0.1));

    if (eventSource.equals(epsilonHelpButton)) {
      helpTooltip.setText("Convergence limit of model (Allowed Value: 0.0000001 to 1.0)");
    } else if (eventSource.equals(alphaHelpButton)) {
      helpTooltip.setText(
          "Weighting of Trusted Agents in calculating local trust values (Allowed Value: 0.0 to 1.0)");
    } else if (eventSource.equals(cassandraHostnameHelpButton)) {
      helpTooltip.setText("Cassandra Server Hostname (IPv4 Format e.g. 127.0.0.1)");
    } else if (eventSource.equals(cassandraPortHelpButton)) {
      helpTooltip.setText("Cassandra Server Port (Allowed Values: 1 to 99999)");
    } else if (eventSource.equals(elasticSearchHostnameHelpButton)) {
      helpTooltip.setText("Elastic Search Server Hostname (IPv4 Format e.g. 127.0.0.1)");
    } else if (eventSource.equals(elasticSearchPortHelpButton)) {
      helpTooltip.setText("Elastic Search Server Port (Allowed Values: 1 to 99999)");
    } else if (eventSource.equals(initialiseGraphHelpButton)) {
      helpTooltip.setText("Initialise a JanusGraph Database with an Example Trust System Dataset");
    } else if (eventSource.equals(importGraphHelpButton)) {
      helpTooltip.setText(
          "Import Trust System from JanusGraph Database, and then Run Trust Model Calculations on it");
    }

    Point2D p = ((Button) event.getSource()).localToScene(20.0, 20.0);
    ((Button) event.getSource()).setTooltip(helpTooltip);
    helpTooltip.setAutoHide(true);
    helpTooltip.show(
        stage,
        p.getX()
            + ((Button) event.getSource()).getScene().getX()
            + ((Button) event.getSource()).getScene().getWindow().getX(),
        p.getY()
            + ((Button) event.getSource()).getScene().getY()
            + ((Button) event.getSource()).getScene().getWindow().getY());
  }
}
