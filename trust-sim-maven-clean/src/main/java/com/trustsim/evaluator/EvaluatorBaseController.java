package com.trustsim.evaluator;

import com.trustsim.TrustSimSceneController;
import com.trustsim.evaluator.data.GraphDataObj;
import com.trustsim.evaluator.data.TableDataObj;
import com.trustsim.evaluator.tables.OneDimTable;
import com.trustsim.evaluator.tables.TwoDimTable;
import com.trustsim.storage.SQLiteDatabaseManager;
import com.trustsim.synthesiser.AgentSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Abstract Class implements all shared functionality of Evaluator and CalculatorEvaluator
 * Controller classes.
 */
public abstract class EvaluatorBaseController implements Initializable {

    private final String sqlTableName = "DEFAULT";

    protected final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
    protected GraphDataObj selectedAllAgentSystemGraph = null;
    protected AgentTypeWrapper selectedGraphAgentType = null;

    @FXML protected MenuItem toMenu;
    @FXML protected MenuItem toAbout;
    @FXML protected Button saveAsXMLButton;
    @FXML protected Button removeButton;
    @FXML protected TableView tableView;
    @FXML protected TableColumn simulationNameColumn;
    @FXML protected AnchorPane tabPaneParent;
    @FXML protected HBox individualTableContainerHBox;
    @FXML protected ComboBox simulationNumberGraphSelectorComboBox;
    @FXML protected ComboBox simulationNumberTableSelectorComboBox;
    @FXML protected Stage stage;

    protected SimulationNumber selectedAgentSystemSimulationNumber = null;
    protected TableDataObj selectedAllAgentSystemTable = null;
    protected ObservableList<GroupedAgentSystem> tableData;
    protected GroupedAgentSystem selectedSimulationResult = null;

    public abstract void initialize(URL location, ResourceBundle resources);

    /**
     * Function initialises Evaluator table view to display AgentSystem data objects.
     */
    protected void initialiseTableView() {
        tableView.setEditable(false);
        simulationNameColumn.setCellValueFactory(
            new PropertyValueFactory<AgentSystem, String>("systemName"));
        simulationNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        simulationNameColumn.setOnEditCommit(
            (EventHandler<CellEditEvent<AgentSystem, String>>)
                t ->
                    t.getTableView()
                        .getItems()
                        .get(t.getTablePosition().getRow())
                        .setSystemName(t.getNewValue()));

        tableView.getColumns().setAll(simulationNameColumn);
        tableView.setPrefWidth(450);
        tableView.setPrefHeight(300);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getSelectionModel().select(0);

        tableView
            .getSelectionModel()
            .selectedIndexProperty()
            .addListener(new currentGroupedAgentSystemListener());
    }

    /**
     * Function refreshes the contents of the table view by refetching the Agent System objects
     * and then adding them to the table view.
     */
    public void refreshTableView(String sqlTableName) {
        tableData = getGroupedSavedSystems(sqlTableName);
        tableView.setItems(tableData);
    }

    /**
     * Function used for clearing all simulation results data from the SQLite database.
     *
     * @param event stores data related to caller of event
     */
    public void deleteAllEvaluatorItems(ActionEvent event) {
        sqLiteDatabaseManager.removeAllObjectsFromTable(sqlTableName);
    }

    /**
     * Function adds event listeners to Agent System action buttons, for exporting to JSON and removing
     * Agent System object from database.
     */
    protected void initialiseAddRemoveButtons(String sqlTableName) {
        saveAsXMLButton.setOnAction(new buttonEventListener("toJSON", sqlTableName));
        removeButton.setOnAction(new buttonEventListener("remove", sqlTableName));
    }

    /**
     * Function initialises the graph selector combo box with GraphDataObj objects that store the dataset
     * to be plotted on the graph pane whenever that combo box item is selected.
     */
    protected abstract void initializeGraphComboBox();

    /**
     * Function re-renders the graph pane, which is called whenever the dataset which the graph pane
     * is required to display has changed.
     */
    protected abstract void refreshEvaluatorGraphPane();

    /**
     * Function re-renders the table pane, which is called whenever the dataset which the table pane
     * is required to display has changed.
     */
    protected abstract void refreshEvaluatorTablePane();

    /**
     * Function re-renders the simulation results pane, which is called whenever the dataset
     * which the results pane is required to display has changed. Results pane is the parent of
     * graph pane and table pane.
     */
    protected void refreshResultView() {
        if (selectedSimulationResult != null) {
            tabPaneParent.setVisible(true);
            initializeSimulationNumberComboBox();
            initializeAgentTypeComboBox();
            initializeGraphComboBox();
            initializeTableComboBox();
        } else {
            tabPaneParent.setVisible(false);
        }
    }

