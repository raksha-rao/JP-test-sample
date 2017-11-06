package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;
import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printProtobufObject;
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
import com.jptest.payments.fulfillment.testonia.bridge.util.SecurityContextHelper;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage;
import com.jptest.payments.payment_message.PaymentMessagePb.PaymentMessage.MessageTypeCase;
import com.jptest.payments.txnfulfillment.api.v1.PaymentMessageResource;

/**
 * Represents bridge for txnfulfillmentserv API calls
 */
@Singleton
public class TxnFulfillmentServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(TxnFulfillmentServBridge.class);
    private static final String jptest_CLIENT_IPADDRESS = "";
    private static final String jptest_CLIENT_METADATA_ID = "91653bda87a748b2bfd19e117f03c5df";
    private static final String jptest_ENTRY_POINT = "http://uri.jptest.com/Mobile/Server/Mobile/mfsconsumer/Withdrawal";
    private static final String jptest_REMOTE_ADDRESS = "6124916781312933570";
    private static final String jptest_VISITOR_ID = "123456789";

    @Inject
    @Named("txnfulfillmentserv")
    private FulfillmentServ client;

    @Inject
    @Named("paymentmessageresource")
    private PaymentMessageResource paymentmessage;

    public FulfillPaymentResponse fulfillPayment(FulfillPaymentRequest request) {
        LOGGER.info("fulfill_payment request: {}", printValueObject(request));
        FulfillPaymentResponse response = client.fulfill_payment(request);
        LOGGER.info("fulfill_payment response: {}", printValueObject(response));
        return response;
    }

    public PaymentMessage processPaymentMessage(PaymentMessage request) {
        LOGGER.info("processPaymentMessage request: {}", printProtobufObject(request));
        PaymentMessage response = paymentmessage.processPaymentMessage(
                SecurityContextHelper.getDetailedSecurityContext(getAccountNumber(request)), "",
                jptest_CLIENT_IPADDRESS, jptest_CLIENT_METADATA_ID, jptest_ENTRY_POINT, jptest_REMOTE_ADDRESS,
                jptest_VISITOR_ID, request);
        LOGGER.info("processPaymentMessage response : {}", printProtobufObject(response));
        return response;
    }

    public FulfillBulkOrderResponse processFulfillBulkOrder(FulfillBulkOrderRequest request) {
        LOGGER.info("processFulfillBulkOrder request: {}", printJsonObject(request));
        FulfillBulkOrderResponse response = client.fulfill_bulk_order(request);
        LOGGER.info("processFulfillBulkOrder response : {}", printJsonObject(response));
        return response;
    }

    public MlpFinalizeResponse processMlpFinalize(MlpFinalizeRequest request) {
        LOGGER.info("processMLPFinalize request: {}", printJsonObject(request));
        MlpFinalizeResponse response = client.mlp_finalize(request);
        LOGGER.info("processMLPFinalize response : {}", printJsonObject(response));
        return response;
    }
    
    protected String getAccountNumber(PaymentMessage request){
    	if (MessageTypeCase.TRANSFER.equals(request.getMessageTypeCase())){
    		return request.getTransfer().getFrontingInstrument().getUserIdentifier().getAccountNumber();
    	}else if (MessageTypeCase.FINANCIAL_AUTHORIZATION.equals(request.getMessageTypeCase())){
    		return request.getFinancialAuthorization().getSenderParty().getUserId().getAccountNumber();
    	}
    	return "";
    }
    
}
