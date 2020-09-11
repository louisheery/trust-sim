package com.trustsim.about;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
class AboutControllerTest extends TrustSimAbstractTest {

  private HomeMockView homeMockView;

  @BeforeEach
  public void beforeEachTest() throws Exception {
    FxToolkit.setupApplication(TrustSim.class);
    homeMockView = new HomeMockView(this);
    homeMockView.clickScreenButton("#toAboutPageButton");
  }

  @AfterEach
  public void afterEachTest() throws Exception {
    FxToolkit.hideStage();
    release(new KeyCode[] {});
    release(new MouseButton[] {});
  }

  @Test
  public void aboutTest() {
    assertNotNull(findElement("#aboutTextHeader"));
    assertNotNull(findElement("#aboutTextSubheader"));
  }
}
