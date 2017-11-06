package com.jptest.payments.fulfillment.testonia.business.component.validation;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Parent class for all E2E golden file comparison tasks
 *
 */
public abstract class E2EGoldenFileComparisonAsserter extends GoldenFileComparisonAsserter {

    public E2EGoldenFileComparisonAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public boolean shouldParticipate(Context context) {
        // return !COMPONENT_LEVEL_ASSERTION
        // .equals(configuration.getString(CoreConfigKeys.ASSERTION_EXECUTION_MODE.getName()));
        return !isUserCreationMode() && !GolenFileAssertionType.COMPONENT.getType()
                .equals(configuration.getString(CoreConfigKeys.GOLDEN_FILE_ASSERTION_EXECUTION_MODE.getName()));
    }
}
