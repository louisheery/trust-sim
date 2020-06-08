package com.trustsim.simulator.agents;

import static java.lang.System.*;

public class TrustVectorList {

  public double[] trustVectors;

  public TrustVectorList(double[] trustVectorValues, int size) {

    trustVectors = new double[size];
    arraycopy(trustVectorValues, 0, trustVectors, 0, trustVectorValues.length);
  }

  public double getTrustVectorValue(int trustVector) {
    return trustVectors[trustVector];
  }

  public void setTrustVectorValue(int trustVector, double trustVectorValue) {
    trustVectors[trustVector] = trustVectorValue;
  }

}
