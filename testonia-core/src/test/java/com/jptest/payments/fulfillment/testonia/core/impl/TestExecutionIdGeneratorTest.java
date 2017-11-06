package com.jptest.payments.fulfillment.testonia.core.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jptest.payments.fulfillment.testonia.core.impl.TestExecutionIdGenerator;

public class TestExecutionIdGeneratorTest {

    @Test
    public void testIdGenerator() {
        String name = TestExecutionIdGenerator.getTestId();
        Assert.assertNotNull(name);
        String nameAgain = TestExecutionIdGenerator.getTestId();
        Assert.assertNotNull(nameAgain);
        Assert.assertSame(nameAgain, name);
    }

}
