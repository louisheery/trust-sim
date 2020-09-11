package com.trustsim.synthesiser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.trustsim.HomeMockView;
import com.trustsim.TrustSim;
import com.trustsim.TrustSimAbstractTest;
import com.trustsim.storage.SQLiteDatabaseManager;
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
class SynthesiserControllerTest extends TrustSimAbstractTest {

    private static final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();
    private static final String TEST_SYSTEM_NAME = "testsystem";

    @BeforeAll
    public static void startUp() {
    }

    @AfterAll
    public static void turnOff() {
    }

    @BeforeEach
    public void beforeEachTest() throws Exception {
        sqLiteDatabaseManager.removeObjectFromTable("agentSystems", "testsystem");
        FxToolkit.setupApplication(TrustSim.class);
        HomeMockView homeMockView = new HomeMockView(this);
        homeMockView.clickScreenButton("#synthesiserButton");
    }

    @AfterEach
    public void afterEachTest() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void checkAddAndModifySystem() {

        sqLiteDatabaseManager.removeObjectFromTable("agentSystems", TEST_SYSTEM_NAME);

        // Add
        clickOn("Add Trust System");
        type(KeyCode.T);
        type(KeyCode.E);
        type(KeyCode.S);
        type(KeyCode.T);
        type(KeyCode.S);
        type(KeyCode.Y);
        type(KeyCode.S);
        type(KeyCode.T);
        type(KeyCode.E);
        type(KeyCode.M);
        type(KeyCode.ENTER);

        clickOn(TEST_SYSTEM_NAME);
        type(KeyCode.TAB);
        type(KeyCode.TAB);
        type(KeyCode.TAB);
        type(KeyCode.DIGIT3);

        type(KeyCode.TAB);
        type(KeyCode.TAB);
        type(KeyCode.DIGIT4);
        type(KeyCode.DIGIT3);

        type(KeyCode.TAB);
        type(KeyCode.DIGIT4);

        type(KeyCode.TAB);
        type(KeyCode.DIGIT1);

        type(KeyCode.TAB);
        type(KeyCode.DIGIT2);

        type(KeyCode.TAB);
        type(KeyCode.DIGIT3);

        type(KeyCode.TAB);
        type(KeyCode.DIGIT4);

        type(KeyCode.TAB);
        type(KeyCode.TAB);
        type(KeyCode.TAB);
        type(KeyCode.DIGIT5);

        clickOn("Save Settings");

        // REMOVE
        clickOn(TEST_SYSTEM_NAME);
        clickOn("Remove Trust System");
    }

    @Test
    public void checkAddSystemAndRemoveSystem() {

        // Add
        sqLiteDatabaseManager.removeObjectFromTable("agentSystems", "testsystem");
        clickOn("Add Trust System");
        String fileName = "testsystem";
        type(KeyCode.T);
        type(KeyCode.E);
        type(KeyCode.S);
        type(KeyCode.T);
        type(KeyCode.S);
        type(KeyCode.Y);
        type(KeyCode.S);
        type(KeyCode.T);
        type(KeyCode.E);
        type(KeyCode.M);
        type(KeyCode.ENTER);

        assertNotNull(findElement("testsystem"));

        // REMOVE
        assertNotNull(sqLiteDatabaseManager.retrieveObjectTableData("agentSystems", TEST_SYSTEM_NAME));
        clickOn(TEST_SYSTEM_NAME);
        clickOn("Remove Trust System");
        assertNull(sqLiteDatabaseManager.retrieveObjectTableData("agentSystems", TEST_SYSTEM_NAME));
    }

