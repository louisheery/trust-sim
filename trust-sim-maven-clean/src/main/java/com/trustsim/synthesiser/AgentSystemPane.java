package com.trustsim.synthesiser;

import static com.trustsim.UtilityFunction.checkValueOrReplace;
import static com.trustsim.simulator.agent.AgentType.BAD;
import static com.trustsim.simulator.agent.AgentType.GOOD;
import static com.trustsim.simulator.agent.AgentType.ISTRUSTED;
import static com.trustsim.simulator.agent.AgentType.MALICIOUS;
import static com.trustsim.simulator.agent.AgentType.OK;
import static com.trustsim.simulator.agent.AgentType.VBAD;
import static com.trustsim.simulator.agent.AgentType.VGOOD;

import com.trustsim.simulator.agent.AgentType;
import com.trustsim.storage.SQLiteDatabaseManager;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Class used to render a JavaFX Pane that displays the agent system settings pane that can be used
 * to view and modify the parameters of the agent system, that are stored in an Agent System
 * object.
 */
public class AgentSystemPane extends SystemPane {

  private final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();

  @FXML private ChartViewer agentPersonalityChart;
  @FXML private VBox tab1VBox;
  @FXML private Text systemNameText;
  @FXML private Button parameterSaveButton;
  @FXML private TextField numberOfTrustedAgentsInput;
  @FXML private TextField numberOfVGoodAgentsInput;
  @FXML private TextField numberOfGoodAgentsInput;
  @FXML private TextField numberOfOkAgentsInput;
  @FXML private TextField numberOfBadAgentsInput;
  @FXML private TextField numberOfVBadAgentsInput;
  @FXML private TextField numberOfMaliciousAgentsInput;
  @FXML private TextField numberOfChangingAgentsInput;
  @FXML private ComboBox<AgentType> changingAgentStartPersonalityComboBox;
  @FXML private ComboBox<AgentType> changingAgentEndPersonalityComboBox;
  @FXML private Button trustedAgentHelpButton;
  @FXML private Button veryGoodAgentHelpButton;
  @FXML private Button goodAgentHelpButton;
  @FXML private Button okAgentHelpButton;
  @FXML private Button badAgentHelpButton;
  @FXML private Button veryBadAgentHelpButton;
  @FXML private Button maliciousAgentHelpButton;
  @FXML private Button changingAgentHelpButton;
  @FXML private Button startPersonalityAgentHelpButton;
  @FXML private Button endPersonalityAgentHelpButton;
  @FXML private AnchorPane anchorPane;
  @FXML private AnchorPane anchorPane4;
  @FXML private GridPane gridPane;
  @FXML private GridPane gridPane4;
  @FXML private Stage stage;

  private AgentSystem selectedAgentSystem;

