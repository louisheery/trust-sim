package com.trustsim.evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.trustsim.evaluator.data.TableDataObj;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class EvaluatorDataStructuresTest {

  @Test
  public void graphTypeNameTest() {

    GraphTypeEnum areaGraph = GraphTypeEnum.AreaGraph;
    GraphTypeEnum scatterGraph = GraphTypeEnum.ScatterGraph;

    assertEquals("Area Graph", areaGraph.getName());
    assertEquals("Scatter Graph", scatterGraph.getName());
  }

  @Test
  public void tableTypeNameTest() {

    TableTypeEnum oneDimTable = TableTypeEnum.OneDimTable;
    TableTypeEnum twoDimTable = TableTypeEnum.TwoDimTable;

    assertEquals("1D Table", oneDimTable.getName());
    assertEquals("2D Table", twoDimTable.getName());
  }

  @Test
  public void tableDataTest() {

    double[] column1Array = new double[10];
    double[] column2Array = new double[10];
    double[][] matrixArray = new double[10][10];
    Arrays.fill(column1Array, Math.random());
    Arrays.fill(column2Array, Math.random());
    for (double[] row : matrixArray) {
      Arrays.fill(row, Math.random());
    }
    List<double[]> column1Data = new ArrayList<>(Collections.singletonList(column1Array));
    List<double[]> column2Data = new ArrayList<>(Collections.singletonList(column2Array));
    List<double[][]> matrixData = new ArrayList<>();
    matrixData.add(matrixArray);

    TableDataObj tableDataObj =
        new TableDataObj(
            "TEST_NAME",
            TableTypeEnum.OneDimTable,
            new ArrayList<>(Arrays.asList("Data Column 1", "Data Column 2")),
            Map.of("EigenTrustModel", column1Data),
            Map.of("EigenTrustModel", column2Data),
            Map.of("EigenTrustModel", matrixData));
    TableTypeEnum twoDimTable = TableTypeEnum.TwoDimTable;

    assertEquals("TEST_NAME", tableDataObj.getName());
    assertEquals(
        new ArrayList<>(Arrays.asList("Data Column 1", "Data Column 2")),
        tableDataObj.getColumnNames());
    assertEquals(TableTypeEnum.OneDimTable, tableDataObj.getTableType());

    assertEquals(column1Data, tableDataObj.getTableXDataAll().get("EigenTrustModel"));
    assertEquals(column2Data, tableDataObj.getTableYDataAll().get("EigenTrustModel"));
    assertEquals(Map.of("EigenTrustModel", matrixData), tableDataObj.getMatrixDataAll());
    assertEquals("TEST_NAME", tableDataObj.toString());
  }
}
