package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.financialinstrument.AnalyzeAddFiResponse;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * Validating the AnalyzeAddFi response
 * 
 * @JP Inc.
 */

public class AnalyzeAddFiResponseAsserter extends BaseAsserter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeAddFiResponseAsserter.class);

	@Override
	public void validate(Context context) {

		AnalyzeAddFiResponse analyzeAddFiResponse = (AnalyzeAddFiResponse) getDataFromContext(context,
				ContextKeys.ANALYZE_ADD_FI_RESPONSE_KEY.getName());

		// validate
		if (analyzeAddFiResponse.getSuccess()) {
			LOGGER.info("analyze_add_fi call success {}:");
		} else {
			Assert.fail("analyze_add_fi call failed");
		}
	}

}
