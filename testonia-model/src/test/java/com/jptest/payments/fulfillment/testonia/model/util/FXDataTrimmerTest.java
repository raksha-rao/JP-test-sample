package com.jptest.payments.fulfillment.testonia.model.util;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link FXDataTrimmer}
 */
public class FXDataTrimmerTest {

	@Test(dataProvider = "trimAuditTrailDP")
	public void trimAuditTrail(String inputAuditTrail, String expectedString) {
		Assert.assertEquals(FXDataTrimmer.trimAuditTrail(inputAuditTrail), expectedString);
	}

	@DataProvider
	private static Object[][] trimAuditTrailDP() {
		return new Object[][] { {
				"V:1:USD:1609:EUR:1000:0.62162125612600000000;C:0:M:USD;D:8402:0.6408466558:0.6413000000:0.6416847800;",
				"V:1:USD:1609:EUR:1000:0.6216212561;C:0:M:USD;D:8402:0.6408466558:0.6413000000:0.6416847800;" } };
	}

	@Test(dataProvider = "trimExchangeRateDP")
	public void trimExchangeRate(String inputExchangeRate, String expectedExchangeRate) {
		Assert.assertEquals(FXDataTrimmer.trimExchangeRate(inputExchangeRate), expectedExchangeRate);
	}

	@DataProvider
	private static Object[][] trimExchangeRateDP() {
		return new Object[][] { { "0.123456789012", "0.1234567890" } };
	}

	@Test(dataProvider = "trimFeeAuditTrailDP")
	public void trimFeeAuditTrail(String inputFeeAuditTrail, String expectedFeeAuditTrail) {
		Assert.assertEquals(FXDataTrimmer.trimFeeAuditTrail(inputFeeAuditTrail), expectedFeeAuditTrail);
	}

	@DataProvider
	private static Object[][] trimFeeAuditTrailDP() {
		return new Object[][] {
				{ "V:6:USD:10000;B:10285:320:0.02900:30::30000:30:0.02900::::::0:::::::;max:15695:30000:;",
						"V:6:USD:10000;B:10285:320:0.02900..." },
				{ null, null},
				{"", ""},
				{"V:6:USD:10000;B", "V:6:USD:10000;B"}};
	}
}