    /**
     * Function re-renders the simulation information pane to display the setting used to run the
     * currently selected group or one or more simulation results objects (AgentSystem objects).
     */
    protected abstract void refreshSystemInfoPane();

    /**
     * Function initialises combo box used to select which agent of the dataset should be displayed
     * on the table and graph views, based on the number of agents in the dataset.
     */
    protected abstract void initializeAgentTypeComboBox();

    /**
     * Function initialises the table selector combo box with TableDataObj objects that store the dataset
     * to be plotted on the table pane whenever that combo box item is selected.
     */
    protected abstract void initializeTableComboBox();

    /**
     * Function re-renders the contents of a individual simulation results data graph pane view,
     * based on the type of table that is specified in the GraphDataObj object.
     */
    protected abstract void reloadIndividualGraphDataView();

    /**
     * Function re-renders the contents of a individual simulation results data table pane view,
     * based on the type of table that is specified in the TableDataObj object.
     */
    protected void reloadIndividualTableDataView() {

        individualTableContainerHBox.getChildren().clear();
        if (selectedSimulationResult != null && selectedGraphAgentType != null && selectedAllAgentSystemGraph != null && selectedAgentSystemSimulationNumber != null) {
            switch (selectedAllAgentSystemTable.getTableType()) {
                case OneDimTable -> {
                    individualTableContainerHBox.getChildren().add(new OneDimTable(selectedAllAgentSystemTable, selectedAgentSystemSimulationNumber.getNumber()));
                }
                case TwoDimTable -> {
                    individualTableContainerHBox.getChildren().add(new TwoDimTable(selectedAllAgentSystemTable, selectedAgentSystemSimulationNumber.getNumber(), "EigenTrustModel"));
                }
            }
        }
    }

    /**
     * Function called when the combo box that specifies which simulation number that the table
     * and graph panes should display data for, and then reloads the corresponding graph and table
     * pane with the newly selected data.
     *
     * @param event stores data related to caller of event
     */
    public void comboBoxSimulationNumberAction(ActionEvent event) {
        if (((ComboBox) event.getSource()).getValue() != null) {
            selectedAgentSystemSimulationNumber = (SimulationNumber) ((ComboBox) event.getSource()).getValue();

            reloadIndividualTableDataView();
            reloadIndividualGraphDataView();
        }
    }

