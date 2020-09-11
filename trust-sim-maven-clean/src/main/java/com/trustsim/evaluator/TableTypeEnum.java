package com.trustsim.evaluator;

/** Object used to store type of table to be rendered on a JavaFX TableView. */
public enum TableTypeEnum {
  OneDimTable("1D Table"),
  TwoDimTable("2D Table");

  private final String name;

  /**
   * Constructor of Enum object.
   *
   * @param name name of object to be instantiated
   */
  TableTypeEnum(final String name) {
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
