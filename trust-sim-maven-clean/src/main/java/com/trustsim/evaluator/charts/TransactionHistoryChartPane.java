package com.trustsim.evaluator.charts;

import com.trustsim.MatrixAlgebra;
import com.trustsim.evaluator.data.GraphDataObj;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import java.awt.Color;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TransactionHistoryChartPane extends VBox {

  @FXML private VBox areaChartPane;
  private final GraphDataObj agentSystemGraph;

  /**
   * Constructor which creates a Chart showing how The Number of Successful and Unsuccessful
   * transactions varies over time during a simulation. the simulation.
   *
   * @param selectedAgentSystemGraph object storing dataset to be plotted on graph
   * @param simulationNumber the number of the simulation to be plotted
   */
  public TransactionHistoryChartPane(GraphDataObj selectedAgentSystemGraph, int simulationNumber) {

    this.agentSystemGraph = selectedAgentSystemGraph;
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/Chart/AgentGlobalTrustChartPane.fxml"));

    List<String> trustModelNames = selectedAgentSystemGraph.getTrustModelNames();

    try {
      fxmlLoader.setController(this);
      fxmlLoader.setRoot(this);
      fxmlLoader.load();
      double numberOfSimulations = agentSystemGraph.getNumberOfSimulations();
      double numberOfTrustModels = agentSystemGraph.getGraphDataAllLinkedList().size();

      if (simulationNumber != -1 || numberOfSimulations == 1) {
        if (simulationNumber == -1) {
          simulationNumber = 0;
        }

        XYSeriesCollection output = new XYSeriesCollection();

        for (String trustModel : trustModelNames) {
          LinkedList<ServiceRequest> graphDataList =
              agentSystemGraph.getGraphDataAllLinkedList().get(trustModel).get(simulationNumber);

          double numberOfBuckets = 20.0;
          double firstRequestTime = graphDataList.getFirst().getRequestTime();
          double lastRequestTime = graphDataList.getLast().getRequestTime();
          int sizeOfBucket = (int) ((lastRequestTime - firstRequestTime) / numberOfBuckets);

          int successfulTransactions =
              getTotalTransactions(true, false, simulationNumber, trustModel);
          int unsuccessfulTransactions =
              getTotalTransactions(false, true, simulationNumber, trustModel);
          int totalTransactions = successfulTransactions + unsuccessfulTransactions;

          XYSeries successfulRequests =
              new XYSeries(
                  trustModel
                      + " Successful Requests ("
                      + successfulTransactions
                      + "/"
                      + totalTransactions
                      + " = "
                      + ((successfulTransactions * 100 / totalTransactions)) / 100.0
                      + ")");
          XYSeries unsuccessfulRequests =
              new XYSeries(
                  trustModel
                      + " Unsuccessful Requests ("
                      + unsuccessfulTransactions
                      + "/"
                      + totalTransactions
                      + " = "
                      + ((unsuccessfulTransactions * 100 / totalTransactions)) / 100.0
                      + ")");

          int numberOfSuccessfulRequests = 0;
          int numberOfUnsuccessfulRequests = 0;
          double currentUpperBoundValue = firstRequestTime + sizeOfBucket;

          for (ServiceRequest request : graphDataList) {
            if (request.getRequestTime() < currentUpperBoundValue) {
              if (request.isCompleted()) {
                numberOfSuccessfulRequests++;
              } else {
                numberOfUnsuccessfulRequests++;
              }
            } else {
              successfulRequests.add(
                  (currentUpperBoundValue - (sizeOfBucket / 2.0)), numberOfSuccessfulRequests);
              unsuccessfulRequests.add(
                  (currentUpperBoundValue - (sizeOfBucket / 2.0)), numberOfUnsuccessfulRequests);

              numberOfSuccessfulRequests = 0;
              numberOfUnsuccessfulRequests = 0;
              currentUpperBoundValue += sizeOfBucket;
            }
          }

          output.addSeries(successfulRequests);
          output.addSeries(unsuccessfulRequests);
        }

        NumberAxis xaxis = new NumberAxis(selectedAgentSystemGraph.getXAxisLabel());
        NumberAxis yaxis = new NumberAxis(selectedAgentSystemGraph.getYAxisLabel());
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        renderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
        XYPlot plot = new XYPlot(output, xaxis, yaxis, renderer);
        plot.setBackgroundPaint(new Color(0xf4f4f4));
        JFreeChart chart = new JFreeChart(selectedAgentSystemGraph.getName(), plot);
        chart.setBackgroundPaint(new Color(0xf4f4f4));
        ChartViewer viewer = new ChartViewer(chart);
        viewer.setPrefWidth(640);
        viewer.setPrefHeight(550);
        areaChartPane.getChildren().add(viewer);

      } else {
        XYIntervalSeriesCollection output = new XYIntervalSeriesCollection();

        for (String trustModel : trustModelNames) {

          List<LinkedList<ServiceRequest>> graphDataList =
              agentSystemGraph.getGraphDataAllLinkedList().get(trustModel);

          int numberOfBuckets = 20;
          double firstRequestTime = graphDataList.get(0).getFirst().getRequestTime();
          double lastRequestTime = graphDataList.get(0).getLast().getRequestTime();
          int sizeOfBucket = (int) ((lastRequestTime - firstRequestTime) / numberOfBuckets);

          int avgSuccessfulTransactions =
              (int) Math.ceil(getAvgTotalTransactions(true, false, trustModel));
          int avgUnsuccessfulTransactions =
              (int) Math.ceil(getAvgTotalTransactions(false, true, trustModel));
          int avgTotalTransactions =
              (int) Math.ceil(avgSuccessfulTransactions + avgUnsuccessfulTransactions);

          XYIntervalSeries successfulRequests =
              new XYIntervalSeries(
                  trustModel
                      + " Successful Requests ("
                      + avgSuccessfulTransactions
                      + "/"
                      + avgTotalTransactions
                      + " = "
                      + ((avgSuccessfulTransactions * 100 / avgTotalTransactions)) / 100.0
                      + ")");
          XYIntervalSeries unsuccessfulRequests =
              new XYIntervalSeries(
                  trustModel
                      + " Unsuccessful Requests ("
                      + avgUnsuccessfulTransactions
                      + "/"
                      + avgTotalTransactions
                      + " = "
                      + ((avgUnsuccessfulTransactions * 100 / avgTotalTransactions)) / 100.0
                      + ")");

          double zvalue99ConfidenceInterval = 1.96;
          double confidenceInterval99Factor =
              zvalue99ConfidenceInterval / Math.sqrt(numberOfSimulations);

          List<List<double[][]>> graphIntervals =
              MatrixAlgebra.calculateLinkedListSegmentedIntervals(
                  graphDataList, firstRequestTime, lastRequestTime, sizeOfBucket);

          List<double[][]> graphMean = MatrixAlgebra.calculateMatrixListMean(graphIntervals);
          List<double[][]> graphStdev = MatrixAlgebra.calculateMatrixListStDev(graphIntervals);

          for (int dataPoints = 0; dataPoints < graphMean.get(0).length; dataPoints++) {

            successfulRequests.add(
                graphMean.get(0)[dataPoints][0],
                graphMean.get(0)[dataPoints][0],
                graphMean.get(0)[dataPoints][0],
                graphMean.get(0)[dataPoints][1],
                graphMean.get(0)[dataPoints][1]
                    - (graphStdev.get(0)[dataPoints][1] * confidenceInterval99Factor),
                graphMean.get(0)[dataPoints][1]
                    + (graphStdev.get(0)[dataPoints][1] * confidenceInterval99Factor));

            unsuccessfulRequests.add(
                graphMean.get(1)[dataPoints][0],
                graphMean.get(1)[dataPoints][0],
                graphMean.get(1)[dataPoints][0],
                graphMean.get(1)[dataPoints][1],
                graphMean.get(1)[dataPoints][1]
                    - (graphStdev.get(1)[dataPoints][1] * confidenceInterval99Factor),
                graphMean.get(1)[dataPoints][1]
                    + (graphStdev.get(1)[dataPoints][1] * confidenceInterval99Factor));
          }

          output.addSeries(successfulRequests);
          output.addSeries(unsuccessfulRequests);
        }

        NumberAxis xaxis = new NumberAxis(selectedAgentSystemGraph.getXAxisLabel());
        NumberAxis yaxis = new NumberAxis(selectedAgentSystemGraph.getYAxisLabel());
        XYErrorRenderer renderer = new XYErrorRenderer();
        renderer.setDrawXError(false);
        renderer.setDrawYError(true);
        renderer.setDefaultLinesVisible(true);
        renderer.setDefaultShapesVisible(true);
        renderer.setDefaultItemLabelGenerator(
            new StandardXYItemLabelGenerator(
                "{2}", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance()));
        renderer.setDefaultToolTipGenerator(
            new StandardXYToolTipGenerator(
                "{0}: (95% CI: [{1}, {2}])",
                NumberFormat.getInstance(), NumberFormat.getInstance()) {
              @Override
              protected Object[] createItemArray(XYDataset data, int series, int item) {
                XYIntervalSeriesCollection d = (XYIntervalSeriesCollection) data;
                Object[] result = new Object[3];
                double y = d.getYValue(series, item);
                result[0] = getYFormat().format(y);
                double min = d.getStartYValue(series, item);
                result[1] = getYFormat().format(min);
                double max = d.getEndYValue(series, item);
                result[2] = getYFormat().format(max);
                return result;
              }
            });
        renderer.setDefaultItemLabelsVisible(true);

        XYPlot plot = new XYPlot(output, xaxis, yaxis, renderer);
        plot.setBackgroundPaint(new Color(0xf4f4f4));
        JFreeChart chart = new JFreeChart(selectedAgentSystemGraph.getName(), plot);
        chart.setBackgroundPaint(new Color(0xf4f4f4));
        ChartViewer viewer = new ChartViewer(chart);
        viewer.setPrefWidth(640);
        viewer.setPrefHeight(550);
        areaChartPane.getChildren().add(viewer);
      }

    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  /**
   * Function which calculates the average total number of transactions (either including/excluding
   * unsuccessful and/or successful transactions) for a set of simulations which were run using a
   * particular trust model.
   *
   * @param includeSuccessful whether successful transactions should be included in total
   * @param includeUnsuccessful whether unsuccessful transactions should be included in total
   * @param trustModelIndex index of the trust model of simulations which output is calculated from
   * @return average value for total number of transactions fulfilling input criteria
   */
  int getAvgTotalTransactions(
      boolean includeSuccessful, boolean includeUnsuccessful, String trustModelIndex) {

    double numberOfSimulations =
        agentSystemGraph.getGraphDataAllLinkedList().get(trustModelIndex).size();
    List<LinkedList<ServiceRequest>> graphDataList =
        agentSystemGraph.getGraphDataAllLinkedList().get(trustModelIndex);

    double totalNumberOfSuccessfulRequests = 0.0;
    double totalNumberOfUnsuccessfulRequests = 0.0;

    for (LinkedList<ServiceRequest> simulation : graphDataList) {
      for (ServiceRequest request : simulation) {
        if (request.isCompleted()) {
          totalNumberOfSuccessfulRequests++;
        } else {
          totalNumberOfUnsuccessfulRequests++;
        }
      }
    }

    int averageTotalTransactions =
        (int)
            Math.ceil(
                (totalNumberOfSuccessfulRequests + totalNumberOfUnsuccessfulRequests)
                    / numberOfSimulations);

    if (includeSuccessful && includeUnsuccessful) {
      return averageTotalTransactions;
    } else if (includeSuccessful) {
      return (int) Math.ceil(totalNumberOfSuccessfulRequests / numberOfSimulations);
    } else if (includeUnsuccessful) {
      return (int)
          (averageTotalTransactions
              - Math.ceil(totalNumberOfSuccessfulRequests / numberOfSimulations));
    } else {
      return 0;
    }
  }

  /**
   * Function which calculates the total number of transactions (either including/excluding
   * unsuccessful and/or successful transactions) for a particular simulation which were run using a
   * particular trust model.
   *
   * @param includeSuccessful whether successful transactions should be included in total
   * @param includeUnsuccessful whether unsuccessful transactions should be included in total
   * @param simulationNumber index of simulation which output is calculated from
   * @param trustModelIndex index of the trust model of simulations which output is calculated from
   * @return value for total number of transactions fulfilling input criteria
   */
  int getTotalTransactions(
      boolean includeSuccessful,
      boolean includeUnsuccessful,
      int simulationNumber,
      String trustModelIndex) {

    LinkedList<ServiceRequest> graphDataList =
        agentSystemGraph.getGraphDataAllLinkedList().get(trustModelIndex).get(simulationNumber);

    int totalNumberOfSuccessfulRequests = 0;
    int totalNumberOfUnsuccessfulRequests = 0;

    for (ServiceRequest request : graphDataList) {
      if (request.isCompleted()) {
        totalNumberOfSuccessfulRequests++;
      } else {
        totalNumberOfUnsuccessfulRequests++;
      }
    }

    if (includeSuccessful && includeUnsuccessful) {
      return totalNumberOfSuccessfulRequests + totalNumberOfUnsuccessfulRequests;
    } else if (includeSuccessful) {
      return totalNumberOfSuccessfulRequests;
    } else if (includeUnsuccessful) {
      return totalNumberOfUnsuccessfulRequests;
    } else {
      return 0;
    }
  }

  /**
   * Function required to override in order to extend VBox; implementation is never called.
   *
   * @return null
   */
  @Override
  public Node getStyleableNode() {
    return null;
  }
}
