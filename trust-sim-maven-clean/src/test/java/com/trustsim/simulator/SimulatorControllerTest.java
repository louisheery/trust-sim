package com.trustsim.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.ComboBoxMatchers.hasItems;
import static org.testfx.matcher.control.ComboBoxMatchers.hasSelectedItem;

import com.trustsim.HomeMockView;
import com.trustsim.TrustSim;
import com.trustsim.TrustSimAbstractTest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Window;
import junitparams.JUnitParamsRunner;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

@RunWith(JUnitParamsRunner.class)
class SimulatorControllerTest extends TrustSimAbstractTest {

  protected ComboBox outputFormatComboBox;


  @BeforeEach
  public void beforeEachTest() throws Exception {
    FxToolkit.setupApplication(TrustSim.class);
    HomeMockView homeMockView = new HomeMockView(this);
    homeMockView.clickScreenButton("#simulatorButton");
  }

  @AfterEach
  public void afterEachTest() throws Exception {
    FxToolkit.hideStage();
    release(new KeyCode[] {});
    release(new MouseButton[] {});
  }

  @Test
  void initialisedSceneTest() {

    verifyThat("#outputFormatComboBox", hasItems(1));
    verifyThat("#outputFormatComboBox", hasSelectedItem("Visualisation & JSON"));

    outputFormatComboBox = lookup("#outputFormatComboBox").queryComboBox();
    ObservableList comboBoxItems = outputFormatComboBox.getItems();
    assertTrue(comboBoxItems.contains("Visualisation & JSON"));
  }

  @Test
  void startSimulationJSONSuccessfulTest() {
    clickOn("#startSimulationButton");
    checkAllFXEventsFinished();
    sleep(4000);
    checkAlertContent("Simulation Results can be Analysed in the Evaluator", "");
  }

  @Test
  void startContinuousSimulationJSONSuccessfulTest() {
    clickOn("#simulationEventTypeComboBox");
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    clickOn("#startSimulationButton");
    checkAllFXEventsFinished();
    sleep(4000);
    checkAlertContent("Simulation Results can be Analysed in the Evaluator", "");
  }

  @Test
  void startSimulationCassandraSuccessfulTest() {

    clickOn("#outputFormatComboBox");
    type(KeyCode.DOWN);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    clickOn("#startSimulationButton");

  }

  @After
  public void tearDown() throws Exception {
    FxToolkit.hideStage();
    release(new KeyCode[] {});
    release(new MouseButton[] {});
  }

  public void checkStageContent() throws IOException {
    final javafx.stage.Stage actualAlertDialog = getTopModalStage();
    assertNotNull(actualAlertDialog);
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
        allWindows.stream()
            .filter(window -> window instanceof Stage)

            .findFirst()
            .orElse(null);
  }
}
