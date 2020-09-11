package com.trustsim.evaluator;

import static com.trustsim.simulator.agent.AgentType.BAD;
import static com.trustsim.simulator.agent.AgentType.GOOD;
import static com.trustsim.simulator.agent.AgentType.ISTRUSTED;
import static com.trustsim.simulator.agent.AgentType.MALICIOUS;
import static com.trustsim.simulator.agent.AgentType.OK;
import static com.trustsim.simulator.agent.AgentType.VBAD;
import static com.trustsim.simulator.agent.AgentType.VGOOD;

import com.trustsim.simulator.agent.AgentType;
import com.trustsim.synthesiser.AgentSystem;
import com.trustsim.synthesiser.SystemPane;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
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
 * Class used to render a JavaFX Pane that displays the simulation settings pane that can be used to
 * view and modify the parameters used to run a simulation, that are stored in an Agent System
 * object.
 */
public class SimulationSettingsPane extends SystemPane {

  @FXML private Text systemNameText;
  @FXML private Text epsilonConstantInput;
  @FXML private Text alphaConstantInput;
  @FXML private Text epsilonConstantElasticInput;
  @FXML private Text alphaConstantElasticInput;
  @FXML private Text decayConstantElasticInput;
  @FXML private Text numberOfServiceRequests;
  @FXML private Text trustUpdateIntervalInput;
  @FXML private Text numberOfTrustedAgentsInput;
  @FXML private Text numberOfVGoodAgentsInput;
  @FXML private Text numberOfGoodAgentsInput;
  @FXML private Text numberOfOkAgentsInput;
  @FXML private Text numberOfBadAgentsInput;
  @FXML private Text numberOfVBadAgentsInput;
  @FXML private Text numberOfMaliciousAgentsInput;
  @FXML private Text changingAgentStartPersonalityComboBox;
  @FXML private Text changingAgentEndPersonalityComboBox;
  @FXML private Text numberOfChangingAgentsInput;
  @FXML private ChartViewer agentPersonalityChart;
  private final GroupedAgentSystem selectedAgentSystem;

  /**
   * Constructor which initialises JavaFX Pane with the simulation parameters that are stored within
   * a GroupedAgentSystem object.
   *
   * @param selectedAgentSystem object storing 1 or more Agent System objects, to be displayed in
   *     the Pane.
   */
  public SimulationSettingsPane(GroupedAgentSystem selectedAgentSystem) {

    this.selectedAgentSystem = selectedAgentSystem;
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/SystemPane/SimulationSettingsPane.fxml"));

    try {
      fxmlLoader.setController(this);
      fxmlLoader.setRoot(this);
      fxmlLoader.load();
      fxmlLoader.getRoot();
      reloadSystemInformationPane();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  /**
   * Function used to re-render the information displayed in the JavaFX Pane of this class, which
   * will fetch the GroupedAgentSystem member variable of this class, and then initialise the Pane's
   * child nodes with the simulation parameters stored within this member variable.
   */
  protected void reloadSystemInformationPane() {

    loadAgentPersonalityChart();
    systemNameText.setText(selectedAgentSystem.getSystemName());
    numberOfTrustedAgentsInput.setText(
        String.valueOf(selectedAgentSystem.getNumberOfTrustedAgents()));
    numberOfVGoodAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfVGoodAgents()));
    numberOfGoodAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfGoodAgents()));
    numberOfOkAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfOkAgents()));
    numberOfBadAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfBadAgents()));
    numberOfVBadAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfVBadAgents()));
    numberOfMaliciousAgentsInput.setText(
        String.valueOf(selectedAgentSystem.getNumberOfMaliciousAgents()));
    numberOfChangingAgentsInput.setText(
        String.valueOf(selectedAgentSystem.getNumberOfElasticAgents()));
    changingAgentStartPersonalityComboBox.setText(
        String.valueOf(selectedAgentSystem.getElasticAgentStartPersonality()));
    changingAgentEndPersonalityComboBox.setText(
        String.valueOf(selectedAgentSystem.getElasticAgentEndPersonality()));

    AgentSystem eigenTrustModel = selectedAgentSystem.getAgentSystem("EigenTrustModel");
    if (eigenTrustModel != null) {
      epsilonConstantInput.setText(String.valueOf(eigenTrustModel.getEpsilonConstantValue()));
      alphaConstantInput.setText(String.valueOf(eigenTrustModel.getAlphaConstantValue()));
    }

    AgentSystem dynamicTrustModel = selectedAgentSystem.getAgentSystem("DynamicTrustModel");
    if (dynamicTrustModel != null) {
      epsilonConstantElasticInput.setText(
          String.valueOf(dynamicTrustModel.getEpsilonConstantValue()));
      alphaConstantElasticInput.setText(String.valueOf(dynamicTrustModel.getAlphaConstantValue()));
      decayConstantElasticInput.setText(String.valueOf(dynamicTrustModel.getDecayConstantValue()));
    }

    numberOfServiceRequests.setText(
        String.valueOf(selectedAgentSystem.getNumberOfServiceRequests()));
    trustUpdateIntervalInput.setText(String.valueOf(selectedAgentSystem.getTrustUpdateInterval()));
  }

  /**
   * Function used to instantiate and define the properties of a JFreeChart XYPlot which is used to
   * display the probability distribution of trustworthiness which different AgentType personality
   * agents possess.
   */
  private void loadAgentPersonalityChart() {

    List<AgentType> personalityTypes =
        new ArrayList<>(Arrays.asList(VGOOD, GOOD, OK, BAD, VBAD, MALICIOUS, ISTRUSTED));

    XYSeriesCollection output = new XYSeriesCollection();

    for (AgentType type : personalityTypes) {
      Function2D gaussianDistribution = new NormalDistributionFunction2D(type.mean, type.stDev);
      XYSeries series =
          DatasetUtils.sampleFunction2DToSeries(gaussianDistribution, 0.0, 1.0, 100, type.name);
      output.addSeries(series);
    }

    NumberAxis axisX = new NumberAxis("Agent Trustworthiness");
    NumberAxis axisY = new NumberAxis("Probability Density");
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
    renderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
    XYPlot plot = new XYPlot(output, axisX, axisY, renderer);
    plot.setBackgroundPaint(new Color(0xf4f4f4));
    JFreeChart chart = new JFreeChart("Agent Personalities", plot);
    chart.setBackgroundPaint(new Color(0xf4f4f4));
    agentPersonalityChart.setChart(chart);
    agentPersonalityChart.setPadding(new Insets(0, 5, 0, 5));
  }
}
