package com.trustsim.evaluator;

import static com.trustsim.evaluator.GraphTypeEnum.AreaGraph;
import static com.trustsim.evaluator.GraphTypeEnum.GlobalTrustGraph;
import static com.trustsim.evaluator.GraphTypeEnum.ScatterGraph;
import static com.trustsim.evaluator.TableTypeEnum.OneDimTable;
import static com.trustsim.evaluator.TableTypeEnum.TwoDimTable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.trustsim.HomeMockView;
import com.trustsim.TrustSim;
import com.trustsim.TrustSimAbstractTest;
import com.trustsim.evaluator.charts.ActualVsPredictedTrustValueChartPane;
import com.trustsim.evaluator.charts.AgentGlobalTrustChartPane;
import com.trustsim.evaluator.charts.TransactionHistoryChartPane;
import com.trustsim.evaluator.data.GraphDataObj;
import com.trustsim.evaluator.data.TableDataObj;
import com.trustsim.evaluator.tables.OneDimTable;
import com.trustsim.storage.SQLiteDatabaseManager;
import com.trustsim.synthesiser.AgentSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import junitparams.JUnitParamsRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

@RunWith(JUnitParamsRunner.class)
class EvaluatorControllerTest extends TrustSimAbstractTest {

  private static final SQLiteDatabaseManager sqLiteDatabaseManager =
      SQLiteDatabaseManager.getInstance();
  private static String TEST_GROUP_NAME;
  private static AgentSystem TEST_AGENT_SYSTEM_1;
  private static AgentSystem TEST_AGENT_SYSTEM_2;

  @BeforeAll
  public static void startUp() {

    List<AgentSystem> allAgentSystems =
        sqLiteDatabaseManager.retrieveAllSystemObjectsTableData("simulationResults");

    TEST_AGENT_SYSTEM_1 = allAgentSystems.get(0);
    GroupedAgentSystem TEST_GROUP_SYSTEM =
        new GroupedAgentSystem(TEST_AGENT_SYSTEM_1, TEST_AGENT_SYSTEM_1.getTrustModel().toString());
    for (int i = 1; i < allAgentSystems.size(); i++) {
      if (allAgentSystems.get(i).getSystemName().equals(allAgentSystems.get(0).getSystemName())) {
        TEST_AGENT_SYSTEM_2 = allAgentSystems.get(i);
        TEST_GROUP_SYSTEM.addAgentSystem(
            TEST_AGENT_SYSTEM_2, TEST_AGENT_SYSTEM_2.getTrustModel().toString());
        break;
      }
    }

    TEST_GROUP_NAME = TEST_GROUP_SYSTEM.getSystemName();
  }

  @AfterAll
  public static void turnOff() {
    sqLiteDatabaseManager.addObjectToTable(
        "simulationResults",
        TEST_AGENT_SYSTEM_1.getSystemName() + TEST_AGENT_SYSTEM_2.getTrustModel().toString(),
        TEST_AGENT_SYSTEM_1);
    sqLiteDatabaseManager.addObjectToTable(
        "simulationResults",
        TEST_AGENT_SYSTEM_2.getSystemName() + TEST_AGENT_SYSTEM_2.getTrustModel().toString(),
        TEST_AGENT_SYSTEM_2);
  }

  @BeforeEach
  public void beforeEachTest() throws Exception {
    FxToolkit.setupApplication(TrustSim.class);
    HomeMockView homeMockView = new HomeMockView(this);
    homeMockView.clickScreenButton("#evaluatorButton");
  }

  @AfterEach
  public void afterEachTest() throws Exception {
    FxToolkit.hideStage();
    release(new KeyCode[] {});
    release(new MouseButton[] {});
  }

  @Test
  public void checkReloadTableDataView() {
    ComboBox<TableDataObj> comboBox = findElement("#tableSelectorComboBox");
    HBox tableContainerHBox = findElement("#tableContainerHBox");

    ObservableList<Node> comboBoxItems = comboBox.getChildrenUnmodifiable();

    clickOn("#allSimulationTableViewTab");
    checkAllFXEventsFinished();
    sleep(1000);
    int itemIndex = 0;
    for (Node item : comboBoxItems) {
      clickOn("#tableSelectorComboBox");
      for (int i = 0; i < itemIndex; i++) {
        type(KeyCode.DOWN);
        sleep(100);
      }
      type(KeyCode.ENTER);
      sleep(100);
      if (comboBox.getItems().get(itemIndex).getTableType() == OneDimTable) {
        assertSame(tableContainerHBox.getChildren().get(0).getClass(), OneDimTable.class);
      } else if (comboBox.getItems().get(itemIndex).getTableType() == TwoDimTable) {
        assertSame(tableContainerHBox.getChildren().get(0).getClass(), OneDimTable.class);
      }
      itemIndex++;
    }
  }

