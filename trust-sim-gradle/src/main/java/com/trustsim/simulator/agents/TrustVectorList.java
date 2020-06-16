package com.trustsim.simulator.agents;

import java.util.ArrayList;
import java.util.List;

public class TrustVectorList {

  private List<Double> trustVector = new ArrayList<>();


  public TrustVectorList(List<Double> trustVectorInput) {
    trustVector.addAll(trustVectorInput);
  }

//  public TrustVectorList(Double[] trustVector) {
//
//    this.directTrust = directTrust;
//    this.indirectTrust = indirectTrust;
//
//    // REPLACE THIS: INSTEAD IT SHOULD CALL THE TRUST MODEL AND GET IT TO CALCULATE
//    // GENERAL TRUST BASED ON THE PARAMETERS WHICH WEIGHT DIRECT TRUST AND INDIRECT TRUST
//    // FOR THIS MODEL:
//    // e.g. this.generalTrust = calculateGeneralTrust(directTrust, indirectTrust);
//    this.generalTrust = generalTrust;
//  }

  public List<Double> getTrustVector() {
    return trustVector;
  }

  public Double getTrustVectorValue(int index) {
    return trustVector.get(index);
  }

  public double getAverageTrustVectorValue() {
    double totalTrustValue = 0;
    for (Double trustValue : trustVector) {
      totalTrustValue += trustValue;
    }
    return totalTrustValue / trustVector.size();
  }

  public void setTrustVector(List<Double> trustVectorInput) {
    trustVector.clear();
    trustVector.addAll(trustVectorInput);
  }

  public void setTrustVectorValue(Double trustInput, int index) {
    trustVector.set(index, trustInput);
  }



}
