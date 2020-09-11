package com.trustsim.evaluator;

import com.trustsim.evaluator.charts.ActualVsPredictedTrustValueChartPane;
import com.trustsim.evaluator.charts.AgentGlobalTrustChartPane;
import com.trustsim.evaluator.charts.TransactionHistoryChartPane;
import com.trustsim.evaluator.data.GraphDataObj;
import com.trustsim.evaluator.data.TableDataObj;
import com.trustsim.evaluator.tables.OneDimTable;
import com.trustsim.evaluator.tables.TwoDimTable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Class is Controller of the Evaluator Scene of application.
 */
@SuppressWarnings("rawtypes")
public class EvaluatorController extends EvaluatorBaseController {

  @FXML private HBox systemInformationContainerHBox;
  @FXML private ImageView trustSimLogoView;
  @FXML private ComboBox individualGraphAgentTrustTypeComboBox;
  @FXML private ComboBox individualGraphSelectorComboBox;
  @FXML private HBox individualGraphContainerHBox;
  @FXML private ComboBox individualTableSelectorComboBox;
  @FXML private ComboBox graphAgentTrustTypeComboBox;
  @FXML private ComboBox graphSelectorComboBox;
  @FXML private HBox graphContainerHBox;
  @FXML private ComboBox tableSelectorComboBox;
  @FXML private HBox tableContainerHBox;
  @FXML private Text selectResultPromptTextField;
  @FXML private HBox trustSimLogoHBox;
  @FXML private TabPane tabPaneUpper;

  private final String sqlTableName = "simulationResults";


  /**
   * Function initialises controller for Evaluator screen, by loading dataset from database
   * and initialising combo boxes and other child view panes.
   *
   * @param location specifies fxml file location
   * @param resources specifies resource bundle
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    trustSimLogoHBox.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    Image trustSimLogo = new Image(getClass().getResourceAsStream("/images/logo/trustsimlogo.png"));
    trustSimLogoView.setImage(trustSimLogo);

    initialiseTableView();
    initialiseAddRemoveButtons(sqlTableName);
    refreshTableView(sqlTableName);
    refreshEvaluatorGraphPane();
    refreshEvaluatorTablePane();
    refreshSystemInfoPane();
    refreshResultView();
    selectResultPromptTextField.setVisible(true);

    tabPaneUpper.getSelectionModel().selectedItemProperty().addListener(
        (observableValue, oldTab, newTab) -> {
          selectFirstOfEachComboBox();
          reloadIndividualGraphDataView();
          reloadIndividualTableDataView();
          reloadGraphDataView();
          reloadTableDataView();
        }
    );

  }



  /**
   * Function initialises EvaluatorController combo boxes to their first selectable value.
   *
   */
  private void selectFirstOfEachComboBox() {

    graphSelectorComboBox.getSelectionModel().selectFirst();
    individualGraphSelectorComboBox.getSelectionModel().selectFirst();

    tableSelectorComboBox.getSelectionModel().selectFirst();
    individualTableSelectorComboBox.getSelectionModel().selectFirst();

    simulationNumberGraphSelectorComboBox.getSelectionModel().selectFirst();
    simulationNumberTableSelectorComboBox.getSelectionModel().selectFirst();

    graphAgentTrustTypeComboBox.getSelectionModel().selectFirst();
    individualGraphAgentTrustTypeComboBox.getSelectionModel().selectFirst();

    individualGraphSelectorComboBox.getSelectionModel().selectFirst();
    graphSelectorComboBox.getSelectionModel().selectFirst();

    individualTableSelectorComboBox.getSelectionModel().selectFirst();
    tableSelectorComboBox.getSelectionModel().selectFirst();
  }



  /**
   * Function called when the combo box that specifies which graph corresponding to the average of a
   * set of two or more simulations or a specific simulation is currently to be displayed has
   * changed, and then reloads the corresponding graph pane with the newly selected graph.
   *
   * @param event stores data related to caller of event
   */
  public void comboBoxGraphAction(ActionEvent event) {
    if (((ComboBox) event.getSource()).getValue() != null) {

      if (individualGraphSelectorComboBox.equals(event.getSource())) {
        selectedAllAgentSystemGraph = (GraphDataObj) individualGraphSelectorComboBox.getValue();
        reloadIndividualGraphDataView();
      } else if (graphSelectorComboBox.equals(event.getSource())) {
        selectedAllAgentSystemGraph = (GraphDataObj) ((ComboBox) event.getSource()).getValue();
        reloadGraphDataView();

      }
    }
  }

