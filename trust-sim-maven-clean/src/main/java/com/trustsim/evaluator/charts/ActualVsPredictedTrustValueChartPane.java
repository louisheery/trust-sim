package com.trustsim.evaluator.charts;

import com.trustsim.MatrixAlgebra;
import com.trustsim.evaluator.data.GraphDataObj;
import java.awt.Color;
import java.io.IOException;
import java.text.NumberFormat;
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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;

public class ActualVsPredictedTrustValueChartPane extends VBox {

  @FXML private VBox scatterChartPane;

  /**
   * Constructor which creates a Chart showing how Agent's Global Trust values (Predicted Trust
   * Value) which was calculated using a particular trust model compares to its actual trust values.
   * the simulation.
   *
   * @param selectedAgentSystemGraph the agent system being plotted on the graph
   * @param simulationNumber index of simulation which output is calculated from
   */
  public ActualVsPredictedTrustValueChartPane(
      GraphDataObj selectedAgentSystemGraph, int simulationNumber) {

    List<String> trustModelNames = selectedAgentSystemGraph.getTrustModelNames();

    double numberOfSimulations =
        selectedAgentSystemGraph
            .getGraphDataAllXArray()
            .get(selectedAgentSystemGraph.getGraphDataAllXArray().keySet().iterator().next())
            .size();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chart/ScatterChartPane.fxml"));

    try {
      fxmlLoader.setController(this);
      fxmlLoader.setRoot(this);
      fxmlLoader.load();

      XYIntervalSeriesCollection output = new XYIntervalSeriesCollection();

      for (String trustModel : trustModelNames) {

        List<double[]> graphDataXList =
            selectedAgentSystemGraph.getGraphDataAllXArray().get(trustModel);
        List<double[]> graphDataYList =
            selectedAgentSystemGraph.getGraphDataAllYArray().get(trustModel);

        XYIntervalSeries values = new XYIntervalSeries(trustModel + " Trust Values");

        double zvalue99ConfidenceInterval = 1.96;
        double confidenceInterval99Factor =
            zvalue99ConfidenceInterval / Math.sqrt(numberOfSimulations);

        for (int i = 0; i < graphDataXList.get(0).length; i++) {

          if (simulationNumber != -1 || numberOfSimulations == 1) {

            if (simulationNumber == -1) {
              simulationNumber = 0;
            }

            values.add(
                graphDataXList.get(simulationNumber)[i],
                graphDataXList.get(simulationNumber)[i],
                graphDataXList.get(simulationNumber)[i],
                graphDataYList.get(simulationNumber)[i],
                graphDataYList.get(simulationNumber)[i],
                graphDataYList.get(simulationNumber)[i]);
          } else {

            double[] graphDataXMeanList = MatrixAlgebra.calculateVectorMean(graphDataXList);
            double[] graphDataYMeanList = MatrixAlgebra.calculateVectorMean(graphDataYList);
            double[] graphDataYStDevList = MatrixAlgebra.calculateVectorStDev(graphDataYList);

            values.add(
                graphDataXMeanList[i],
                graphDataXMeanList[i],
                graphDataXMeanList[i],
                graphDataYMeanList[i],
                graphDataYMeanList[i] - (graphDataYStDevList[i] * confidenceInterval99Factor),
                graphDataYMeanList[i] + (graphDataYStDevList[i] * confidenceInterval99Factor));
          }
        }

        output.addSeries(values);
      }

      NumberAxis axisX = new NumberAxis(selectedAgentSystemGraph.getXAxisLabel());
      NumberAxis axisY = new NumberAxis(selectedAgentSystemGraph.getYAxisLabel());

      XYErrorRenderer renderer = new XYErrorRenderer();
      renderer.setDrawXError(false);
      renderer.setDrawYError(true);
      renderer.setDefaultLinesVisible(true);
      renderer.setDefaultShapesVisible(true);

      renderer.setDefaultItemLabelGenerator(
          new StandardXYItemLabelGenerator(
              "{2}", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance()));
      renderer.setDefaultItemLabelsVisible(true);
      renderer.setDefaultToolTipGenerator(
          new StandardXYToolTipGenerator(
              "{0}: (95% CI: [{1}, {2}])", NumberFormat.getInstance(), NumberFormat.getInstance()) {
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

      XYPlot plot = new XYPlot(output, axisX, axisY, renderer);
      plot.setBackgroundPaint(new Color(0xf4f4f4));
      JFreeChart chart = new JFreeChart(selectedAgentSystemGraph.getName(), plot);
      chart.setBackgroundPaint(new Color(0xf4f4f4));
      ChartViewer viewer = new ChartViewer(chart);
      viewer.setPrefWidth(640);
      viewer.setPrefHeight(550);
      scatterChartPane.getChildren().add(viewer);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
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
