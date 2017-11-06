package com.jptest.payments.fulfillment.testonia.business.component.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.PayV2Response;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * Validating the PayV2 response
 * 
 * @JP Inc.
 */

public class PayV2ResponseAsserter extends BaseAsserter {
	private static final Logger LOGGER = LoggerFactory.getLogger(PayV2ResponseAsserter.class);

	@Override
	public void validate(final Context context) {

		final PayV2Response payV2Response = (PayV2Response) this.getDataFromContext(context,
				ContextKeys.PAY_V2_RESPONSE.getName());

		Assert.assertNotNull(payV2Response, "payV2Response should not be NULL");
		Assert.assertEquals(payV2Response.getRequestStatus(), "PROSD");

		Assert.assertNotNull(payV2Response.getFulfillmentHandle());

		context.setData(ContextKeys.WTRANSACTION_ID_KEY.getName(), payV2Response.getFulfillmentHandle());
        context.addReportingAttribute(ReportingAttributes.PAYMENT_ENCRYPTED_ID, payV2Response.getFulfillmentHandle());
		LOGGER.info("\n FulfillPayment Encrypted Transaction Handle:{}", payV2Response.getFulfillmentHandle());
	}

}
