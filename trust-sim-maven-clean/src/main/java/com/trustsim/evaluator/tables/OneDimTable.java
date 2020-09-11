package com.trustsim.evaluator.tables;

import com.trustsim.MatrixAlgebra;
import com.trustsim.evaluator.data.TableDataObj;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class OneDimTable extends VBox {

  private final TableDataObj tableDataObjInput;
  private final int numberOfRows;
  private final int numberOfColumns;

  @FXML private TableView<double[]> table1;

  /**
   * Constructor for a One dimensional table JavaFX object, which is used to plot 1 or more columns
   * of data.
   *
   * @param selectedAgentSystemTable the agent system being displayed on the table
   * @param simulationNumber the simulation number of the simulation to be displayed; -1 for average
   */
  public OneDimTable(TableDataObj selectedAgentSystemTable, int simulationNumber) {

    tableDataObjInput = selectedAgentSystemTable;

    int numberOfSimulations =
        selectedAgentSystemTable
            .getTableXDataAll()
            .get(selectedAgentSystemTable.getTableXDataAll().keySet().iterator().next())
            .size();

    if (simulationNumber == -1 && numberOfSimulations > 1) {
      numberOfRows =
          (tableDataObjInput.getTableXDataAll())
              .get(tableDataObjInput.getTableXDataAll().keySet().iterator().next())
              .get(0)
              .length;
      numberOfColumns = 1 + (2 * tableDataObjInput.getTableXDataAll().size());
    } else {
      if (simulationNumber == -1) {
        simulationNumber = 0;
      }

      numberOfRows =
          (tableDataObjInput.getTableXDataAll())
              .get(tableDataObjInput.getTableXDataAll().keySet().iterator().next())
              .get(simulationNumber)
              .length;
      numberOfColumns = 1 + tableDataObjInput.getTableXDataAll().size();
    }

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Table/1DTablePane.fxml"));

    try {

      fxmlLoader.setController(this);
      fxmlLoader.setRoot(this);
      fxmlLoader.load();
      fxmlLoader.getRoot();

      ObservableList<double[]> data = getTableData(selectedAgentSystemTable, simulationNumber);

      table1.setItems(data);
      table1.getColumns().setAll(createColumns(simulationNumber, numberOfSimulations));
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  /**
   * Function which generates an list of table data which can be read by a TableView object to be
   * displayed on that table.
   *
   * @param data object containing data to be plotted on table
   * @param simulationNumber the simulation number of the simulation to be plotted; -1 for average
   * @return observable list with each item representing data in a column of the table
   */
  private ObservableList<double[]> getTableData(TableDataObj data, int simulationNumber) {
    List<double[]> tableXData =
        data.getTableXDataAll().get(data.getTableXDataAll().keySet().iterator().next());
    Map<String, List<double[]>> tableYData = data.getTableYDataAll();

    int numberOfSimulations = tableXData.size();

    ObservableList<double[]> output = FXCollections.observableArrayList();
    List<String> trustModelNames = data.getTrustModelNames();

    if (simulationNumber != -1 || numberOfSimulations == 1) {

      if (simulationNumber == -1) {
        simulationNumber = 0;
      }
      for (int i = 0; i < numberOfRows; i++) {

        double[] rowData = new double[numberOfColumns];

        for (int j = 0; j < numberOfColumns; j++) {
          if (j == 0) {
            rowData[j] = MatrixAlgebra.roundNumber(tableXData.get(simulationNumber)[i], 4);
          } else {
            rowData[j] =
                MatrixAlgebra.roundNumber(
                    tableYData.get(trustModelNames.get(j - 1)).get(simulationNumber)[i], 4);
          }
        }
        output.add(rowData);
      }
    } else {

      double[] averageXValues = MatrixAlgebra.calculateVectorMean(tableXData, 4);

      for (int i = 0; i < numberOfRows; i++) {

        double[] rowData = new double[numberOfColumns];

        for (int j = 0; j < numberOfColumns; j++) {
          if (j == 0) {
            rowData[j] = averageXValues[i];
          } else {

            List<double[]> averageYValues = new ArrayList<>();
            List<double[]> averageYStDevValues = new ArrayList<>();
            int trustIndex = (j % 2 == 0) ? ((j - 2) / 2) : ((j - 1) / 2);
            List<double[]> tableYDatum = tableYData.get(trustModelNames.get(trustIndex));
            averageYValues.add(MatrixAlgebra.calculateVectorMean(tableYDatum, 4));
            averageYStDevValues.add(MatrixAlgebra.calculateVectorStDev(tableYDatum, 4));

            if (j % 2 != 0) {
              rowData[j] = averageYValues.get(0)[i];
            } else {
              rowData[j] = averageYStDevValues.get(0)[i];
            }
          }
        }
        output.add(rowData);
      }
    }

    return output;
  }

  /**
   * Function generates a list of TableColumn objects which can be displayed within a TableView
   * object.
   *
   * @param simulationNumber the simulation number of the simulation to be plotted; -1 for average
   * @param numberOfSimulations the number of simulations over which the simulation is performed
   * @return list of TableColumn objects to be plotted on a TableView
   */
  private List<TableColumn<double[], Double>> createColumns(
      int simulationNumber, int numberOfSimulations) {
    if (simulationNumber == -1) {
      return IntStream.range(0, numberOfColumns)
          .mapToObj(this::createColumnStdev)
          .collect(Collectors.toList());
    } else {
      return IntStream.range(0, numberOfColumns)
          .mapToObj(this::createColumn)
          .collect(Collectors.toList());
    }
  }

  /**
   * Function generates a TableColumn object of a particular column of a TableView, where the
   * TableView does not include mean & standard deviation of data across multiple datasets.
   *
   * @param i the index of the TableColumn
   * @return table column object
   */
  private TableColumn<double[], Double> createColumn(int i) {
    TableColumn<double[], Double> column;
    column = new TableColumn<>(tableDataObjInput.getColumnNames().get(i));
    column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[i]));
    return column;
  }

  /**
   * Function generates a TableColumn object of a particular column of a TableView, where the
   * TableView does include mean & standard deviation of data across multiple datasets.
   *
   * @param i the index of the TableColumn
   * @return table column object
   */
  private TableColumn<double[], Double> createColumnStdev(int i) {
    TableColumn<double[], Double> column;
    if (i == 0) {
      column = new TableColumn<>(tableDataObjInput.getColumnNames().get(i));
    } else {
      if (i % 2 != 0) {
        column = new TableColumn<>(tableDataObjInput.getColumnNames().get((i + 1) / 2));
      } else {
        column = new TableColumn<>(tableDataObjInput.getColumnNames().get(i / 2) + " Stdev");
      }
    }
    column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[i]));
    return column;
  }
}
