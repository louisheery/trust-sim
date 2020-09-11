package com.trustsim.simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.trustsim.simulator.simulationmanager.ContinuousServiceRequest;
import org.junit.jupiter.api.Test;

public class ContinuousServiceRequestTest {

  @Test
  public void continuousServiceRequestTest() {

    ContinuousServiceRequest serviceRequest = new ContinuousServiceRequest(0, 1, 0);
    ContinuousServiceRequest serviceRequestSame = new ContinuousServiceRequest(0, 1, 0);
    ContinuousServiceRequest serviceRequestDifferent = new ContinuousServiceRequest(0, 2, 0);

    // CompareTo check
    assertEquals(0, serviceRequest.compareTo(serviceRequestSame));
    assertEquals(-1, serviceRequest.compareTo(serviceRequestDifferent));

    // Manipulate Unpaid Payments
    serviceRequestSame.addUnpaidPayment();
    serviceRequestSame.clearUnpaidPayment();

    assertFalse(serviceRequest.isCompleted());
    serviceRequest.generateServiceRequestEvents(8, 2);
    assertEquals(0, serviceRequest.getSingularServiceRequest(0).getRequestTime());
    assertEquals(2, serviceRequest.getSingularServiceRequest(2).getRequestTime());
    assertEquals(4, serviceRequest.getSingularServiceRequest(4).getRequestTime());
    assertEquals(6, serviceRequest.getSingularServiceRequest(6).getRequestTime());

    serviceRequest.setContractStartTime(0);
    serviceRequest.setContractEndTime(6);

    assertEquals(0, serviceRequest.getRequestTime());
    assertEquals(6, serviceRequest.getCompletionTime());

    // Check event outcomes
    serviceRequest.setEventTransactionOutcome(0, true);
    serviceRequest.setEventTransactionOutcome(2, true);
    serviceRequest.setEventTransactionOutcome(4, true);
    serviceRequest.setEventTransactionOutcome(6, true);

    assertEquals(4, serviceRequest.getServiceRequestEvents().size());
    assertEquals(6, serviceRequest.getCompletionTime());

    assertTrue(serviceRequest.isCompleted());
  }
}
