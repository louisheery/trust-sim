package com.trustsim.synthesiser;

import com.trustsim.simulator.storage.SQLiteDatabaseManager;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SynthesiserController implements Initializable {

  public MenuItem toMenu;
  public Button addSystemButton;
  public Button removeSystemButton;
  public VBox systemInformationBox;
  public Text systemNameText;
  public TextField numberOfServiceRequestsInput;
  public TextField parameter4Text;
  public TextField numberOfConsumersInput;
  public TextField numberOfProducersInput;
  public Button parameterSaveButton;

  @FXML
  Pane homePane, synthesiserPane;
  public Stage stage;

  @FXML
  private TableView tableView;
  @FXML
  private TableColumn systemNameColumn;
//  @FXML
//  private TableColumn systemDateColumn;

  private ObservableList<AgentSystem> tableData;
  private final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();

  private AgentSystem selectedAgentSystem = null;

  //XStreamManager xStreamManager = new XStreamManager();

  @Override
  public void initialize(URL location, ResourceBundle resources) {


    tableView.setEditable(false);

    systemNameColumn.setCellValueFactory(new PropertyValueFactory<AgentSystem, String>("systemName"));
    systemNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    systemNameColumn.setOnEditCommit(
        (EventHandler<CellEditEvent<AgentSystem, String>>) t -> ((AgentSystem) t.getTableView().getItems().get(
            t.getTablePosition().getRow())
        ).setSystemName(t.getNewValue()));


//    systemDateColumn.setCellValueFactory(new PropertyValueFactory<AgentSystem, String>("dateCreated"));
//    systemDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//    systemDateColumn.setOnEditCommit(
//        (EventHandler<CellEditEvent<AgentSystem, String>>) t -> ((AgentSystem) t.getTableView().getItems().get(
//            t.getTablePosition().getRow())
//        ).setDateCreated(t.getNewValue()));

    tableView.getColumns().setAll(systemNameColumn);
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
        AgentSystem newSystem = new AgentSystem(systemName);

        List<Integer> itemData = systemParametersToList(newSystem);

        boolean didAddItem = sqLiteDatabaseManager.addToTable("agentSystems", systemName, itemData);

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


        systemBeingEdited.setNumberOfConsumersParameter(Integer.parseInt(numberOfConsumersInput.getText()));
        systemBeingEdited.setNumberOfProducersParameter(Integer.parseInt(numberOfProducersInput.getText()));
        systemBeingEdited.setNumberOfServiceRequests(Integer.parseInt(numberOfServiceRequestsInput.getText()));
        systemBeingEdited.setParameter4Value(Integer.parseInt(parameter4Text.getText()));

        sqLiteDatabaseManager.removeFromTable("agentSystems", systemBeingEdited.getSystemName());

//        String agentSystemXML = null;
//        try {
//          agentSystemXML = xStreamManager.encodeToXML(systemBeingEdited);
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
        List<Integer> itemDataEditing = systemParametersToList(systemBeingEdited);

        sqLiteDatabaseManager.addToTable("agentSystems", systemBeingEdited.getSystemName(), itemDataEditing);

        reloadSystemInformationPane();
      }


    }

    private List<Integer> systemParametersToList(AgentSystem newSystem) {
      List<Integer> itemData = new ArrayList<>();
      itemData.add(newSystem.getNumberOfConsumersParameter());
      itemData.add(newSystem.getNumberOfProducersParameter());
      itemData.add(newSystem.getNumberOfServiceRequests());
      return itemData;
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
      systemInformationBox.setVisible(true);
      systemNameText.setText(selectedAgentSystem.getSystemName());
      numberOfConsumersInput.setText(selectedAgentSystem.getNumberOfConsumersParameter().toString());
      numberOfProducersInput.setText(selectedAgentSystem.getNumberOfProducersParameter().toString());
      numberOfServiceRequestsInput.setText(selectedAgentSystem.getNumberOfServiceRequests().toString());
      parameter4Text.setText(selectedAgentSystem.getParameter4().toString());
    } else {
      systemInformationBox.setVisible(false);
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