  /**
   * Function called when the combo box that specifies which agent that the graph corresponding to
   * the average of a set of two or more simulations or a specific simulation is currently to be
   * displayed has changed, and then reloads the corresponding graph pane with the newly selected
   * graph.
   *
   * @param event stores data related to caller of event
   */
  public void comboBoxAgentTypeAction(ActionEvent event) {
    if (((ComboBox) event.getSource()).getValue() != null) {

      if (individualGraphAgentTrustTypeComboBox.equals(event.getSource())) {
        selectedGraphAgentType = (AgentTypeWrapper) ((ComboBox) event.getSource()).getValue();
        reloadIndividualGraphDataView();
      } else if (graphAgentTrustTypeComboBox.equals(event.getSource())) {
        selectedGraphAgentType = (AgentTypeWrapper) ((ComboBox) event.getSource()).getValue();
        reloadGraphDataView();
      }
    }
  }

  /**
   * Function called when the combo box that specifies which agent system that the table should
   * display data for, and then reloads the corresponding table pane with the newly selected
   * data.
   *
   * @param event stores data related to caller of event
   */
  public void comboBoxTableAction(ActionEvent event) {
    if (((ComboBox) event.getSource()).getValue() != null) {

      if (individualTableSelectorComboBox.equals(event.getSource())) {
        selectedAllAgentSystemTable = (TableDataObj) ((ComboBox) event.getSource()).getValue();
        reloadIndividualTableDataView();
      } else if (tableSelectorComboBox.equals(event.getSource())) {
        selectedAllAgentSystemTable = (TableDataObj) ((ComboBox) event.getSource()).getValue();
        reloadTableDataView();
      }
    }
  }

  /**
   * Function used for clearing all simulation results data from the SQLite database.
   *
   * @param event stores data related to caller of event
   */
  @Override
  public void deleteAllEvaluatorItems(ActionEvent event) {
    sqLiteDatabaseManager.removeAllObjectsFromTable(sqlTableName);
    refreshTableView(sqlTableName);
  }

  /**
   * Function re-renders the graph pane, which is called whenever the dataset which the graph pane
   * is required to display has changed.
   */
  @Override
  protected void refreshEvaluatorGraphPane() {
    if (selectedSimulationResult != null) {
      tabPaneParent.setVisible(true);
      initializeGraphComboBox();
      graphSelectorComboBox.getSelectionModel().selectFirst();
      individualGraphSelectorComboBox.getSelectionModel().selectFirst();
      selectedAllAgentSystemGraph = (GraphDataObj) graphSelectorComboBox.getSelectionModel().getSelectedItem();
    } else {
      tabPaneParent.setVisible(false);
    }
  }

  /**
   * Function re-renders the table pane, which is called whenever the dataset which the table pane
   * is required to display has changed.
   */
  @Override
  protected void refreshEvaluatorTablePane() {

    if (selectedSimulationResult != null) {
      tabPaneParent.setVisible(true);
      initializeTableComboBox();
      tableSelectorComboBox.getSelectionModel().selectFirst();
      individualTableSelectorComboBox.getSelectionModel().selectFirst();
      selectedAllAgentSystemTable = (TableDataObj) tableSelectorComboBox.getSelectionModel().getSelectedItem();
    } else {
      tabPaneParent.setVisible(false);
    }
  }