  /**
   * Constructor which initialises JavaFX Pane with the agent system parameters that are stored
   * within an AgentSystem object.
   *
   * @param selectedAgentSystem object storing agent system data to be displayed in the Pane.
   */
  public AgentSystemPane(AgentSystem selectedAgentSystem, Stage stage) {

    this.selectedAgentSystem = selectedAgentSystem;

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        "/SystemPane/AgentSystemPane.fxml"));

    try {

      fxmlLoader.setController(this);
      fxmlLoader.setRoot(this);
      fxmlLoader.load();
      fxmlLoader.getRoot();

      anchorPane.setBottomAnchor(gridPane, .0);
      anchorPane.setTopAnchor(gridPane, .0);
      anchorPane.setLeftAnchor(gridPane, .0);
      anchorPane.setRightAnchor(gridPane, .0);

      anchorPane4.setBottomAnchor(gridPane4, .0);
      anchorPane4.setTopAnchor(gridPane4, .0);
      anchorPane4.setLeftAnchor(gridPane4, .0);
      anchorPane4.setRightAnchor(gridPane4, .0);

      reloadSystemInformationPane();

      loadAgentPersonalityChart();

    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  /**
   * Function used to instantiate and define the properties of a JFreeChart XYPlot which is used to
   * display the probability distribution of trustworthiness which different AgentType personality
   * agents possess.
   */
  private void loadAgentPersonalityChart() {

    List<AgentType> personalityTypes = new ArrayList<>();
    personalityTypes.addAll(Arrays.asList(VGOOD, GOOD, OK, BAD, VBAD, MALICIOUS, ISTRUSTED));

    XYSeriesCollection output = new XYSeriesCollection();

    for (AgentType type : personalityTypes) {
      Function2D gaussianDistribution = new NormalDistributionFunction2D(type.mean, type.stDev);
      XYSeries series = DatasetUtils.sampleFunction2DToSeries(gaussianDistribution, 0.0, 1.0, 100, type.name);

      output.addSeries(series);

    }

    NumberAxis xAxis = new NumberAxis("Agent Trustworthiness");
    NumberAxis yAxis = new NumberAxis("Probability Density");
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
    renderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
    XYPlot plot = new XYPlot(output, xAxis, yAxis, renderer);
    plot.setBackgroundPaint(new Color(0xf4f4f4));
    JFreeChart chart = new JFreeChart("Agent Personalities", plot);
    chart.setBackgroundPaint(new Color(0xf4f4f4));
    agentPersonalityChart.setChart(chart);
    agentPersonalityChart.setPadding(new Insets(0,5,0,5));

  }

  /**
   * Function used to re-render the information displayed in the JavaFX Pane of this class, which
   * will fetch the AgentSystem member variable of this class, and then initialise the Pane's
   * child nodes with the simulation parameters stored within this member variable.
   */
  protected void reloadSystemInformationPane() {

    selectedAgentSystem = (AgentSystem) sqLiteDatabaseManager.retrieveObjectTableData("agentSystems", selectedAgentSystem.getSystemName());

    if (selectedAgentSystem != null) {
      tab1VBox.setVisible(true);
      parameterSaveButton.setOnAction(new buttonEventListener("save"));
      systemNameText.setText(selectedAgentSystem.getSystemName());

      numberOfTrustedAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfTrustedAgents()));
      numberOfVGoodAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfVGoodAgents()));
      numberOfGoodAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfGoodAgents()));
      numberOfOkAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfOkAgents()));
      numberOfBadAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfBadAgents()));
      numberOfVBadAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfVBadAgents()));
      numberOfMaliciousAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfMaliciousAgents()));

      initializeElasticAgentComboBox();
      numberOfChangingAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfElasticAgents()));
      changingAgentStartPersonalityComboBox.getSelectionModel().select(selectedAgentSystem.getElasticAgentStartPersonality());
      changingAgentEndPersonalityComboBox.getSelectionModel().select(selectedAgentSystem.getElasticAgentEndPersonality());

    } else {
      tab1VBox.setVisible(false);
    }
  }

  /**
   * Function will initialise the contents of the combo box used to select the start and end
   * personality of elastic (changing personality) agents of an agent system.
   */
  private void initializeElasticAgentComboBox() {
    List<AgentType> agentPersonalityList = new ArrayList<>(Arrays.asList(VGOOD, GOOD, OK, BAD, VBAD));
    ObservableList<AgentType> agentPersonalityData = FXCollections
        .observableList(agentPersonalityList);

    changingAgentStartPersonalityComboBox.getItems().clear();
    changingAgentStartPersonalityComboBox.getItems().addAll(agentPersonalityData);
    changingAgentStartPersonalityComboBox.getSelectionModel().select(selectedAgentSystem.getElasticAgentStartPersonality());

    changingAgentEndPersonalityComboBox.getItems().clear();
    changingAgentEndPersonalityComboBox.getItems().addAll(agentPersonalityData);
    changingAgentEndPersonalityComboBox.getSelectionModel().select(selectedAgentSystem.getElasticAgentEndPersonality());
  }

  /**
   * Event Listener object implementation which can be instantiated to listen for when the action
   * button used to save a Agent System is invoked in the view.
   */
  private class buttonEventListener implements EventHandler<ActionEvent> {

    private final String buttonEventType;

    public buttonEventListener(String eventType) {
      this.buttonEventType = eventType;
    }

    @Override
    public void handle(ActionEvent event) {

      // Delete old system
      // Add new system to table
      switch (buttonEventType) {
        case "save" -> {
          AgentSystem existingSystemConfig = selectedAgentSystem;
          String existingSystemName = existingSystemConfig.getSystemName() == null ? "" : existingSystemConfig.getSystemName();
          sqLiteDatabaseManager.removeObjectFromTable(
              "agentSystems", existingSystemName);

          AgentSystem newSystemConfig =
              new AgentSystem(
                  existingSystemName,
                  checkValueOrReplace(Integer.parseInt(numberOfTrustedAgentsInput.getText()), 1, 1000, 1),
                  checkValueOrReplace(Integer.parseInt(numberOfVGoodAgentsInput.getText()), 0, 1000, 0),
                      checkValueOrReplace(Integer.parseInt(numberOfGoodAgentsInput.getText()),0, 1000, 0),
                          checkValueOrReplace(Integer.parseInt(numberOfOkAgentsInput.getText()),0, 1000, 0),
                              checkValueOrReplace(Integer.parseInt(numberOfBadAgentsInput.getText()),0, 1000, 0),
                                  checkValueOrReplace(Integer.parseInt(numberOfVBadAgentsInput.getText()),0, 1000, 0),
                                      checkValueOrReplace(Integer.parseInt(numberOfMaliciousAgentsInput.getText()),0, 1000, 0),
                                          checkValueOrReplace(Integer.parseInt(numberOfChangingAgentsInput.getText()),0, 1000, 0),
                  changingAgentStartPersonalityComboBox.getSelectionModel().getSelectedItem(),
                  changingAgentEndPersonalityComboBox.getSelectionModel().getSelectedItem()
              );


          sqLiteDatabaseManager.addObjectToTable(
              "agentSystems", newSystemConfig.getSystemName(), newSystemConfig);
          reloadSystemInformationPane();

        }
      }
    }
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

    if (eventSource.equals(trustedAgentHelpButton)) {
      helpTooltip.setText("Highly Trustworthy Agents (Allowed Value: 0 to 1000)");
  } else if (eventSource.equals(veryGoodAgentHelpButton)) {
      helpTooltip.setText("Very Trustworthy Agents (Allowed Value: 0 to 1000)");
} else if (eventSource.equals(goodAgentHelpButton)) {
      helpTooltip.setText("Moderately Trustworthy Agents (Allowed Value: 0 to 1000)");
    } else if (eventSource.equals(okAgentHelpButton)) {
      helpTooltip.setText("Reasonably Trustworthy Agents (Allowed Value: 0 to 1000)");
    } else if (eventSource.equals(badAgentHelpButton)) {
      helpTooltip.setText("Not Very Trustworthy Agents (Allowed Value: 0 to 1000)");
    } else if (eventSource.equals(veryBadAgentHelpButton)) {
      helpTooltip.setText("Not Trustworthy Agents (Allowed Value: 0 to 1000)");
    } else if (eventSource.equals(maliciousAgentHelpButton)) {
      helpTooltip.setText("Not Trustworthy & Actively Malicious Agents (Allowed Value: 0 to 1000)");
    } else if (eventSource.equals(changingAgentHelpButton)) {
      helpTooltip.setText("Changing Personality Agents (Allowed Value: 0 to 1000)");
    } else if (eventSource.equals(startPersonalityAgentHelpButton)) {
      helpTooltip.setText("Trustworthiness Personality for first half of simulation");
    } else if (eventSource.equals(endPersonalityAgentHelpButton)) {
      helpTooltip.setText("Trustworthiness Personality for second half of simulation");
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

}