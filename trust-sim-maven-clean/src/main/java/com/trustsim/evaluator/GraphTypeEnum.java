package com.trustsim.evaluator;

/** Object used to store type of graph to be plotted on a JFreeChart Chart Viewer. */
public enum GraphTypeEnum {
  AreaGraph("Area Graph"),
  ScatterGraph("Scatter Graph"),
  GlobalTrustGraph("Agent Global Trust Graph");

  private final String name;

  /**
   * Constructor of Enum object.
   *
   * @param name name of object to be instantiated
   */
  GraphTypeEnum(final String name) {
    this.name = name;
  }

  /**
   * Function returns name of Enum object.
   *
   * @return string of name of Enum object
   */
  public String getName() {
    return name;
  }
}
