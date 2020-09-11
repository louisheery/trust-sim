package com.trustsim.evaluator;

/** Class acts as a wrapper around a simulation number, to be displayed in a combo box. */
public class SimulationNumber {

  private final int number;

  /**
   * Constructor initialises object.
   *
   * @param number simulation number of object
   */
  public SimulationNumber(int number) {
    this.number = number;
  }

  /**
   * Function returns number of simulation of object.
   *
   * @return simulation number
   */
  public int getNumber() {
    return number;
  }

  /**
   * Function overrides default toString function.
   *
   * @return string representation of object
   */
  @Override
  public String toString() {
    return "Replication " + (number + 1);
  }
}
