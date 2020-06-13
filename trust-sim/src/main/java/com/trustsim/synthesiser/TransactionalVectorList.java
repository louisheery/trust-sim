package com.trustsim.synthesiser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionalVectorList {

  public Map<Integer, Boolean> transactions = new HashMap<>();
  public int firstTransactionTime = 0;
  public int lastTransactionTime = 0;
  public int transactionCount;

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
    }

  }


  public int getNumberOfTransactions() {
    return transactionCount;
  }
}
