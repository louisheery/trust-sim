package com.trustsim.evaluator.data;

import com.trustsim.evaluator.TableTypeEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableDataObj implements DataObj {

  private final String name;
  private final List<String> columnNames;
  private final TableTypeEnum tableType;
  private final Map<String, List<double[]>> columnXData;
  private final Map<String, List<double[]>> columnYData;
  private final Map<String, List<double[][]>> matrixData;
  private final List<String> trustModelNames;

  /**
   * Constructor which initialises this object which is used to store immutable data to be plotted
   * to a Table.
   *
   * @param name object name
   * @param tableType type of table which data can be plotted on
   * @param columnNames names of columns of data
   * @param columnXData data related to x axis of table [OPTION A: for a One Dimensional Table]
   * @param columnYData data related to y axis of table [OPTION A: for a One Dimensional Table]
   * @param matrixData 2d-data related to x-y axis of table [OPTION B: for a Two Dimensional Table]
   */
  public TableDataObj(
      String name,
      TableTypeEnum tableType,
      List<String> columnNames,
      Map<String, List<double[]>> columnXData,
      Map<String, List<double[]>> columnYData,
      Map<String, List<double[][]>> matrixData) {
    this.name = name;
    this.columnNames = columnNames;
    this.tableType = tableType;
    this.columnXData = columnXData;
    this.columnYData = columnYData;
    this.matrixData = matrixData;
    if (columnXData != null) {
      this.trustModelNames = new ArrayList<>(columnXData.keySet());
    } else {
      this.trustModelNames = new ArrayList<>(matrixData.keySet());
    }
  }

  /**
   * Returns name of object.
   *
   * @return name of object
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Function returns the names of each column for the table.
   *
   * @return column names of table
   */
  public List<String> getColumnNames() {
    return columnNames;
  }

  /**
   * Function returns type of table which this object contains the data to plot.
   *
   * @return Enum object representing table type to be plotted
   */
  public TableTypeEnum getTableType() {
    return tableType;
  }

  /**
   * Returns x axis dataset.
   *
   * @return Map object with key representing Trust Model plotted on table, and value storing the
   *     corresponding x axis dataset for that trust model
   */
  public Map<String, List<double[]>> getTableXDataAll() {
    return columnXData;
  }

  /**
   * Returns y axis dataset.
   *
   * @return Map object with key representing Trust Model plotted on table, and value storing the
   *     corresponding y axis dataset for that trust model
   */
  public Map<String, List<double[]>> getTableYDataAll() {
    return columnYData;
  }

  /**
   * Returns x-y matrix dataset.
   *
   * @return the Map object with key representing Trust Model plotted on table, and value storing
   *     the corresponding x-y dataset for that trust model
   */
  public Map<String, List<double[][]>> getMatrixDataAll() {
    return matrixData;
  }

  /**
   * Returns names of trust models of the data stored in this object.
   *
   * @return trust model names
   */
  @Override
  public List<String> getTrustModelNames() {
    return trustModelNames;
  }

  /**
   * Function overrides default toString function.
   *
   * @return string representation of object
   */
  @Override
  public String toString() {
    return name;
  }
}
