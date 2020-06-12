package com.trustsim.synthesiser;

import com.trustsim.simulator.agents.ConsumerAgent;
import com.trustsim.simulator.agents.ProducerAgent;

public class TransactionHistory {

  private final ConsumerAgent agent1;
  private final ProducerAgent agent2;
  private TransactionalVectorList transactionalVector;

  public TransactionHistory(ConsumerAgent  agent1, ProducerAgent agent2, TransactionalVectorList transactionalHistoryTimeAndSuccessValues) {
    this.agent1 = agent1;
    this.agent2 = agent2;
    transactionalVector = transactionalHistoryTimeAndSuccessValues;
  }

  public Double getTransactionalValue(int transactionTime) {
    return transactionalVector.getTrustVectorValue(transactionTime);
  }

  public TransactionalVectorList getTransactionalVector() {
    return transactionalVector;
  }

  public void setTransactionalValue(int transactionTime, double transactionalValue) {
    transactionalVector.setTrustVectorValue(transactionTime, transactionalValue);
  }

  public void setTransactionalVector(TransactionalVectorList transactionalHistoryTimeAndSuccessValues) {
    transactionalVector = transactionalHistoryTimeAndSuccessValues;
  }

  public ConsumerAgent getConsumerAgent() {
    return agent1;
  }

  public ProducerAgent getProducerAgent() {
    return agent2;
  }

}