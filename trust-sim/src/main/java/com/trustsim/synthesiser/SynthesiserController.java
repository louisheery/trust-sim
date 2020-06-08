package com.trustsim.synthesiser;

import com.trustsim.simulator.agents.Graph;
import com.trustsim.simulator.storage.SQLiteDatabaseManager;
import com.trustsim.simulator.storage.XStreamManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class SynthesiserController implements Initializable {

  public MenuItem toMenu;
  public Button addSystemButton;
  public Button removeSystemButton;
  public VBox systemInformationBox;
  public Text systemNameText;
  public TextField parameter3Text;
  public TextField parameter4Text;
  public TextField parameter1Text;
  public TextField parameter2Text;
  public Button parameterSaveButton;

  @FXML
  Pane homePane, synthesiserPane;
  public Stage stage;

  @FXML
  private TableView tableView;
  @FXML
  private TableColumn systemNameColumn;
  @FXML
  private TableColumn systemDateColumn;

  private ObservableList<AgentSystem> tableData;
  private final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();

  private AgentSystem selectedAgentSystem = null;

  XStreamManager xStreamManager = new XStreamManager();

  @Override
  public void initialize(URL location, ResourceBundle resources) {


    tableView.setEditable(false);

    systemNameColumn.setCellValueFactory(new PropertyValueFactory<AgentSystem, String>("systemName"));
    systemNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    systemNameColumn.setOnEditCommit(
        (EventHandler<CellEditEvent<AgentSystem, String>>) t -> ((AgentSystem) t.getTableView().getItems().get(
            t.getTablePosition().getRow())
        ).setSystemName(t.getNewValue()));


    systemDateColumn.setCellValueFactory(new PropertyValueFactory<AgentSystem, String>("dateCreated"));
    systemDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    systemDateColumn.setOnEditCommit(
        (EventHandler<CellEditEvent<AgentSystem, String>>) t -> ((AgentSystem) t.getTableView().getItems().get(
            t.getTablePosition().getRow())
        ).setDateCreated(t.getNewValue()));

    tableView.getColumns().setAll(systemNameColumn, systemDateColumn);
    tableView.setPrefWidth(450);
    tableView.setPrefHeight(300);
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tableView.getSelectionModel().select(0);

    tableView.getSelectionModel().selectedIndexProperty().addListener(new currentAgentSystemListener());

    //tableView.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());

    setupEditButtons();
    reloadTableView();
    reloadSystemInformationPane();
  }

  private void setupEditButtons() {
    addSystemButton.setOnAction(new buttonEventListener("add"));
    removeSystemButton.setOnAction(new buttonEventListener("remove"));
    parameterSaveButton.setOnAction(new buttonEventListener("save"));

  }

  private class buttonEventListener implements EventHandler<ActionEvent> {

    private String buttonEventType;

    public buttonEventListener(String eventType) {
      this.buttonEventType = eventType;
    }

    @Override
    public void handle(ActionEvent event) {

      if (buttonEventType.equals("add")) {
        String systemName = addSystemPopup();
        Graph blankGraph = new Graph();
        String currentDate = LocalDate.now().toString();
        AgentSystem newSystem = new AgentSystem(systemName, currentDate, blankGraph);
        String agentSystemXML = null;
        try {
          agentSystemXML = xStreamManager.encodeToXML(newSystem);
        } catch (IOException e) {
          e.printStackTrace();
        }
        boolean didAddItem = sqLiteDatabaseManager.addToTable("agentSystems", systemName, agentSystemXML);

        if (!didAddItem) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("A Agent System with this Name Already Exists");
          alert.showAndWait();
        }

        reloadTableView();
      } else if (buttonEventType.equals("remove")) {
        int selectedRow = tableView.getSelectionModel().getSelectedIndex();
        AgentSystem systemBeingDeleted = (AgentSystem) tableView.getSelectionModel().getSelectedItem();
        String systemBeingDeletedName = systemBeingDeleted.getSystemName();
        sqLiteDatabaseManager.removeFromTable("agentSystems", systemBeingDeletedName);
        tableData.remove(selectedRow);

        tableView.requestFocus();
        tableView.getSelectionModel().select(selectedRow == 0 ? 0 : selectedRow - 1);
      } else {
        AgentSystem systemBeingEdited = (AgentSystem) tableView.getSelectionModel().getSelectedItem();
        systemBeingEdited.setParameter1Value(parameter1Text.getText() == null ? "WASNULLVALUE" : parameter1Text.getText());
        systemBeingEdited.setParameter2Value(parameter2Text.getText() == null ? "WASNULLVALUE" : parameter2Text.getText());
        systemBeingEdited.setParameter3Value(parameter3Text.getText() == null ? "WASNULLVALUE" : parameter3Text.getText());
        systemBeingEdited.setParameter4Value(parameter4Text.getText() == null ? "WASNULLVALUE" : parameter4Text.getText());
        String systemBeingEditedName = systemBeingEdited.getSystemName();
        sqLiteDatabaseManager.removeFromTable("agentSystems", systemBeingEditedName);

        String agentSystemXML = null;
        try {
          agentSystemXML = xStreamManager.encodeToXML(systemBeingEdited);
          System.out.println("12312as");
        } catch (IOException e) {
          e.printStackTrace();
        }
        sqLiteDatabaseManager.addToTable("agentSystems", systemBeingEditedName, agentSystemXML);

        reloadSystemInformationPane();
      }


    }
  }

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

  private void reloadSystemInformationPane() {

    if (selectedAgentSystem != null) {
      systemNameText.setText(selectedAgentSystem.getSystemName());
      parameter1Text.setText(selectedAgentSystem.getParameter1());
      parameter2Text.setText(selectedAgentSystem.getParameter2());
      parameter3Text.setText(selectedAgentSystem.getParameter3());
      parameter4Text.setText(selectedAgentSystem.getParameter4());
    }
  }

  @FXML
  public void toMainMenu(Event event) throws IOException {
    stage = (Stage) synthesiserPane.getScene().getWindow();
    homePane = FXMLLoader.load(getClass().getResource("/Home.fxml"));
    Scene scene = new Scene(homePane);
    stage.setScene(scene);
    stage.setTitle("Home - TrustSim");
    stage.show();
  }

  public void reloadTableView() {
    tableData = getSavedSystems();
    tableView.setItems(tableData);
  }

  private ObservableList<AgentSystem> getSavedSystems() {
    return sqLiteDatabaseManager.retrieveSystemTableData("agentSystems");
  }

  private class currentAgentSystemListener implements ChangeListener<Number> {
    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

      int selectedRow = newValue.intValue();

      if (0 <= selectedRow && selectedRow <= tableData.size()) {
        selectedAgentSystem = tableData.get(selectedRow);
        reloadSystemInformationPane();
      }
    }
  }
}
