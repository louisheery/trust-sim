package com.trustsim.evaluator.tables;

import com.trustsim.MatrixAlgebra;
import com.trustsim.evaluator.data.TableDataObj;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class TwoDimTable extends VBox {

  private final int numberOfRows;
  private final int numberOfColumns;

  @FXML private TableView<double[]> tableView;

  /**
   * Constructor for a two dimensional table JavaFX object, which is used to a grid of data of data.
   *
   * @param selectedAgentSystemTable the agent system being displayed on the table
   * @param simulationNumber the simulation number of the simulation to be displayed; -1 for average
   * @param trustModelIndex index of the trust model of simulations which output is calculated from
   */
  public TwoDimTable(
      TableDataObj selectedAgentSystemTable, int simulationNumber, String trustModelIndex) {

    if (simulationNumber == -1) {
      simulationNumber = 0;
    }

    numberOfRows =
        selectedAgentSystemTable
            .getMatrixDataAll()
            .get(trustModelIndex)
            .get(simulationNumber)
            .length;
    numberOfColumns =
        selectedAgentSystemTable
            .getMatrixDataAll()
            .get(trustModelIndex)
            .get(simulationNumber)[0]
            .length;

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Table/2DTablePane.fxml"));

    try {

      fxmlLoader.setController(this);
      fxmlLoader.setRoot(this);
      fxmlLoader.load();
      fxmlLoader.getRoot();

      ObservableList<double[]> data =
          getTableData(selectedAgentSystemTable, simulationNumber, trustModelIndex);
      tableView.setItems(data);
      tableView.getColumns().setAll(createColumns());
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
   * @param trustModelIndex index of the trust model of simulations which output is calculated from
   * @return observable list with each item representing data in a column of the table
   */
  private ObservableList<double[]> getTableData(
      TableDataObj data, int simulationNumber, String trustModelIndex) {
    double[][] tableData =
        MatrixAlgebra.roundMatrix(
            data.getMatrixDataAll().get(trustModelIndex).get(simulationNumber), 4);

    ObservableList<double[]> output = FXCollections.observableArrayList();

    for (int i = 0; i < numberOfRows; i++) {

      double[] rowData = new double[numberOfColumns];

      if (numberOfColumns >= 0) {
        System.arraycopy(tableData[i], 0, rowData, 0, numberOfColumns);
      }

      output.add(rowData);
    }

    return output;
  }

  /**
   * Function generates a list of TableColumn objects which can be displayed within a TableView
   * object.
   *
   * @return list of TableColumn objects to be plotted on a TableView
   */
  private List<TableColumn<double[], Double>> createColumns() {
    return IntStream.range(0, numberOfColumns)
        .mapToObj(this::createColumn)
        .collect(Collectors.toList());
  }

  /**
   * Function generates a TableColumn object of a particular column of a TableView.
   *
   * @param i the index of the TableColumn
   * @return table column object
   */
  private TableColumn<double[], Double> createColumn(int i) {
    TableColumn<double[], Double> column = new TableColumn<>("Agent " + i);
    column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[i]));
    column.setCellFactory(
        e ->
            new TableCell<>() {
              @Override
              public void updateItem(Double item, boolean empty) {
                // Always invoke super constructor
                super.updateItem(item, empty);

                if (item == null || empty) {
                  setText(null);
                } else {
                  setText(item.toString());

                  if (item == 0.0) {
                    this.setStyle("-fx-background-color: #ff0000");
                  } else {
                    float adjustedValue =
                        item.floatValue() <= 0.05
                            ? item.floatValue() * 4
                            : (float) (0.2 + (item.floatValue() * 0.8));
                    Paint cellColor =
                        Paint.valueOf(
                            Integer.toHexString(
                                new Color(
                                        (float) (1.0 - adjustedValue), ((float) 1.0), (float) (0.0))
                                    .hashCode()));

                    this.setStyle(
                        "-fx-background-color: #" + cellColor.toString().substring(4, 10));
                  }
                }
              }
            });

    return column;
  }
}
