package com.trustsim.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.trustsim.simulator.trustmodel.TrustModelEnum;
import org.junit.jupiter.api.Test;

public class TrustModelTest {

  @Test
  public void trustModelTest() {

    TrustModelEnum eigenTrustEnum = TrustModelEnum.EigenTrustModel;
    TrustModelEnum dynamicTrustEnum = TrustModelEnum.DynamicTrustModel;

    assertEquals("EigenTrustModel", eigenTrustEnum.getName());
    assertEquals("EigenTrustModel", eigenTrustEnum.toString());
    assertEquals("DynamicTrustModel", dynamicTrustEnum.getName());
    assertEquals("DynamicTrustModel", dynamicTrustEnum.toString());
  }
}
