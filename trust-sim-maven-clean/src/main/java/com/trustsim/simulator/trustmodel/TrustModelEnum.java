package com.trustsim.simulator.trustmodel;

/** Object used to store type of trust models. */
public enum TrustModelEnum {
  EigenTrustModel("EigenTrustModel"),
  DynamicTrustModel("DynamicTrustModel");

  private final String name;

  TrustModelEnum(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
