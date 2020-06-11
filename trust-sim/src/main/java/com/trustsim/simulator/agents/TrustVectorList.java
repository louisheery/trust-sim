package com.trustsim.simulator.agents;

import static java.lang.System.*;

public class TrustVectorList {

  public Double[] trustVectors;

  public TrustVectorList(Double[] trustVectorValues, int size) {

    trustVectors = new Double[size];
    arraycopy(trustVectorValues, 0, trustVectors, 0, trustVectorValues.length);
  }

  public Double getTrustVectorValue(int trustVector) {
    return trustVectors[trustVector];
  }

  public void setTrustVectorValue(int trustVector, double trustVectorValue) {
    trustVectors[trustVector] = trustVectorValue;
  }

}
