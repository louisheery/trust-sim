package com.trustsim.evaluator.data;

import com.trustsim.evaluator.GraphTypeEnum;
import com.trustsim.simulator.simulationmanager.ServiceRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphDataObj implements DataObj {

  private final String name;
  private final String axisXLabel;
  private final String axisYLabel;
  private final GraphTypeEnum graphType;
  private List<String> trustModelNames;
  private Map<String, List<double[]>> graphDataXArray;
  private Map<String, List<double[]>> graphDataYArray;
  private Map<String, List<LinkedList<ServiceRequest>>> graphDataLinkedList;
  private Map<String, List<List<double[]>>> graphDataDoubleArray;
  private final int numberOfSimulations;
  private final int numberOfEvents;
  private final int trustUpdateInterval;

  /**
   * Constructor which initialises this object which is used to store immutable data to be plotted
   * to a Graph.
   *
   * @param name object name
   * @param axisXLabel label of x axis
   * @param axisYLabel label of y axis
   * @param graphType type of graph to be plotted
   * @param graphDataXArray data relating to x axis [OPTION A] (can be null if OPTION B not null)
   * @param graphDataYArray data related to y axis [OPTION A] (can be null if OPTION B not null)
   * @param graphDataLinkedList time series dataset [OPTION B] (can be null if OPTION A not null)
   */
  public GraphDataObj(
      String name,
      String axisXLabel,
      String axisYLabel,
      GraphTypeEnum graphType,
      Map<String, List<double[]>> graphDataXArray,
      Map<String, List<double[]>> graphDataYArray,
      Map<String, List<LinkedList<ServiceRequest>>> graphDataLinkedList,
      int numberOfEvents,
      int trustUpdateInterval) {
    this.name = name;
    this.axisXLabel = axisXLabel;
    this.axisYLabel = axisYLabel;
    this.graphType = graphType;
    this.graphDataXArray = graphDataXArray;
    this.graphDataYArray = graphDataYArray;
    this.graphDataLinkedList = graphDataLinkedList;
    if (graphDataXArray != null) {
      this.numberOfSimulations =
          graphDataXArray.get(graphDataXArray.keySet().iterator().next()).size();
      this.trustModelNames = new ArrayList<>(graphDataXArray.keySet());
    } else {
      this.numberOfSimulations =
          graphDataLinkedList.get(graphDataLinkedList.keySet().iterator().next()).size();
      this.trustModelNames = new ArrayList<>(graphDataLinkedList.keySet());
    }

    this.numberOfEvents = numberOfEvents;
    this.trustUpdateInterval = trustUpdateInterval;
  }

  /**
   * Constructor which initialises this object which is used to store immutable data to be plotted
   * to a Graph.
   *
   * @param name object name
   * @param axisXLabel label of x axis
   * @param axisYLabel label of y axis
   * @param graphType type of graph to be plotted
   * @param trustModelNames name of trust models being plotted
   * @param graphDataDoubleArray dual axis dataset to be plotted
   */
  public GraphDataObj(
      String name,
      String axisXLabel,
      String axisYLabel,
      GraphTypeEnum graphType,
      List<String> trustModelNames,
      Map<String, List<List<double[]>>> graphDataDoubleArray,
      int numberOfEvents,
      int trustUpdateInterval) {
    this.name = name;
    this.axisXLabel = axisXLabel;
    this.axisYLabel = axisYLabel;
    this.graphType = graphType;
    this.trustModelNames = trustModelNames;
    this.graphDataDoubleArray = graphDataDoubleArray;
    this.numberOfSimulations =
        graphDataDoubleArray.get(graphDataDoubleArray.keySet().iterator().next()).size();
    this.trustModelNames = new ArrayList<>(graphDataDoubleArray.keySet());
    this.numberOfEvents = numberOfEvents;
    this.trustUpdateInterval = trustUpdateInterval;
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
   * Function returns the x axis label.
   *
   * @return x axis of graph label
   */
  public String getXAxisLabel() {
    return axisXLabel;
  }

  /**
   * Function returns the y axis label.
   *
   * @return y axis of graph label
   */
  public String getYAxisLabel() {
    return axisYLabel;
  }

  /**
   * Function returns type of graph which this object contains the data to plot.
   *
   * @return Enum object representing graph type to be plotted
   */
  public GraphTypeEnum getGraphType() {
    return graphType;
  }

  /**
   * Returns x axis dataset.
   *
   * @return Map object with key representing Trust Model plotted on graph, and value storing the
   *     corresponding x axis dataset for that trust model
   */
  public Map<String, List<double[]>> getGraphDataAllXArray() {
    return graphDataXArray;
  }

  /**
   * Returns y axis dataset.
   *
   * @return the Map object with key representing Trust Model plotted on graph, and value storing
   *     the corresponding y axis dataset for that trust model
   */
  public Map<String, List<double[]>> getGraphDataAllYArray() {
    return graphDataYArray;
  }

  /**
   * Returns time series dataset.
   *
   * @return the Map object with key representing Trust Model plotted on graph, and value storing
   *     the corresponding time series dataset for that trust model
   */
  public Map<String, List<LinkedList<ServiceRequest>>> getGraphDataAllLinkedList() {
    return graphDataLinkedList;
  }

  /**
   * Returns x-y dataset.
   *
   * @return the Map object with key representing Trust Model plotted on graph, and value storing
   *     the corresponding x-y dataset for that trust model
   */
  public Map<String, List<List<double[]>>> getGraphDataAllDoubleArray() {
    return graphDataDoubleArray;
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

  /**
   * Function returning number of simulations contained within the object's dataset.
   *
   * @return number of simulations
   */
  public double getNumberOfSimulations() {
    return numberOfSimulations;
  }

  /**
   * Function returning number of service requests contained within the object's dataset.
   *
   * @return number of service requests
   */
  public int getNumberOfServiceRequests() {
    return numberOfEvents;
  }

  /**
   * Function returning trust update interval contained within the object's dataset.
   *
   * @return trust update interval
   */
  public int getTrustUpdateInterval() {
    return trustUpdateInterval;
  }
}
