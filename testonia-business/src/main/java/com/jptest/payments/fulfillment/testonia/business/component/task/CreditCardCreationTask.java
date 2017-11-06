package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.bridge.FIBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.qi.rest.domain.pojo.CreditCardMill;
import com.jptest.qi.rest.domain.pojo.Creditcard;

/**
 * This task is to generate Credit Card details
 * 
 * @JP Inc.
 *
 */
public class CreditCardCreationTask extends BaseTask<Creditcard> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CreditCardCreationTask.class);

	@Inject
	private FIBridge fiBridge;

	private final String country;
	private final String courrency;
	private final String cardType;

	public CreditCardCreationTask(final String country, final String currency, final String type) {
		this.country = country;
		this.courrency = currency;
		this.cardType = type;
	}

	@Override
	public Creditcard process(final Context context) {
		CreditCardMill creditCardMill = new CreditCardMill();
		creditCardMill.setCountry(country);
		creditCardMill.setCurrency(courrency);
		creditCardMill.setCardType(cardType);
		Creditcard creditcard = fiBridge.genrateCreditCard(creditCardMill);
		LOGGER.info("CreditCard number {}:", creditcard.getCardNumber());
		return creditcard;
	}

}
