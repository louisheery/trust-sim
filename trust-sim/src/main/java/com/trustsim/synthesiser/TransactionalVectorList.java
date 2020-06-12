package com.trustsim.synthesiser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionalVectorList {

  public List<Double> transactionalValues = new ArrayList<>();

  public TransactionalVectorList(Double[] transactionalValuesInput) {
    Collections.addAll(transactionalValues, transactionalValuesInput);
  }

  public Double getTrustVectorValue(int transactionTime) {
    return transactionalValues.get(transactionTime);
  }

  public void setTrustVectorValue(int transactionTime, double transactionValue) {
    transactionalValues.set(transactionTime, transactionValue);
  }


}
