package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;
import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.money.FulfillBulkOrderRequest;
import com.jptest.money.FulfillBulkOrderResponse;
import com.jptest.money.FulfillPaymentRequest;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.FulfillmentServ;
import com.jptest.money.MlpFinalizeRequest;
import com.jptest.money.MlpFinalizeResponse;
import com.jptest.money.PayV2Request;
import com.jptest.money.PayV2Response;
import com.jptest.money.PaymentServ;

/**
 * Represents bridge for paymentserv_ca API calls
 */
@Singleton
public class PaymentServCABridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServCABridge.class);

    @Inject
    @Named("paymentserv_ca")
    private FulfillmentServ paymentserv_ca;

	@Inject
	@Named("paymentservca")
	private PaymentServ paymentserv;

    public FulfillPaymentResponse fulfillPayment(final FulfillPaymentRequest request) {
        LOGGER.info("fulfill_payment request: {}", printValueObject(request));
        final FulfillPaymentResponse response = this.paymentserv_ca.fulfill_payment(request);
        LOGGER.info("fulfill_payment response: {}", printValueObject(response));
        return response;
    }

    public FulfillBulkOrderResponse processFulfillBulkOrder(FulfillBulkOrderRequest request) {
        LOGGER.info("processFulfillBulkOrder request: {}", printValueObject(request));
        FulfillBulkOrderResponse response = this.paymentserv_ca.fulfill_bulk_order(request);
        LOGGER.info("processFulfillBulkOrder response : {}", printValueObject(response));
        return response;
    }
    
    public MlpFinalizeResponse processMlpFinalize(MlpFinalizeRequest request) {
        LOGGER.info("processMLPFinalize request: {}", printJsonObject(request));
        MlpFinalizeResponse response = this.paymentserv_ca.mlp_finalize(request);
        LOGGER.info("processMLPFinalize response : {}", printJsonObject(response));
        return response;
    }

	public PayV2Response payV2(PayV2Request request) {
		LOGGER.info("payV2 request: {}", printValueObject(request));
		PayV2Response response = this.paymentserv.pay_v2(request);
		LOGGER.info("payV2 response : {}", printValueObject(response));
		return response;
	}
}
