package com.jptest.payments.fulfillment.testonia.business.component.validation;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;

/**
 * Parent class for all Component Level golden file comparison tasks
 *
 */
public abstract class ComponentLevelGoldenFileComparisonAsserter extends GoldenFileComparisonAsserter {

    public ComponentLevelGoldenFileComparisonAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public boolean shouldParticipate(Context context) {
        return !isUserCreationMode() && !GolenFileAssertionType.E2E.getType()
                .equals(configuration.getString(CoreConfigKeys.GOLDEN_FILE_ASSERTION_EXECUTION_MODE.getName()));
    }
}
