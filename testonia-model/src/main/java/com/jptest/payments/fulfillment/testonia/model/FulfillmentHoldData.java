package com.jptest.payments.fulfillment.testonia.model;

/**
 * Part of the test input data that represents 
 * the transaction hold specific information. This is used 
 * within {@link TestCaseInputData} which represents the entire
 * test input. 
 */
public class FulfillmentHoldData {

    private Long holdType;

    public Long getHoldType() {
        return holdType;
    }

    public void setHoldType(Long holdType) {
        this.holdType = holdType;
    }

}
