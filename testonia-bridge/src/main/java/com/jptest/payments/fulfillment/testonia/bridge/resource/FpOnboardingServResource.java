package com.jptest.payments.fulfillment.testonia.bridge.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jptest.financialproduct.creditapispec.v1.onboarding.deferredpayments.DeferredPaymentApplication;

@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public interface FpOnboardingServResource {

	// fponboardingserv_r
    // URI_PATH = "https://msmaster.qa.jptest.com:13274/v1/credit/applications-deferred-payments";
	// HEADER:
	// X-jptest-SECURITY-CONTEXT --> {"actor" : {"auth_claims" : ["USERNAME","PASSWORD"]}}
	// BODY:
	//	{ 
	//		"user_info" :  
	//		{ 
	//			"account_number" :  "XXXXXX",
	//			"address" :  
	//			{
	//				"country_code" :  "DE"
	//			}, 
	//		},
	//		"business_case" :  "STANDALONE" ,
	//		"product_type" :  "PAD",
	//		"channel" : "jptest_DOT_COM",
	//		"product_currency_code" :  "EUR",
	//		"set_default_payment_method" : "on"
	//	}

	String SEC_CTX_HEADER = "X-jptest-SECURITY-CONTEXT";

	@Path("/v1/credit/applications-deferred-payments")
	@POST
	Response enrollPAD(DeferredPaymentApplication request, @HeaderParam(SEC_CTX_HEADER) String securityContext);
}
