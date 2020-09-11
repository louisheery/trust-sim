package com.trustsim;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

public abstract class TrustSimAbstractTest extends ApplicationTest {

    private Stage primaryStage;

    @Before
    public void beforeEachTest() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(TrustSim.class);
    }

    @After
    public void afterEachTest() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.show();
    }

    public void checkAllFXEventsFinished(){
        WaitForAsyncUtils.waitForFxEvents(1);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public <T extends Node> T findElement(final String nodeName) {
        return (T) lookup(nodeName).queryAll().iterator().next();
    }
}
