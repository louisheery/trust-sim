package com.trustsim.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.trustsim.simulator.simulationmanager.ServiceRequest;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class SimulationSubClassesTest {

  @Test
  public void serviceRequestObjectTest() {
    ServiceRequest serviceRequest =
        new ServiceRequest(new ArrayList<>(Arrays.asList(Math.random(), Math.random())), 10);
    ServiceRequest serviceRequestEqual =
        new ServiceRequest(new ArrayList<>(Arrays.asList(Math.random(), Math.random())), 10);
    ServiceRequest serviceRequestNotEqual =
        new ServiceRequest(new ArrayList<>(Arrays.asList(Math.random(), Math.random())), 20);

    assertEquals(0, serviceRequest.compareTo(serviceRequestEqual));
    assertNotEquals(0, serviceRequest.compareTo(serviceRequestNotEqual));

    serviceRequest.setConsumer(1);
    serviceRequest.setProducer(2);
    assertEquals(1, serviceRequest.getConsumer());
    assertEquals(2, serviceRequest.getProducer());

    assertFalse(serviceRequest.isCompleted());
    serviceRequest.setTransactionOutcome(true);
    assertTrue(serviceRequest.isCompleted());

    assertEquals(10.0, serviceRequest.getRequestTime());
    serviceRequest.setRequestTime(20);
    assertEquals(20.0, serviceRequest.getRequestTime());

    assertEquals(0.0, serviceRequest.getCompletionTime());
    serviceRequest.setCompletionTime(30);
    assertEquals(30.0, serviceRequest.getCompletionTime());
  }
}
