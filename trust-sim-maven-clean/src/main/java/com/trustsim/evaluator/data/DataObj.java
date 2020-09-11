package com.trustsim.evaluator.data;

import java.util.List;

public interface DataObj {

  /**
   * Returns name of object.
   *
   * @return name of object
   */
  String getName();

  /**
   * Returns names of trust models of the data stored in this object.
   *
   * @return trust model names
   */
  List<String> getTrustModelNames();

  /**
   * Function overrides default toString function.
   *
   * @return string representation of object
   */
  @Override
  String toString();
}
