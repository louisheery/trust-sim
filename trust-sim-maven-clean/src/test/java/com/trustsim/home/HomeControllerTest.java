package com.trustsim.home;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.trustsim.HomeMockView;
import com.trustsim.TrustSim;
import com.trustsim.TrustSimAbstractTest;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import junitparams.JUnitParamsRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.testfx.api.FxToolkit;

@RunWith(JUnitParamsRunner.class)
class HomeControllerTest extends TrustSimAbstractTest {

  private HomeMockView mockView;

  @BeforeEach
  public void beforeEachTest() throws Exception {
    FxToolkit.setupApplication(TrustSim.class);
    mockView = new HomeMockView(this);
  }

  @AfterEach
  public void tearDown() throws Exception {
    FxToolkit.hideStage();
    release(new KeyCode[] {});
    release(new MouseButton[] {});
  }

  @Test
  public void testSynthesiserButton() {
    mockView.clickScreenButton("#synthesiserButton");
    checkAllFXEventsFinished();
    assertEquals("Synthesiser - TrustSim", getPrimaryStage().getTitle());
  }

  @Test
  public void testSimulatorButton() {
    mockView.clickScreenButton("#simulatorButton");
    checkAllFXEventsFinished();
    assertEquals("Simulator - TrustSim", getPrimaryStage().getTitle());
  }

  @Test
  public void testEvaluatorButton() {
    mockView.clickScreenButton("#evaluatorButton");
    checkAllFXEventsFinished();
    assertEquals("Evaluator - TrustSim", getPrimaryStage().getTitle());
  }

  @Test
  public void testHelpButtons() {

    mockView.clickScreenButton("#synthesiserHelpButton");
    checkAllFXEventsFinished();
    assertEquals("Home - TrustSim", getPrimaryStage().getTitle());

    mockView.clickScreenButton("#simulatorHelpButton");
    checkAllFXEventsFinished();
    assertEquals("Home - TrustSim", getPrimaryStage().getTitle());

    mockView.clickScreenButton("#evaluatorHelpButton");
    checkAllFXEventsFinished();
    assertEquals("Home - TrustSim", getPrimaryStage().getTitle());
  }
}
