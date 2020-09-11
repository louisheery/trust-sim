package com.trustsim.evaluator;

import com.trustsim.synthesiser.AgentSystem;
import com.trustsim.synthesiser.SystemPane;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;

/**
 * Class used to render a JavaFX Pane that displays the simulation settings pane that can be used to
 * view and modify the parameters used to run a simulation, that are stored in an Agent System
 * object.
 */
public class SimpleSimulationSettingsPane extends SystemPane {

  @FXML private Text systemNameText;
  @FXML private Text epsilonConstantInput;
  @FXML private Text alphaConstantInput;
  @FXML private Text epsilonConstantElasticInput;
  @FXML private Text alphaConstantElasticInput;
  @FXML private Text decayConstantElasticInput;
  @FXML private Text numberOfServiceRequests;
  @FXML private Text trustUpdateIntervalInput;
  @FXML private Text numberOfAgentsInput;
  private final GroupedAgentSystem selectedAgentSystem;

  /**
   * Constructor which initialises JavaFX Pane with the simulation parameters that are stored within
   * a GroupedAgentSystem object.
   *
   * @param selectedAgentSystem object storing 1 or more Agent System objects, to be displayed in
   *     the Pane.
   */
  public SimpleSimulationSettingsPane(GroupedAgentSystem selectedAgentSystem) {

    this.selectedAgentSystem = selectedAgentSystem;
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/SystemPane/SimpleSimulationSettingsPane.fxml"));

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

    systemNameText.setText(selectedAgentSystem.getSystemName());

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
    numberOfAgentsInput.setText(String.valueOf(selectedAgentSystem.getNumberOfAgents()));
  }
}
