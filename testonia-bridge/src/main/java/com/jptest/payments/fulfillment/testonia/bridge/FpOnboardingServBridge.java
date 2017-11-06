package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.inject.name.Named;
import com.jptest.appplatform.commoncomponentsspecification.Address;
import com.jptest.financialproduct.creditapispec.v1.onboarding.deferredpayments.DeferredPaymentApplication;
import com.jptest.financialproduct.creditapispec.v1.onboarding.deferredpayments.UserInfo;
import com.jptest.financialproduct.creditapispec.v2.common.BusinessCase;
import com.jptest.financialproduct.creditapispec.v2.common.Channel;
import com.jptest.financialproduct.creditapispec.v2.common.FinancialProductType;
import com.jptest.payments.fulfillment.testonia.bridge.resource.FpOnboardingServResource;
import com.jptest.platform.security.securitycontext.SecurityContext;
import com.jptest.platform.security.securitycontext.Subject;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.sv.api.rest.util.SecurityContextUtil;

/**
 * Represents a bridge for fponboardingserv API calls
 */
@Singleton
public class FpOnboardingServBridge {

	private static final Logger LOGGER = LoggerFactory.getLogger(FpOnboardingServBridge.class);

	@Inject
	@Named("fponboardingserv_r")
	private FpOnboardingServResource fpOnboardingServResource;

	/**
	 * Enroll user for PAD and make it the default wallet instrument by
	 * enrolling account for deferred payment
	 *
	 * @param accountNumber
	 * @return
	 */
	public void enrollPAD(final User buyer) {
		String accountNumber = buyer.getAccountNumber();
		LOGGER.info("Attempting to enroll-PAD for account : {}", accountNumber);

		String context = getSecurityContext(accountNumber);
		DeferredPaymentApplication request = generateEnrollPadRequest(buyer);
		LOGGER.info("Request_Body_JSON : {} ", printJsonObject(request));

		Response response = fpOnboardingServResource.enrollPAD(request, context);
		LOGGER.info("Response_JSON : {} ", printJsonObject(response));

		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			LOGGER.info("Enroll PAD - SUCCESS for account : {}", accountNumber);
		} else {
			LOGGER.info("Enroll PAD - FAILED for account: {}, reason: {}", accountNumber, response.getStatus());
		}
	}

	/**
	 * Method to generate security context (SecurityContext)
	 *
	 * @param accountNumber
	 * @return
	 */
	private String getSecurityContext(String accountNumber) {
		com.jptest.platform.security.securitycontext.User actor = new com.jptest.platform.security.securitycontext.User();
		actor.setAccountNumber(accountNumber);
		// actor.setPartyId(acctNumber);
		actor.setAuthClaims(Lists.newArrayList("USERNAME", "PASSWORD"));
		// actor.setUserType("MERCHANT");
		// actor.setAuthState("LOGGEDIN");

		Subject subject = new Subject();
		subject.setSubject(actor);

		SecurityContext context = new SecurityContext();
		context.setActor(actor);
		context.setSubjects(Lists.newArrayList(subject));

		String retVal = null;
		try {
			retVal = SecurityContextUtil.serializeSecurityContext(context);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error occurred converting SecurityContext to String => ", e);
		}

		LOGGER.info("Header_SecurityContext_JSON : {} ", printJsonObject(context));
		return retVal;
	}

	/**
	 * Method to generate DeferredPaymentApplication object
	 *
	 * @param accountNumber
	 * @return
	 */
	private DeferredPaymentApplication generateEnrollPadRequest(final User buyer) {
		Address address = new Address();
		address.setCountryCode(buyer.getCountry());

		UserInfo userInfo = new UserInfo();
		userInfo.setAccountNumber(buyer.getAccountNumber());
		userInfo.setAddress(address);

		DeferredPaymentApplication deferredRequest = new DeferredPaymentApplication();
		deferredRequest.setUserInfo(userInfo);
		deferredRequest.setBusinessCase(BusinessCase.STANDALONE);
		deferredRequest.setProductType(FinancialProductType.PAD);
		deferredRequest.setChannel(Channel.jptest_DOT_COM);
		deferredRequest.setProductCurrencyCode(buyer.getCurrency());
		deferredRequest.setAdditionalProperty("set_default_payment_method", "on");

		return deferredRequest;
	}
}
