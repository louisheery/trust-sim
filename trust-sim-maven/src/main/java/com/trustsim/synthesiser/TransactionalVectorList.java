package com.trustsim.synthesiser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionalVectorList {

  private Map<Integer, Boolean> transactions = new HashMap<>();
  private int firstTransactionTime = 0;
  private int lastTransactionTime = 0;
  private int transactionCount = 0;
  private int successfulTransactionCount = 0;

  public TransactionalVectorList() {
  }

  public TransactionalVectorList(List<Double> transactionalValuesInput) {
    //transactions = transactionalValuesInput;
  }

  public Boolean getTransactionValue(int transactionTime) {
    return transactions.get(transactionTime);
  }

  public Map<Integer, Boolean> getTransactionsMap() {
    return transactions;
  }

  public int getFirstTransactionTime() {
    return firstTransactionTime;
  }

  public int getLastTransactionTime() {
    return lastTransactionTime;
  }

  public void setTransactionalVectorValue(Integer transactionTime, boolean didTransactionComplete) {
    firstTransactionTime = Math.min(firstTransactionTime, transactionTime);
    lastTransactionTime = Math.max(lastTransactionTime, transactionTime);

    if (!transactions.containsKey(transactionTime)) {
      transactions.put(transactionTime, didTransactionComplete);
      transactionCount++;
      if (didTransactionComplete) {
        successfulTransactionCount++;
      }
    }

  }


  public int getNumberOfTransactions() {
    return transactionCount;
  }

  public int getNumberOfSuccessfulTransactions() {
    return successfulTransactionCount;
  }
}