  @Test
  public void checkReloadGraphDataView() {
    TableView tableView = findElement("#TableView");
    interact(
        () -> {
          tableView.getSelectionModel().selectFirst();
        });
    sleep(500);
    ComboBox<GraphDataObj> comboBox = findElement("#graphSelectorComboBox");
    HBox tableContainerHBox = findElement("#graphContainerHBox");

    ObservableList<Node> comboBoxItems = comboBox.getChildrenUnmodifiable();
    int itemIndex = 0;
    for (Node node : comboBoxItems) {
      clickOn("#allSimulationGraphViewTab");
      clickOn("#graphSelectorComboBox");
      for (int j = 0; j < itemIndex; j++) {
        type(KeyCode.DOWN);
        sleep(100);
      }
      type(KeyCode.ENTER);
      if (comboBox.getItems().get(itemIndex).getGraphType() == ScatterGraph) {
        assertSame(
            tableContainerHBox.getChildren().get(0).getClass(),
            ActualVsPredictedTrustValueChartPane.class);
      } else if (comboBox.getItems().get(itemIndex).getGraphType() == AreaGraph) {
        assertSame(
            tableContainerHBox.getChildren().get(0).getClass(), TransactionHistoryChartPane.class);
      } else if (comboBox.getItems().get(itemIndex).getGraphType() == GlobalTrustGraph) {
        assertSame(
            tableContainerHBox.getChildren().get(0).getClass(), AgentGlobalTrustChartPane.class);
      }
      itemIndex++;
    }

    for (int j = 0; j <= 2; j++) {
      clickOn("#allSimulationGraphViewTab");
      clickOn("#graphSelectorComboBox");
      for (int k = 0; k < itemIndex; k++) {
        type(KeyCode.DOWN);
        sleep(100);
      }
      type(KeyCode.ENTER);
      if (comboBox.getItems().get(j).getGraphType() == ScatterGraph) {
        assertSame(
            tableContainerHBox.getChildren().get(0).getClass(), AgentGlobalTrustChartPane.class);
      } else if (comboBox.getItems().get(j).getGraphType() == AreaGraph) {
        assertSame(
            tableContainerHBox.getChildren().get(0).getClass(), AgentGlobalTrustChartPane.class);
      } else if (comboBox.getItems().get(j).getGraphType() == GlobalTrustGraph) {
        assertSame(
            tableContainerHBox.getChildren().get(0).getClass(), AgentGlobalTrustChartPane.class);
      }
      itemIndex++;
    }
  }

  public void checkAlertContent(final String titleText, final String headerText) {
    final javafx.stage.Stage actualAlertDialog = getTopModalStage();
    assertNotNull(actualAlertDialog);

    final DialogPane alertPane = (DialogPane) actualAlertDialog.getScene().getRoot();
    assertEquals(titleText, alertPane.getHeaderText());
    assertEquals(headerText, alertPane.getContentText());
  }

  private Stage getTopModalStage() {
    FxRobot fxRobot = new FxRobot();
    // Window ordering: top @ [0] -> bottom @ [n]
    final List<Window> allWindows =
        new ArrayList<>(fxRobot.robotContext().getWindowFinder().listWindows());
    Collections.reverse(allWindows);

    return (Stage)
        allWindows.stream().filter(window -> window instanceof Stage).findFirst().orElse(null);
  }

  @Test
  public void checkSaveToJSON() {
    clickOn(TEST_GROUP_NAME);
    clickOn("Export to JSON");
    String fileName = "testfile";
    type(KeyCode.T);
    type(KeyCode.E);
    type(KeyCode.S);
    type(KeyCode.T);
    type(KeyCode.F);
    type(KeyCode.I);
    type(KeyCode.L);
    type(KeyCode.E);
    type(KeyCode.ENTER);
    // checkAlertContent(fileName + ".json has been saved", "");
  }

  @Test
  public void checkRemoveButton() {

    TableView tableView = findElement("#TableView");
    String firstItemWhichWasDeleted;

    interact(
        () -> {
          tableView.getSelectionModel().selectFirst();
        });
    sleep(500);
    firstItemWhichWasDeleted = tableView.getSelectionModel().getSelectedItem().toString();

    clickOn("Remove");

    assertFalse(
        sqLiteDatabaseManager
            .retrieveAllSystemObjectsTableData("agentSystem")
            .contains(firstItemWhichWasDeleted));
    assertNotEquals(
        firstItemWhichWasDeleted, tableView.getSelectionModel().getSelectedItem().toString());
  }
}
