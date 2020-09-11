package com.trustsim.importer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.trustsim.HomeMockView;
import com.trustsim.TrustSim;
import com.trustsim.TrustSimAbstractTest;
import com.trustsim.storage.SQLiteDatabaseManager;
import com.trustsim.synthesiser.AgentSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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
class ImporterControllerTest extends TrustSimAbstractTest {

  private static final SQLiteDatabaseManager sqLiteDatabaseManager =
      SQLiteDatabaseManager.getInstance();
  private static String TEST_SYSTEM_NAME;
  private static AgentSystem TEST_AGENT_SYSTEM;
  private HomeMockView homeMockView;

  @BeforeAll
  public static void startUp() {
    TEST_AGENT_SYSTEM =
        sqLiteDatabaseManager.retrieveAllSystemObjectsTableData("simulationResults").get(0);
    TEST_SYSTEM_NAME = TEST_AGENT_SYSTEM.getSystemName();
  }

  @AfterAll
  public static void turnOff() {
    sqLiteDatabaseManager.addObjectToTable(
        "simulationResults", TEST_SYSTEM_NAME, TEST_AGENT_SYSTEM);
  }

  @BeforeEach
  public void beforeEachTest() throws Exception {
    FxToolkit.setupApplication(TrustSim.class);
    homeMockView = new HomeMockView(this);
    homeMockView.clickScreenButton("#importerButton");
  }

  @AfterEach
  public void afterEachTest() throws Exception {
    FxToolkit.hideStage();
    release(new KeyCode[] {});
    release(new MouseButton[] {});
  }

  @Test
  public void importerTest() {
    clickOn("Initialise Graph");
    sleep(20000);

    clickOn("Import Graph");
    sleep(20000);
    checkAlertContent("Simulation Results can be Analysed in the Evaluator", "");
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
}
