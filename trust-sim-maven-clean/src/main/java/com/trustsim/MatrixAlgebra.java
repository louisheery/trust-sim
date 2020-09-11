package com.trustsim;

import com.trustsim.simulator.simulationmanager.ServiceRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Mathematical Helper Functions Class, consisting of a set of static member functions which can be
 * used to compute vector and matrix mathematics.
 */
public class MatrixAlgebra {

  /**
   * Calculates the vector difference between two 1 dimensional vectors.
   *
   * @param a vector 1
   * @param b vector 2
   * @return vector difference between the two vectors
   */
  public static double vectorDifferenceMagnitude(double[] a, double[] b) {

    double difference = 0.0;

    for (int i = 0; i < a.length; i++) {
      difference += Math.pow(a[i] - b[i], 2);
    }
    return difference == 0.0 ? difference : Math.sqrt(difference / a.length);
  }

  /**
   * Rounds a number to a particular number of decimal places.
   *
   * @param number original number
   * @param decimalPlaces decimal places to round number to
   * @return rounded output number
   */
  public static double roundNumber(double number, int decimalPlaces) {
    return new BigDecimal(Double.toString(number))
        .setScale(decimalPlaces, RoundingMode.HALF_UP)
        .doubleValue();
  }

  /**
   * Rounds all numbers to a particular number of decimal places contained within a matrix.
   *
   * @param matrix original matrix, who's values are to be rounded
   * @param decimalPlaces decimal places to round numbers to
   * @return rounded output matrix
   */
  public static double[][] roundMatrix(double[][] matrix, int decimalPlaces) {

    double[][] result = new double[matrix.length][matrix[0].length];

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        result[i][j] = roundNumber(matrix[i][j], decimalPlaces);
      }
    }
    return result;
  }

  /**
   * Calculates the mean value of each element of a Vector from a list of equally sized vectors.
   *
   * @param inputList list of vectors
   * @return output vector representing average of all input vectors
   */
  public static double[] calculateVectorMean(List<double[]> inputList) {

    if (inputList.size() < 1) {
      return new double[0];
    }

    int count = inputList.size();

    double[] output = new double[inputList.get(0).length];

    for (int row = 0; row < inputList.get(0).length; row++) {
      double sum = 0.0;
      for (double[] vector : inputList) {
        sum += vector[row];
      }
      output[row] = sum / count;
    }

    return output;
  }

  /**
   * Calculates the mean value of each element of a Vector from a list of equally sized vectors, and
   * then rounds the resulting output to a particular number of decimal places.
   *
   * @param inputList list of vectors
   * @param decimalPlaces decimal places to round numbers to
   * @return output vector representing average of all input vectors which is rounded
   */
  public static double[] calculateVectorMean(List<double[]> inputList, int decimalPlaces) {

    if (inputList.size() < 1) {
      return new double[0];
    }

    int count = inputList.size();

    double[] output = new double[inputList.get(0).length];

    for (int row = 0; row < inputList.get(0).length; row++) {
      double sum = 0.0;
      for (double[] vector : inputList) {
        sum += vector[row];
      }
      output[row] = roundNumber(sum / count, decimalPlaces);
    }

    return output;
  }

  /**
   * Calculates the standard deviation value of each element of a Vector from a list of equally
   * sized vectors.
   *
   * @param inputList list of vectors
   * @return output vector representing standard deviation of all input vectors
   */
  public static double[] calculateVectorStDev(List<double[]> inputList) {

    if (inputList.size() < 1) {
      return new double[0];
    }

    int count = inputList.size();

    double[] output = new double[inputList.get(0).length];

    for (int row = 0; row < inputList.get(0).length; row++) {
      // Calculate Mean
      double sum = 0.0;
      for (double[] vector : inputList) {
        sum += vector[row];
      }
      double mean = sum / count;

      // Calculate Stdev
      double sumSqDiff = 0.0;
      for (double[] vector : inputList) {
        sumSqDiff += (mean - vector[row]) * (mean - vector[row]);
      }

      output[row] = Math.sqrt((1.0 / (count - 1)) * sumSqDiff);
    }
    return output;
  }

  /**
   * Calculates the standard deviation value of each element of a Vector from a list of equally
   * sized vectors, and then rounds the resulting output to a particular number of decimal places.
   *
   * @param inputList list of vectors
   * @param decimalPlaces decimal places to round numbers to
   * @return output vector representing standard deviation of all input vectors which is rounded
   */
  public static double[] calculateVectorStDev(List<double[]> inputList, int decimalPlaces) {

    if (inputList.size() < 1) {
      return new double[0];
    }

    int count = inputList.size();

    double[] output = new double[inputList.get(0).length];

    for (int row = 0; row < inputList.get(0).length; row++) {
      // Calculate Mean
      double sum = 0.0;
      for (double[] vector : inputList) {
        sum += vector[row];
      }
      double mean = sum / count;

      // Calculate Stdev
      double sumSqDiff = 0.0;
      for (double[] vector : inputList) {
        sumSqDiff += (mean - vector[row]) * (mean - vector[row]);
      }

      output[row] = roundNumber(Math.sqrt((1.0 / (count - 1)) * sumSqDiff), decimalPlaces);
    }
    return output;
  }

  /**
   * Calculates the mean value of a list of values.
   *
   * @param inputList list of values
   * @return output value representing average of all input values
   */
  public static double calculateListMean(List<Double> inputList) {

    if (inputList.size() < 1) {
      return 0.0;
    }
    double sum = 0.0;
    for (Double value : inputList) {
      sum += value;
    }

    return sum / inputList.size();
  }

  /**
   * Calculates the standard deviation value of a list of values.
   *
   * @param inputList list of values
   * @return output value representing average of all input values
   */
  public static double calculateListStDev(List<Double> inputList) {

    if (inputList.size() < 1) {
      return 0.0;
    }

    // Calculate mean
    double mean = calculateListMean(inputList);

    // Calculate Stdev
    double sumSqDiff = 0.0;
    for (Double value : inputList) {
      sumSqDiff += (mean - value) * (mean - value);
    }

    return Math.sqrt((1.0 / (inputList.size() - 1)) * sumSqDiff);
  }

  /**
   * Segments a list of multiple linked lists of service requests into an output list of bucketed
   * objects. Each bucketed object is a list of double[][], where each double[][] object represents
   * the number of successful and unsuccessful service requests for each interval of time.
   *
   * @param inputList list of 1 or more linked lists of service request objects.
   * @param min max simulation time of bucketed values
   * @param max min simulation time of bucketed values
   * @param intervalSize size of each bucket of values
   * @return list of bucketed successful and unsuccessful transaction bucketed lists.
   */
  public static List<List<double[][]>> calculateLinkedListSegmentedIntervals(
      List<LinkedList<ServiceRequest>> inputList, double min, double max, double intervalSize) {

    List<List<double[][]>> output = new ArrayList<>();

    for (LinkedList<ServiceRequest> simulation : inputList) {

      double numberOfSuccessfulRequests = 0;
      double numberOfUnsuccessfulRequests = 0;
      double currentUpperBoundValue = min + intervalSize;

      int range = (int) ((max - min) / intervalSize);

      double[][] successfulRequests = new double[range + 1][2];
      double[][] unsuccessfulRequests = new double[range + 1][2];

      int i = 0;
      for (ServiceRequest request : simulation) {

        if (request.getRequestTime() < currentUpperBoundValue) {
          if (request.isCompleted()) {
            numberOfSuccessfulRequests++;
          } else {
            numberOfUnsuccessfulRequests++;
          }
        } else {
          successfulRequests[i][0] = (currentUpperBoundValue - (intervalSize / 2.0));
          successfulRequests[i][1] = numberOfSuccessfulRequests;

          unsuccessfulRequests[i][0] = (currentUpperBoundValue - (intervalSize / 2.0));
          unsuccessfulRequests[i][1] = numberOfUnsuccessfulRequests;

          numberOfSuccessfulRequests = 0;
          numberOfUnsuccessfulRequests = 0;
          currentUpperBoundValue += intervalSize;
          i++;
        }
      }

      List<double[][]> simulationIteration = new ArrayList<>();
      simulationIteration.add(successfulRequests);
      simulationIteration.add(unsuccessfulRequests);

      output.add(simulationIteration);
    }

    return output;
  }

  /**
   * Function calculates the mean value of a list of lists of double[][] matrices.
   *
   * @param graphDataList input list of list of double[][] matrices
   * @return list of double[][] representing the mean value across each list of double[][] inputs
   */
  public static List<double[][]> calculateMatrixListMean(List<List<double[][]>> graphDataList) {

    List<double[][]> output = new ArrayList<>();

    for (int dataSet = 0; dataSet < graphDataList.get(0).size(); dataSet++) {

      double[][] outputItem =
          new double[graphDataList.get(0).get(0).length][graphDataList.get(0).get(0)[0].length];

      for (int dataPoint = 0; dataPoint < graphDataList.get(0).get(0).length; dataPoint++) {

        double valueX = 0.0;
        double meanCountY = 0.0;
        double meanValueY = 0.0;
        for (List<double[][]> simulationDatasets : graphDataList) {

          valueX = simulationDatasets.get(dataSet)[dataPoint][0];
          meanValueY += simulationDatasets.get(dataSet)[dataPoint][1];
          meanCountY++;
        }

        meanValueY /= meanCountY;
        outputItem[dataPoint][0] = valueX;
        outputItem[dataPoint][1] = meanValueY;
      }

      output.add(outputItem);
    }

    return output;
  }

  /**
   * Function calculates the standard deviation value of a list of lists of double[][] matrices.
   *
   * @param graphDataList input list of list of double[][] matrices
   * @return list of double[][] representing the standard deviation value across each list of
   *     double[][] inputs
   */
  public static List<double[][]> calculateMatrixListStDev(List<List<double[][]>> graphDataList) {

    List<double[][]> linkedListIntervalsMeans = calculateMatrixListMean(graphDataList);

    List<double[][]> output = new ArrayList<>();

    for (int dataSet = 0; dataSet < graphDataList.get(0).size(); dataSet++) {

      double[][] outputItem =
          new double[graphDataList.get(0).get(0).length][graphDataList.get(0).get(0)[0].length];

      for (int dataPoint = 0; dataPoint < graphDataList.get(0).get(0).length; dataPoint++) {

        double valueX = 0.0;
        double sumSqDiff = 0.0;
        double count = 0.0;
        for (List<double[][]> simulationDatasets : graphDataList) {
          count++;
          valueX = simulationDatasets.get(dataSet)[dataPoint][0];
          sumSqDiff +=
              (linkedListIntervalsMeans.get(dataSet)[dataPoint][1]
                      - simulationDatasets.get(dataSet)[dataPoint][1])
                  * (linkedListIntervalsMeans.get(dataSet)[dataPoint][1]
                      - simulationDatasets.get(dataSet)[dataPoint][1]);
        }

        outputItem[dataPoint][0] = valueX;
        outputItem[dataPoint][1] = Math.sqrt((1.0 / (count - 1)) * sumSqDiff);
      }

      output.add(outputItem);
    }

    return output;
  }
}
