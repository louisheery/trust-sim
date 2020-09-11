package com.trustsim.evaluator.charts;

import com.trustsim.MatrixAlgebra;
import com.trustsim.evaluator.AgentTypeWrapper;
import com.trustsim.evaluator.data.GraphDataObj;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;

public class AgentGlobalTrustChartPane extends VBox {

  @FXML public VBox areaChartPane;

  /**
   * Constructor which creates a Chart showing how Agent's Global Trust values evolves through the
   * simulation.
   *
   * @param selectedAgentSystemGraph the agent system being plotted on the graph
   * @param simulationNumber the simulation number of the simulation to be plotted; -1 for average
   * @param dataPointBinSize the size of bins
   * @param agentTypeIndices the index of agent in the system which is being plotted
   * @param agentTypeWrapper object which stores how agent type changes through simulation
   */
  public AgentGlobalTrustChartPane(
      GraphDataObj selectedAgentSystemGraph,
      int simulationNumber,
      int dataPointBinSize,
      List<Integer> agentTypeIndices,
      AgentTypeWrapper agentTypeWrapper) {

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Chart/AreaChartPane.fxml"));

    try {
      fxmlLoader.setController(this);
      fxmlLoader.setRoot(this);
      fxmlLoader.load();

      double numberOfSimulations = selectedAgentSystemGraph.getNumberOfSimulations();
      double updateWidth = selectedAgentSystemGraph.getTrustUpdateInterval();

      List<String> trustModelNames = selectedAgentSystemGraph.getTrustModelNames();

      if (simulationNumber != -1 || numberOfSimulations == 1) {

        if (simulationNumber == -1) {
          simulationNumber = 0;
        }

        XYIntervalSeriesCollection output = new XYIntervalSeriesCollection();

        Map<String, List<List<double[]>>> graphDataList =
            selectedAgentSystemGraph.getGraphDataAllDoubleArray();

        int currentTimePeriod;

        for (String trustModel : trustModelNames) {

          currentTimePeriod = 0;

          XYIntervalSeries agentTrustValue =
              new XYIntervalSeries(trustModel + " Agent Trust Value");

          for (int updateTimeIndex = 0;
              updateTimeIndex < graphDataList.get(trustModel).get(simulationNumber).size();
              updateTimeIndex++) {

            List<Double> trustValueAtTimeStep = new ArrayList<>();

            for (Integer agentIndex : agentTypeIndices) {
              trustValueAtTimeStep.add(
                  graphDataList
                      .get(trustModel)
                      .get(simulationNumber)
                      .get(updateTimeIndex)[agentIndex]);
            }

            double trustValueMean = MatrixAlgebra.calculateListMean(trustValueAtTimeStep);

            agentTrustValue.add(
                currentTimePeriod,
                currentTimePeriod,
                currentTimePeriod,
                trustValueMean,
                trustValueMean,
                trustValueMean);

            currentTimePeriod += updateWidth;
          }
          output.addSeries(agentTrustValue);
        }

        // Axis
        NumberAxis axisX = new NumberAxis(selectedAgentSystemGraph.getXAxisLabel());
        NumberAxis axisY = new NumberAxis(selectedAgentSystemGraph.getYAxisLabel());
        DeviationRenderer renderer = new DeviationRenderer(true, false);
        renderer.setSeriesStroke(
            0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        renderer.setSeriesStroke(0, new BasicStroke(2.2f));
        renderer.setSeriesFillPaint(0, new Color(250, 180, 180));
        renderer.setDefaultLinesVisible(true);
        renderer.setDefaultItemLabelsVisible(true);
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
        XYPlot plot = new XYPlot(output, axisX, axisY, renderer);

        // Add Agent Change Markings
        Integer previousChangeTime = agentTypeWrapper.getTypeChangeTimes().get(0);
        Integer currentChangeTime;
        for (int i = 0; i < agentTypeWrapper.getTypeChangeTimes().size(); i++) {
          currentChangeTime = agentTypeWrapper.getTypeChangeTimes().get(i);
          final Marker originalEnd = new ValueMarker(currentChangeTime);
          originalEnd.setPaint(Color.orange);

          String labelText;
          if (i == 0) {
            labelText = agentTypeWrapper.getAgentTypesMap().get(currentChangeTime).toString();
          } else {
            labelText =
                agentTypeWrapper.getAgentTypesMap().get(previousChangeTime).toString()
                    + " -> "
                    + agentTypeWrapper.getAgentTypesMap().get(currentChangeTime).toString();
          }
          originalEnd.setLabel(labelText);
          originalEnd.setLabelAnchor(RectangleAnchor.CENTER);
          originalEnd.setLabelTextAnchor(TextAnchor.CENTER_RIGHT);
          plot.addDomainMarker(originalEnd);

          previousChangeTime = currentChangeTime;
        }

        plot.setBackgroundPaint(new Color(0xf4f4f4));
        JFreeChart chart = new JFreeChart(selectedAgentSystemGraph.getName(), plot);
        chart.setBackgroundPaint(new Color(0xf4f4f4));
        ChartViewer viewer = new ChartViewer(chart);
        viewer.setPrefWidth(640);
        viewer.setPrefHeight(550);
        areaChartPane.getChildren().add(viewer);

      } else {

        XYIntervalSeriesCollection output = new XYIntervalSeriesCollection();

        Map<String, List<List<double[]>>> graphDataList =
            selectedAgentSystemGraph.getGraphDataAllDoubleArray();

        double zvalue99ConfidenceInterval = 1.96;
        double confidenceInterval99Factor =
            zvalue99ConfidenceInterval / Math.sqrt(numberOfSimulations);

        int currentTimePeriod;

        for (String trustModel : trustModelNames) {

          currentTimePeriod = 0;
          XYIntervalSeries agentTrustValue =
              new XYIntervalSeries(trustModel + " Agent Trust Value");

          for (int updateTimeIndex = 0;
              updateTimeIndex < graphDataList.get(trustModel).get(0).size();
              updateTimeIndex++) {

            List<Double> trustValueAtTimeStep = new ArrayList<>();

            for (List<double[]> simulationDataset : graphDataList.get(trustModel)) {
              for (Integer agentIndex : agentTypeIndices) {
                trustValueAtTimeStep.add(simulationDataset.get(updateTimeIndex)[agentIndex]);
              }
            }

            double trustValueMean = MatrixAlgebra.calculateListMean(trustValueAtTimeStep);
            double trustValueStDev = MatrixAlgebra.calculateListStDev(trustValueAtTimeStep);
            agentTrustValue.add(
                currentTimePeriod,
                currentTimePeriod,
                currentTimePeriod,
                trustValueMean,
                trustValueMean - (confidenceInterval99Factor * trustValueStDev),
                trustValueMean + (confidenceInterval99Factor * trustValueStDev));

            currentTimePeriod += updateWidth;
          }

          output.addSeries(agentTrustValue);
        }

        DeviationRenderer renderer = new DeviationRenderer(true, false);

        for (int trustModel = 0; trustModel < graphDataList.size(); trustModel++) {
          renderer.setSeriesStroke(
              trustModel, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
          renderer.setSeriesStroke(trustModel, new BasicStroke(2.2f));
          if (trustModel % 3 == 0) {
            renderer.setSeriesFillPaint(trustModel, new Color(250, 180, 180));
          } else if (trustModel % 3 == 1) {
            renderer.setSeriesFillPaint(trustModel, new Color(180, 222, 250));
          } else {
            renderer.setSeriesFillPaint(trustModel, new Color(193, 250, 180));
          }
        }

        renderer.setDefaultLinesVisible(true);
        renderer.setDefaultItemLabelsVisible(true);
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
        NumberAxis axisX = new NumberAxis(selectedAgentSystemGraph.getXAxisLabel());
        NumberAxis axisY = new NumberAxis(selectedAgentSystemGraph.getYAxisLabel());

        XYPlot plot = new XYPlot(output, axisX, axisY, renderer);

        LegendItemCollection legend = plot.getLegendItems();

        for (int trustModel = 0; trustModel < graphDataList.size(); trustModel++) {
          if (trustModel % 3 == 0) {
            legend.add(
                new LegendItem(
                    selectedAgentSystemGraph.getTrustModelNames().get(trustModel)
                        + " 95% Confidence Interval",
                    null,
                    null,
                    null,
                    new Rectangle(10, 10),
                    new Color(250, 180, 180)));
          } else if (trustModel % 3 == 1) {
            legend.add(
                new LegendItem(
                    selectedAgentSystemGraph.getTrustModelNames().get(trustModel)
                        + " 95% Confidence Interval",
                    null,
                    null,
                    null,
                    new Rectangle(10, 10),
                    new Color(217, 250, 180)));
          } else {
            legend.add(
                new LegendItem(
                    selectedAgentSystemGraph.getTrustModelNames().get(trustModel)
                        + " 95% Confidence Interval",
                    null,
                    null,
                    null,
                    new Rectangle(10, 10),
                    new Color(180, 250, 250)));
          }
        }
        plot.setFixedLegendItems(legend);

        // Add Agent Change Markings
        Integer previousChangeTime = agentTypeWrapper.getTypeChangeTimes().get(0);
        Integer currentChangeTime;
        for (int i = 0; i < agentTypeWrapper.getTypeChangeTimes().size(); i++) {
          currentChangeTime = agentTypeWrapper.getTypeChangeTimes().get(i);
          final Marker originalEnd = new ValueMarker(currentChangeTime);
          originalEnd.setPaint(Color.orange);

          String labelText;
          if (i == 0) {
            labelText =
                "     " + agentTypeWrapper.getAgentTypesMap().get(currentChangeTime).toString();
          } else {
            labelText =
                "     "
                    + agentTypeWrapper.getAgentTypesMap().get(previousChangeTime).toString()
                    + " → "
                    + agentTypeWrapper.getAgentTypesMap().get(currentChangeTime).toString();
          }
          originalEnd.setLabel(labelText);
          originalEnd.setLabelAnchor(RectangleAnchor.CENTER);
          originalEnd.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
          plot.addDomainMarker(originalEnd);

          previousChangeTime = currentChangeTime;
        }

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
   * Function required to override in order to extend VBox; implementation is never called.
   *
   * @return null
   */
  @Override
  public Node getStyleableNode() {
    return null;
  }
}
