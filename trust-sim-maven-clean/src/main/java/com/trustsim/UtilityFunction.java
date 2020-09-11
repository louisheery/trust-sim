package com.trustsim;

public class UtilityFunction {

  /**
   * Function for checking input value lies between expected range of allowed values, else it is
   * replaced with defaultValue.
   *
   * @param value value to be checked
   * @param min minimum allowed value
   * @param max maximum allowed value
   * @param defaultValue default value
   * @return value or default value
   */
  public static int checkValueOrReplace(int value, int min, int max, int defaultValue) {

    if (value < min || value > max) {
      return defaultValue;
    } else {
      return value;
    }
  }

  /**
   * Function for checking input value lies between expected range of allowed values, else it is
   * replaced with defaultValue.
   *
   * @param value value to be checked
   * @param min minimum allowed value
   * @param max maximum allowed value
   * @param defaultValue default value
   * @return value or default value
   */
  public static double checkValueOrReplace(
      double value, double min, double max, double defaultValue) {

    if (value < min || value > max) {
      return defaultValue;
    } else {
      return value;
    }
  }
}
