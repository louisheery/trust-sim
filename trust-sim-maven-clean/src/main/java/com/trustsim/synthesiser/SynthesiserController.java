package com.trustsim.synthesiser;

import com.trustsim.TrustSimSceneController;
import com.trustsim.simulator.trustmodel.TrustModelEnum;
import com.trustsim.storage.SQLiteDatabaseManager;
import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class is Controller of the Synthesiser Scene of application.
 */
public class SynthesiserController implements Initializable {

  @FXML private Button addSystemButton;
  @FXML private Button removeSystemButton;
  @FXML private Text selectSystemPromptTextField;
  @FXML private MenuItem toMenu;
  @FXML private MenuItem toAbout;
  @FXML private HBox trustSimLogoHBox;
  @FXML private HBox systemPaneContainerHBox;
  @FXML private ImageView trustSimLogoView;
  @FXML private TableView tableView;
  @FXML private TableColumn systemNameColumn;
  @FXML private Stage stage;

  private AgentSystem selectedAgentSystem = null;
  private ObservableList<AgentSystem> tableData;
  private final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();



  /**
   * Function initialises controller for Synthesiser screen, by loading dataset from database
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

    tableView.setEditable(false);

    systemNameColumn.setCellValueFactory(
        new PropertyValueFactory<AgentSystem, String>("systemName"));
    systemNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    systemNameColumn.setOnEditCommit(
        (EventHandler<CellEditEvent<AgentSystem, String>>)
            t ->
                t.getTableView()
                    .getItems()
                    .get(t.getTablePosition().getRow())
                    .setSystemName(t.getNewValue()));

    tableView.getColumns().setAll(systemNameColumn);
    tableView.setPrefWidth(450);
    tableView.setPrefHeight(300);
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tableView.getSelectionModel().select(0);

    tableView
        .getSelectionModel()
        .selectedIndexProperty()
        .addListener(new currentAgentSystemListener());

    setupEditButtons();
    reloadTableView();
    reloadSystemInformationPane();
    selectSystemPromptTextField.setVisible(true);
  }

  private void setupEditButtons() {
    addSystemButton.setOnAction(new buttonEventListener("add"));
    removeSystemButton.setOnAction(new buttonEventListener("remove"));
  }

  public void deleteAllSynthesiserItems(ActionEvent event) {
    sqLiteDatabaseManager.removeAllObjectsFromTable("agentSystems");
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

  /**
   * Event Listener object implementation which can be instantiated to listen for when one of the
   * action buttons, used to add or remove an Agent System object is invoked in the view.
   */
  private class buttonEventListener implements EventHandler<ActionEvent> {

    private final String buttonEventType;

    public buttonEventListener(String eventType) {
      this.buttonEventType = eventType;
    }

    /**
     * Function overrides EventHandler interface function to determine what actions are taken
     * when one of the action buttons is pressed.
     * @param event stores data related to caller of event
     */
    @Override
    public void handle(ActionEvent event) {

      switch (buttonEventType) {
        case "add" -> {
          String systemName = addSystemPopup();
          AgentSystem newSystem = new AgentSystem(systemName);
          newSystem.setTrustModel(TrustModelEnum.EigenTrustModel);

          boolean didAddItem =
              sqLiteDatabaseManager.addObjectToTable("agentSystems", systemName, newSystem);
          if (!didAddItem) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("A Agent System with this Name Already Exists");
            alert.showAndWait();
          }
          reloadTableView();
          reloadSystemInformationPane();
        }
        case "remove" -> {
          int selectedRow = tableView.getSelectionModel().getSelectedIndex();
          AgentSystem systemBeingDeleted =
              (AgentSystem) tableView.getSelectionModel().getSelectedItem();
          String systemBeingDeletedName = systemBeingDeleted.getSystemName();
          sqLiteDatabaseManager.removeObjectFromTable("agentSystems", systemBeingDeletedName);
          reloadTableView();
          tableView.getSelectionModel().select(selectedRow == 0 ? 0 : selectedRow - 1);
          reloadSystemInformationPane();
          tableView.requestFocus();
        }
      }
    }
  }

  /**
   * Function dispatches a dialog box used to specify the name of a new agent system which
   * should be created.
   *
   * @return name of agent system to be created
   */
  private String addSystemPopup() {

    String agentSystemName = null;

    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Add Agent System");
    dialog.setHeaderText("Enter Agent System Name");

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      agentSystemName = result.get();
    }

    return agentSystemName;
  }

  /**
   * Function used to re-render the information displayed in the JavaFX Pane of this class, which
   * will fetch the currently selected AgentSystem and then initialise the Pane's
   * child nodes with the agent system parameters stored within this member variable.
   */
  private void reloadSystemInformationPane() {

    if (selectedAgentSystem != null) {

      switch (selectedAgentSystem.getTrustModelName()) {

        case "EigenTrustModel" -> {
          systemPaneContainerHBox.getChildren().clear();
          AgentSystemPane agentSystemPane = new AgentSystemPane(selectedAgentSystem, stage);
          agentSystemPane.setStage(stage);
          systemPaneContainerHBox.getChildren().add(agentSystemPane);
        }

        case "DynamicTrustModel" -> {
          systemPaneContainerHBox.getChildren().clear();
          AgentSystemPane agentSystemPane = new AgentSystemPane(selectedAgentSystem, stage);
          agentSystemPane.setStage(stage);
          systemPaneContainerHBox.getChildren().add(agentSystemPane);
        }
        case "unassigned" -> {
          systemPaneContainerHBox.getChildren().clear();
          AgentSystemPane agentSystemPane = new AgentSystemPane(selectedAgentSystem, stage);
          agentSystemPane.setStage(stage);
          systemPaneContainerHBox.getChildren().add(agentSystemPane);
        }
      }

    } else {
      selectSystemPromptTextField.setVisible(true);
    }
  }


  /**
   * Function used to fetch the current agent system objects stored within the SQLite database
   * and then re-render them in the Table View JavaFX node.
   */
  public void reloadTableView() {
    tableData = getSavedSystems();
    tableView.setItems(tableData);
  }

  /**
   * Function used to obtain a list of Agent System objects from SQLite database.
   *
   * @return list of Agent System objects
   */
  private SortedList<AgentSystem> getSavedSystems() {
    return new SortedList<>(sqLiteDatabaseManager.retrieveAllSystemObjectsTableData("agentSystems"),
        Comparator.comparing(AgentSystem::toString));
  }

  /**
   * Change Listener object implementation which can be instantiated to listen for when the
   * currently selected agent system has changed.
   */
  private class currentAgentSystemListener implements ChangeListener<Number> {

    /**
     * Function overrides ChangeListener interface function to determine what actions are taken
     * when the currently selected agent system object changes.
     * @param observable the tableView object being observed by this change listener
     * @param oldValue the previously selected grouped agent system object
     * @param newValue the newly selected grouped agent system object
     */
    @Override
    public void changed(
        ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

      int selectedRow = newValue.intValue();

      if (0 <= selectedRow && selectedRow <= tableData.size()) {
        selectedAgentSystem = tableData.get(selectedRow);
        reloadSystemInformationPane();
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
}
