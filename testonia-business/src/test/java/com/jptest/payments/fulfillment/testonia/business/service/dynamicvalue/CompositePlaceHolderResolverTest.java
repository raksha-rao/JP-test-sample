package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import com.jptest.payments.fulfillment.testonia.business.guice.DefaultTestoniaGuiceModule;
import com.jptest.payments.fulfillment.testonia.core.impl.TestCaseGlobalContext;

/**
 * Test for {@link CompositePlaceHolderResolver}
 */
@Guice(modules = DefaultTestoniaGuiceModule.class)
public class CompositePlaceHolderResolverTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompositePlaceHolderResolverTest.class);

	@Inject
	private CompositePlaceHolderResolver compositePlaceHolderResolver;

	@Test
	public void testPlaceHolderResolver() {
		TestCaseGlobalContext context = new TestCaseGlobalContext();

		String[] placeholders = new String[] {
				// FX related placeholder
				"${FX:AMOUNT-FROM(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}",
				"${FX:AMOUNT-TO(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}",
				"${FX:EXCHANGE-RATE(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}",
				"${FX:AUDIT-TRAIL(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}",
				"${FX:FEE-AUDIT-TRAIL(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}",
				"${FX:ACTUAL-FIXED-FEE(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}",
				"${FX:ACTUAL-PERCENT-FEE(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}",
				"${FX:TOTAL-FEE-AMOUNT(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}",
				"${FX:TOTAL-FEE-AMOUNT(USD,1000,CAD,CA,FALSE,CONVERT_RECEIVE)}",
				// FX-Refund related placeholder
				"${FX-REFUND:AUDIT-TRAIL(USD,1000,CAD,CA,TRUE,CONVERT_SEND)}",
                // FX-interbank related placeholder
                "${FX-INTERBANK:AUDIT-TRAIL(USD,1000,CAD,TRUE,CONVERT_SEND)}",
				// Fee related placeholder
				"${FEE:ACTUAL-FIXED-FEE(CAD,1000,CA,USD,US,RECEIVER,MEFT)}",
				"${FEE:ACTUAL-PERCENT-FEE(CAD,1000,CA,USD,US,RECEIVER,BALANCE)}",
				"${FEE:TOTAL-FEE-AMOUNT(CAD,1000,CA,USD,US,RECEIVER,IACH)}",
				"${FEE:AUDIT-TRAIL(CAD,1000,CA,USD,US,RECEIVER,ECHECK)}",
                "${FEE:AUDIT-TRAIL(AUD,1000,ES,AUD,AU,P2P,ECHECK)}",
                "${FEE:AUDIT-TRAIL(USD,1000,US,USD,US,CHARGEBACK,CHARGE,cb_1000)}" };

		for (String placeholder : placeholders) {
			String output = compositePlaceHolderResolver.resolvePlaceholder(context, placeholder);
			Assert.assertNotNull(output, "for placeholder:" + placeholder);
		}
	}

	@Test
	public void testMathPlaceHolderResolver() {
		TestCaseGlobalContext context = new TestCaseGlobalContext();
		Assert.assertEquals(compositePlaceHolderResolver.resolvePlaceholder(context, "${MATH-BINARY:SUBTRACT-INT(1000, 2000)}"),
				"-1000");
		Assert.assertEquals(compositePlaceHolderResolver.resolvePlaceholder(context, "${MATH-BINARY:SUBTRACT-INT(2000, 1000)}"),
				"1000");
		Assert.assertEquals(compositePlaceHolderResolver.resolvePlaceholder(context, "${MATH-BINARY:SUBTRACT-INT(2000,-1000)}"),
				"3000");
		Assert.assertEquals(compositePlaceHolderResolver.resolvePlaceholder(context, "${MATH-BINARY:ADD-INT(1000,2000)}"),
				"3000");
		Assert.assertEquals(compositePlaceHolderResolver.resolvePlaceholder(context, "${MATH-BINARY:ADD-INT(2000, 1000)}"),
				"3000");
		Assert.assertEquals(compositePlaceHolderResolver.resolvePlaceholder(context, "${MATH-BINARY:ADD-INT(2000,-1000)}"),
				"1000");
		Assert.assertEquals(compositePlaceHolderResolver.resolvePlaceholder(context, "${MATH-UNARY:NEGATE-INT(2000)}"),
				"-2000");
		Assert.assertEquals(compositePlaceHolderResolver.resolvePlaceholder(context, "${MATH-UNARY:NEGATE-INT(-2000)}"),
				"2000");
	}

	@Test
	public void testCombinationOfResolvers() {
		TestCaseGlobalContext context = new TestCaseGlobalContext();
		String toAmount = compositePlaceHolderResolver.resolvePlaceholder(context,
				"${FX:AMOUNT-TO(USD,${MATH-BINARY:SUBTRACT-INT(1000,${FEE:ACTUAL-FIXED-FEE(USD,1000,US,CAD,CA,RECEIVER,ECHECK)})},CAD,CA,FALSE,CONVERT_RECEIVE)}");
		Assert.assertNotNull(toAmount);

		String fee = compositePlaceHolderResolver.resolvePlaceholder(context,
				"${FEE:ACTUAL-FIXED-FEE(USD,1000,US,CAD,CA,RECEIVER,ECHECK)}");
		LOGGER.info("fee:{}", fee);
		Integer amountInt = 1000 - Integer.parseInt(fee);
		String amount = compositePlaceHolderResolver.resolvePlaceholder(context,
				"${FX:AMOUNT-TO(USD," + amountInt + ",CAD,CA,FALSE,CONVERT_RECEIVE)}");
		LOGGER.info("amount:{}", amount);

		Assert.assertEquals(toAmount, amount);

	}

    @Test
    public void testPlaceHolder() {
        List<String> inputList = Arrays.asList("${FEE:AUDIT-TRAIL(USD,1000,US,USD,US,RECEIVER,CHARGE)}");
        TestCaseGlobalContext context = new TestCaseGlobalContext();
        for (String input : inputList) {
            String output = compositePlaceHolderResolver.resolvePlaceholder(context, input);
            LOGGER.info("{} -> {}", input, output);
            Assert.assertNotNull(output);
        }

    }
}
