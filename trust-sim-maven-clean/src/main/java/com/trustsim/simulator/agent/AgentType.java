package com.trustsim.simulator.agent;

/**
 * Enum represents a personality type of a simulation agent, and that personality's corresponding
 * mean and standard deviation, which describe the gaussian distribution of the probability that a
 * transaction is carried out successfully.
 */
public enum AgentType {
  MALICIOUS(0, 0.05, "Malicious Agent"),
  VBAD(0.0, 0.05, "V. Bad Agent"),
  BAD(0.25, 0.1, "Bad Agent"),
  OK(0.5, 0.1, "OK Agent"),
  GOOD(0.75, 0.1, "Good Agent"),
  VGOOD(0.9, 0.05, "V. Good Agent"),
  ISTRUSTED(1.0, 0.05, "Trusted Agent"),
  REALUSER(0.0, 0.0, "Real User");

  public double mean;
  public double stDev;
  public String name;

  /**
   * Object constructor.
   *
   * @param mean midpoint of gaussian distribution describing whether transaction is carried out
   * @param stDev corresponding standard deviation of gaussian distribution
   * @param name name of agent personality type
   */
  AgentType(final double mean, final double stDev, final String name) {
    this.mean = mean;
    this.stDev = stDev;
    this.name = name;
  }

  /**
   * Function overrides default toString function.
   *
   * @return string representation of object
   */
  @Override
  public String toString() {
    return this.name;
  }
}