  /**
   * Function initialises combo box used to select which agent of the dataset should be displayed
   * on the table and graph views, based on the number of agents in the dataset.
   */
  @Override
  protected void initializeAgentTypeComboBox() {

    List<AgentTypeWrapper> outputFormatList = new ArrayList<>();
    if (selectedSimulationResult != null) {

      for (AgentTypeWrapper type : selectedSimulationResult.getAgentTypes()) {
        outputFormatList.add(type);
      }
    }
    ObservableList<AgentTypeWrapper> agentTypeData = FXCollections.observableList(outputFormatList);

    graphAgentTrustTypeComboBox.getItems().clear();
    graphAgentTrustTypeComboBox.getItems().addAll(agentTypeData);
    graphAgentTrustTypeComboBox.getSelectionModel().selectFirst();
    graphAgentTrustTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          selectedGraphAgentType = (AgentTypeWrapper) newValue;
          reloadGraphDataView();
        }
    );

    individualGraphAgentTrustTypeComboBox.getItems().clear();
    individualGraphAgentTrustTypeComboBox.getItems().addAll(agentTypeData);
    individualGraphAgentTrustTypeComboBox.getSelectionModel().selectFirst();
    individualGraphAgentTrustTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          selectedGraphAgentType = (AgentTypeWrapper) newValue;
          reloadIndividualGraphDataView();
        }
    );

  }

  /**
   * Function initialises the graph selector combo box with GraphDataObj objects that store the dataset
   * to be plotted on the graph pane whenever that combo box item is selected.
   */
  @Override
  protected void initializeGraphComboBox() {

    List<GraphDataObj> individualOutputFormatList = new ArrayList<>();
    if (selectedSimulationResult != null) {
      if (selectedSimulationResult.getServiceRequestHistoryAll() != null) {
        individualOutputFormatList.add(new GraphDataObj(
            "Service Request History",
            "Service Request Time",
            "Number of Service Requests",
            GraphTypeEnum.AreaGraph,
            null,
            null,
            selectedSimulationResult.getServiceRequestHistoryAll(),
            selectedSimulationResult.getNumberOfServiceRequests(),
            selectedSimulationResult.getTrustUpdateInterval()
        ));
        individualOutputFormatList.add(new GraphDataObj(
            "Predicted vs. Actual Trust Values",
            "Actual Agent Trust Values",
            "EigenTrust Predicted Agent Trust Values",
            GraphTypeEnum.ScatterGraph,
            selectedSimulationResult.getAgentPersonalityTrustValuesAll(),
            selectedSimulationResult.getGlobalTrustVectorAll(),
            null,
            selectedSimulationResult.getNumberOfServiceRequests(),
            selectedSimulationResult.getTrustUpdateInterval()
        ));

        individualOutputFormatList.add(new GraphDataObj(
            "Agent Global Trust Value",
            "Service Request Time",
            "Agent Global Trust Value",
            GraphTypeEnum.GlobalTrustGraph,
            selectedSimulationResult.getTrustModelNames(),
            selectedSimulationResult.getGlobalTrustVectorTimeSeriesAll(),
            selectedSimulationResult.getNumberOfServiceRequests(),
            selectedSimulationResult.getTrustUpdateInterval()
        ));

      }
    }

    ObservableList<GraphDataObj> graphAllFormatData = FXCollections
        .observableList(individualOutputFormatList);

    individualGraphSelectorComboBox.getItems().clear();
    individualGraphSelectorComboBox.getItems().addAll(graphAllFormatData);
    individualGraphSelectorComboBox.getSelectionModel().selectFirst();
    individualGraphSelectorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          selectedAllAgentSystemGraph = (GraphDataObj) newValue;
          reloadIndividualGraphDataView();

          if (newValue != null) {
            if (((GraphDataObj) newValue).getGraphType().equals(GraphTypeEnum.GlobalTrustGraph)) {
              individualGraphAgentTrustTypeComboBox.setVisible(true);
              graphAgentTrustTypeComboBox.setVisible(true);
            } else {
              individualGraphAgentTrustTypeComboBox.setVisible(false);
              graphAgentTrustTypeComboBox.setVisible(false);
            }
          }
        }
    );

    graphSelectorComboBox.getItems().clear();
    graphSelectorComboBox.getItems().addAll(graphAllFormatData);
    graphSelectorComboBox.getSelectionModel().selectFirst();
    graphSelectorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          selectedAllAgentSystemGraph = (GraphDataObj) newValue;
          reloadIndividualGraphDataView();

          if (newValue != null) {
            if (((GraphDataObj) newValue).getGraphType().equals(GraphTypeEnum.GlobalTrustGraph)) {
              individualGraphAgentTrustTypeComboBox.setVisible(true);
              graphAgentTrustTypeComboBox.setVisible(true);
            } else {
              individualGraphAgentTrustTypeComboBox.setVisible(false);
              graphAgentTrustTypeComboBox.setVisible(false);
            }
          }
        }
    );
  }

  /**
   * Function initialises the table selector combo box with TableDataObj objects that store the dataset
   * to be plotted on the table pane whenever that combo box item is selected.
   */
  @Override
  protected void initializeTableComboBox() {

    List<TableDataObj> individualOutputFormatList = new ArrayList<>();
    if (selectedSimulationResult != null) {
      individualOutputFormatList.add(new TableDataObj(
          "Personality vs. Predicted Trust Values",
          TableTypeEnum.OneDimTable,
          new ArrayList<>(Arrays.asList("Actual", "EigenTrust", "DynamicTrust")),
          selectedSimulationResult.getAgentPersonalityTrustValuesAll(),
          selectedSimulationResult.getGlobalTrustVectorAll(),
          null));

      individualOutputFormatList.add(new TableDataObj(
          "Local Trust Values",
          TableTypeEnum.TwoDimTable,
          new ArrayList<>(Arrays.asList("Trustee", "Trustor")),
          null,
          null,
          selectedSimulationResult.getLocalTrustConnectionMatrixAll()));
    }
    ObservableList<TableDataObj> tableAllFormatData = FXCollections
        .observableList(individualOutputFormatList);

    individualTableSelectorComboBox.getItems().clear();
    individualTableSelectorComboBox.getItems().addAll(tableAllFormatData);
    individualTableSelectorComboBox.getSelectionModel().selectFirst();
    individualTableSelectorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          selectedAllAgentSystemTable = (TableDataObj) newValue;
        }
    );

    tableSelectorComboBox.getItems().clear();
    tableSelectorComboBox.getItems().addAll(tableAllFormatData);
    tableSelectorComboBox.getSelectionModel().selectFirst();
    tableSelectorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          selectedAllAgentSystemTable = (TableDataObj) newValue;
        }
    );

  }

  /**
   * Function re-renders the contents of a combined simulation results data graph pane view,
   * based on the type of table that is specified in the GraphDataObj object.
   */
  private void reloadGraphDataView() {

    graphContainerHBox.getChildren().clear();
    switch (selectedAllAgentSystemGraph.getGraphType()) {
      case AreaGraph -> {
        graphContainerHBox.getChildren().add(new TransactionHistoryChartPane(selectedAllAgentSystemGraph, -1));
      }
      case ScatterGraph -> {
        graphContainerHBox.getChildren().add(new ActualVsPredictedTrustValueChartPane(selectedAllAgentSystemGraph, -1));
      }
      case GlobalTrustGraph -> {
        List<Integer> agentTypeIndices = selectedSimulationResult.getAgentIndexOfType(selectedGraphAgentType);
        graphContainerHBox.getChildren().add(new AgentGlobalTrustChartPane(selectedAllAgentSystemGraph, -1, selectedSimulationResult.getTrustUpdateInterval(), agentTypeIndices, selectedGraphAgentType));
      }
    }

  }

  /**
   * Function re-renders the contents of a individual simulation results data graph pane view,
   * based on the type of table that is specified in the GraphDataObj object.
   */
  @Override
  protected void reloadIndividualGraphDataView() {

    individualGraphContainerHBox.getChildren().clear();

    if (selectedSimulationResult != null && selectedGraphAgentType != null && selectedAllAgentSystemGraph != null && selectedAgentSystemSimulationNumber != null) {
      switch (selectedAllAgentSystemGraph.getGraphType()) {
        case AreaGraph -> {
          individualGraphContainerHBox.getChildren().add(new TransactionHistoryChartPane(selectedAllAgentSystemGraph, selectedAgentSystemSimulationNumber.getNumber()));
        }
        case ScatterGraph -> {
          individualGraphContainerHBox.getChildren().add(new ActualVsPredictedTrustValueChartPane(selectedAllAgentSystemGraph, selectedAgentSystemSimulationNumber.getNumber()));
        }
        case GlobalTrustGraph -> {
          List<Integer> agentTypeIndices = selectedSimulationResult.getAgentIndexOfType(selectedGraphAgentType);
          individualGraphContainerHBox.getChildren().add(new AgentGlobalTrustChartPane(selectedAllAgentSystemGraph, selectedAgentSystemSimulationNumber.getNumber(), selectedSimulationResult.getTrustUpdateInterval(), agentTypeIndices, selectedGraphAgentType));
        }
      }
    }
  }

  /**
   * Function re-renders the contents of a combined simulation results data table pane view,
   * based on the type of table that is specified in the TableDataObj object.
   */
  private void reloadTableDataView() {

    tableContainerHBox.getChildren().clear();
    switch (selectedAllAgentSystemTable.getTableType()) {
      case OneDimTable -> {
        tableContainerHBox.getChildren().add(new OneDimTable(selectedAllAgentSystemTable, -1));
      }
      case TwoDimTable -> {
        tableContainerHBox.getChildren().add(new TwoDimTable(selectedAllAgentSystemTable, -1, "EigenTrustModel"));
      }
    }
  }

  /**
   * Function re-renders the simulation information pane to display the setting used to run the
   * currently selected group or one or more simulation results objects (AgentSystem objects).
   */
  @Override
  protected void refreshSystemInfoPane() {

    systemInformationContainerHBox.getChildren().clear();
    if (selectedSimulationResult != null) {
      systemInformationContainerHBox.getChildren().add(new SimulationSettingsPane(selectedSimulationResult));
    }
  }


}