    /**
     * Function initialises combo box used to select which simulation number of the dataset should
     * be displayed on the table and graph views, based on the number of simulations in the dataset.
     */
    private void initializeSimulationNumberComboBox() {

        List<SimulationNumber> outputFormatList = new ArrayList<>();
        if (selectedSimulationResult != null) {
            for  (int i = 0; i < selectedSimulationResult.getNumberOfSimulations(); i++) {
                outputFormatList.add(new SimulationNumber(i));
            }
        }
        ObservableList<SimulationNumber> simulationNumberData = FXCollections
            .observableList(outputFormatList);

        simulationNumberGraphSelectorComboBox.getItems().clear();
        simulationNumberGraphSelectorComboBox.getItems().addAll(simulationNumberData);
        simulationNumberGraphSelectorComboBox.getSelectionModel().selectFirst();
        simulationNumberGraphSelectorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                selectedAgentSystemSimulationNumber = (SimulationNumber) newValue;
                reloadIndividualGraphDataView();
            }
        );

        simulationNumberTableSelectorComboBox.getItems().clear();
        simulationNumberTableSelectorComboBox.getItems().addAll(simulationNumberData);
        simulationNumberTableSelectorComboBox.getSelectionModel().selectFirst();
        simulationNumberTableSelectorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                selectedAgentSystemSimulationNumber = (SimulationNumber) newValue;
                reloadIndividualTableDataView();
            }
        );

    }

    /**
     * Function retrieves the Agent System objects stored in the simulationResults SQLite database,
     * then groups them into GroupedAgentSystem objects (that store multiple Agent Systems which took
     * place under identical simulation conditions, but using different trust models), and then
     * returns these grouped objects as a sorted list.
     *
     * @return sorted list of grouped agent system objects
     */
    private SortedList<GroupedAgentSystem> getGroupedSavedSystems(String sqlTableName) {

        List<AgentSystem> agentSystems = sqLiteDatabaseManager.retrieveAllSystemObjectsTableData(sqlTableName);

        if (agentSystems.size() < 1) return new SortedList<>(FXCollections.observableArrayList(),
            Comparator.comparing(GroupedAgentSystem::toString));

        ObservableList<GroupedAgentSystem> groupedAgentSystems = FXCollections.observableArrayList();

        List<GroupedAgentSystem> currentGroups = new ArrayList<>();

        for (AgentSystem currentSystem : agentSystems) {

            boolean isFound = false;
            for (GroupedAgentSystem currentGroup : currentGroups) {
                if (currentSystem.getSystemName().equals(currentGroup.getSystemName())) {
                    currentGroup.addAgentSystem(currentSystem, currentSystem.getTrustModel().toString());
                    isFound = true;
                }
            }
            if (!isFound) {
                GroupedAgentSystem newGroupedAgentSystem = new GroupedAgentSystem(currentSystem, currentSystem.getTrustModel().toString());
                currentGroups.add(newGroupedAgentSystem);
            }

        }

        groupedAgentSystems.addAll(currentGroups);

        return new SortedList<>(groupedAgentSystems, Comparator.comparing(GroupedAgentSystem::toString));
    }

    /**
     * Change Listener object implementation which can be instantiated to listen for when the
     * currently selected agent system has changed.
     */
    protected class currentGroupedAgentSystemListener implements ChangeListener<Number> {

        /**
         * Function overrides ChangeListener interface function to determine what actions are taken
         * when the currently selected grouped agent system object changes.
         * @param observable the tableView object being observed by this change listener
         * @param oldValue the previously selected grouped agent system object
         * @param newValue the newly selected grouped agent system object
         */
        @Override
        public void changed(
            ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            int selectedRow = newValue.intValue();

            if (0 <= selectedRow && selectedRow <= tableData.size()) {
                selectedSimulationResult = tableData.get(selectedRow);
                refreshEvaluatorGraphPane();
                refreshEvaluatorTablePane();
                refreshResultView();
                refreshSystemInfoPane();
            } else {
                selectedSimulationResult = null;
            }
        }
    }

    /**
     * Event Listener object implementation which can be instantiated to listen for when one of the
     * action buttons, used to remove a Grouped Agent System or export a grouped agent system to JSON
     * is invoked in the view.
     */
    protected class buttonEventListener implements EventHandler<ActionEvent> {

        private final String buttonEventType;
        private final String sqlTableName;

        public buttonEventListener(String eventType, String sqlTableName) {
            this.buttonEventType = eventType;
            this.sqlTableName = sqlTableName;
        }

        /**
         * Function overrides EventHandler interface function to determine what actions are taken
         * when one of the action buttons is pressed.
         * @param event stores data related to caller of event
         */
        @Override
        public void handle(ActionEvent event) {

            switch (buttonEventType) {
                case "remove" -> {
                    int selectedRow = tableView.getSelectionModel().getSelectedIndex();
                    GroupedAgentSystem systemGroupBeingDeleted = (GroupedAgentSystem) tableView.getSelectionModel().getSelectedItem();
                    Map<String, AgentSystem> systemsBeingDeleted = systemGroupBeingDeleted.getAgentSystems();
                    for (AgentSystem systemBeingDeleted : systemsBeingDeleted.values()) {
                        String systemBeingDeletedName = systemBeingDeleted.getSystemName() + systemBeingDeleted.getTrustModel().toString();
                        sqLiteDatabaseManager
                            .removeObjectFromTable(sqlTableName, systemBeingDeletedName);
                    }
                    refreshTableView(sqlTableName);
                    refreshEvaluatorGraphPane();
                    refreshEvaluatorTablePane();
                    refreshResultView();
                    refreshSystemInfoPane();
                    tableView.requestFocus();
                    tableView.getSelectionModel().select(selectedRow == 0 ? 0 : selectedRow - 1);
                }
                case "toJSON" -> {
                    AgentSystem existingSystemConfig =
                        ((GroupedAgentSystem) tableView.getSelectionModel().getSelectedItem()).getAgentSystems().values().iterator().next();
                    String output = existingSystemConfig.toJsonString(0);
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(stage);

                    if (file != null) {
                        try (FileWriter fileWriter = new FileWriter(file)) {
                            fileWriter.write(output);
                            fileWriter.flush();

                            Alert alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("File Saved");
                            alert.setHeaderText(file.getName() + " has been saved");
                            alert.showAndWait();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Function used externally to set the stage object that this evaluator controller's view
     * is displayed within.
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

        TrustSimSceneController sceneController = TrustSimSceneController.getInstance(null);
        try {
            if (event.getSource().equals(toMenu)) {
                sceneController.changeScene("Home");
            } else if (event.getSource().equals(toAbout)) {
                sceneController.changeScene("About");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