    @Test
    public void checkAddSystemThatAlreadyExists() {

        // Add
        sqLiteDatabaseManager.removeObjectFromTable("agentSystems", "testsystem");
        clickOn("Add Trust System");
        String fileName = "testsystem";
        type(KeyCode.T);
        type(KeyCode.E);
        type(KeyCode.S);
        type(KeyCode.T);
        type(KeyCode.S);
        type(KeyCode.Y);
        type(KeyCode.S);
        type(KeyCode.T);
        type(KeyCode.E);
        type(KeyCode.M);
        type(KeyCode.ENTER);

        assertNotNull(findElement("testsystem"));

        // Add again
        clickOn("Add Trust System");
        type(KeyCode.T);
        type(KeyCode.E);
        type(KeyCode.S);
        type(KeyCode.T);
        type(KeyCode.S);
        type(KeyCode.Y);
        type(KeyCode.S);
        type(KeyCode.T);
        type(KeyCode.E);
        type(KeyCode.M);
        type(KeyCode.ENTER);


        checkAlertContent("A Agent System with this Name Already Exists", "");
    }

//    @Test
//    public void checkEvaluatorPanelRendersCorrectly() {
//        HBox systemInformationDataView = findElement("#systemInformationContainerHBox");
//        assertEquals(0, systemInformationDataView.getChildren().size());
//        clickOn(TEST_SYSTEM_NAME);
//        assertEquals(1, systemInformationDataView.getChildren().size());
//        assertEquals(
//            EigenSystemPaneUneditable.class, systemInformationDataView.getChildren().get(0).getClass());
//
//    }

//    @Test
//    public void checkReloadTableDataView() {
//        clickOn(TEST_SYSTEM_NAME);
//        ComboBox<TableDataObj> comboBox = findElement("#tableSelectorComboBox");
//        HBox tableContainerHBox = findElement("#tableContainerHBox");
//
//        ObservableList<Node> comboBoxItems = comboBox.getChildrenUnmodifiable();
//
//        int itemIndex = 0;
//        for (Node item : comboBoxItems) {
//            clickOn(
//                (Node) lookup(".tab-pane > .tab-header-area > .headers-region > .tab").nth(1).query());
//            clickOn("#tableSelectorComboBox");
//            for (int i = 0; i < itemIndex; i++) type(KeyCode.DOWN);
//            type(KeyCode.ENTER);
//            if (comboBox.getItems().get(itemIndex).getTableType() == ONEDTABLE) {
//                System.out.println("FIRST" + tableContainerHBox.getChildren().get(0).getClass());
//                assertTrue(tableContainerHBox.getChildren().get(0).getClass() == OneDimTable.class);
//            } else if (comboBox.getItems().get(itemIndex).getTableType() == TWODTABLE) {
//                System.out.println(comboBox.getItems().get(itemIndex).getTableType());
//                System.out.println("SECOND" + tableContainerHBox.getChildren().get(0).getClass());
//                assertTrue(tableContainerHBox.getChildren().get(0).getClass() == TwoDimTable.class);
//            }
//            itemIndex++;
//        }
//
//    }
//
//    @Test
//    public void checkReloadGraphDataView() {
//        clickOn(TEST_SYSTEM_NAME);
//        ComboBox<GraphDataObj> comboBox = findElement("#graphSelectorComboBox");
//        HBox tableContainerHBox = findElement("#graphContainerHBox");
//
//        ObservableList<Node> comboBoxItems = comboBox.getChildrenUnmodifiable();
//
//        int itemIndex = 0;
//        for (Node item : comboBoxItems) {
//            clickOn(
//                (Node) lookup(".tab-pane > .tab-header-area > .headers-region > .tab").nth(0).query());
//            clickOn("#graphSelectorComboBox");
//            for (int i = 0; i < itemIndex; i++) type(KeyCode.DOWN);
//            type(KeyCode.ENTER);
//            if (comboBox.getItems().get(itemIndex).getGraphType() == SCATTERGRAPH) {
//                assertTrue(tableContainerHBox.getChildren().get(0).getClass() == ScatterChartPane.class);
//            } else if (comboBox.getItems().get(itemIndex).getGraphType() == AREAGRAPH) {
//                System.out.println(comboBox.getItems().get(itemIndex).getGraphType());
//                assertTrue(tableContainerHBox.getChildren().get(0).getClass() == AreaChartPane.class);
//            }
//            itemIndex++;
//        }
//
//    }



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
