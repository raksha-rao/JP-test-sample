package com.jptest.payments.fulfillment.testonia.business.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.jptest.money.PaymentExternalOpaqueDataVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.money.AmqTxnDao;
import com.jptest.vo.serialization.UniversalDeserializer;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This is a refactored DoRTAssistant.java from bluefin - it provides RT related utility operations
 * 
 * This class helps in validating Reference (recurring) transaction.
 * e.g. if billing_agreement is present or not, if merchant_pull is updated or not.
 * 
 */
@Singleton
public class DoRTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoRTService.class);

    private final AmqTxnDao amqTxnDao;

    private final AMQAssistant amqAssistant;

    private final XMLHelper xmlHelper;

    @Inject
    public DoRTService(AmqTxnDao amqTxnDao,
                    AMQAssistant amqAssistant,
                    XMLHelper xmlHelper) {
        this.amqTxnDao = checkNotNull(amqTxnDao);
        this.amqAssistant = checkNotNull(amqAssistant);
        this.xmlHelper = checkNotNull(xmlHelper);
    }

    public String getDebitMessageID() {
        String debitMessageId = amqTxnDao.getSmallestMessageID();
        LOGGER.info("get Debit message ID: {}", debitMessageId);
        return debitMessageId;
    }

    public String getCreditMessageID() {
        String creditMessageId = amqTxnDao.getLargestMessageID();
        LOGGER.info("get Credit message ID: {}", creditMessageId);
        return creditMessageId;
    }

    public void validateAmq(String messageId, String baId,
            String billingAgreementHandle,
            String isMerchantPullUpdatesSkipped, String isMozartFlow,
            String postProcessMessageType, String invoiceId, boolean is_p10)
                    throws Exception {
        String payload = amqAssistant.getPayloadStringByMsgId(messageId);

        // validate baId
        if (!baId.isEmpty()) {
            String retVal_ba_id = xmlHelper.getResultByTagElementName(payload,
                    "invoice", "ba_id");
            LOGGER.info("returned value for msg : {}", retVal_ba_id);
            Assert.assertEquals(retVal_ba_id, baId);
        }

        // validate billingAgreementHandle
        if (!billingAgreementHandle.isEmpty()) {
            String retVal_billing_agreement_handle = xmlHelper.getResultByTagElementName(payload, "invoice",
                    "billing_agreement_handle");
            LOGGER.info("returned value for billing agreement handle : {}", retVal_billing_agreement_handle);
            Assert.assertEquals(retVal_billing_agreement_handle, billingAgreementHandle);
        }

        // validate isMerchantPullUpdatesSkipped
        if (isMerchantPullUpdatesSkipped.equalsIgnoreCase("true")
                && !is_p10) {
            String actualIsMerchantPullUpdatesSkipped = xmlHelper.getResultByTagElementName(payload, "invoice",
                    "is_merchant_pull_updates_skipped");
            Assert.assertEquals(actualIsMerchantPullUpdatesSkipped, isMerchantPullUpdatesSkipped);
        }

        // validate isMozartFlow
        String retVal_is_mozart = xmlHelper.getResultByTagElementName(payload, postProcessMessageType,
                "is_mozart_flow");
        LOGGER.info("returned value for mozart flow : {}", retVal_is_mozart);
        Assert.assertEquals(retVal_is_mozart, isMozartFlow);

        // validate invoiceId
        String retVal_invoice_id = xmlHelper.getResultByTagElementName(payload, "invoice", "invoice_id");
        LOGGER.info("returned value for invoice_id : {}", retVal_invoice_id);
        Assert.assertEquals(retVal_invoice_id, invoiceId);

        // validate payment_extensions
        String retVal_payment_extensions = xmlHelper.getResultByTagName(payload,
                "class_name");
        LOGGER.info("returned value for payment_extensions : {}", retVal_payment_extensions);
        Assert.assertEquals(retVal_payment_extensions, "Money::PaymentExternalOpaqueDataVO");
    }

    public void validateFailedAmqMessage(String message_id,
            String sender_account_number, String recipient_account_number,
            String legacy_pimp_rc, String invoice,
            String post_process_message_type,
            String is_ca_path_for_p20_payment, String is_mozart_flow)
                    throws Exception {
        String payload = amqAssistant.getPayloadStringByMsgId(message_id);

        // validate sender_account_number
        String retVal_sender_account_number = xmlHelper.getResultByTagElementName(payload,
                post_process_message_type, "sender_account_number");
        LOGGER.info("returned value for sender account number : {}", retVal_sender_account_number);
        Assert.assertEquals(retVal_sender_account_number, sender_account_number);

        // validate recipient_account_number
        String retVal_recipient_account_number = xmlHelper.getResultByTagElementName(payload,
                post_process_message_type, "recipient_account_number");
        LOGGER.info("returned value for recipient account number : {}", retVal_recipient_account_number);
        Assert.assertEquals(retVal_recipient_account_number, recipient_account_number);

        // validate legacy_pimp_rc
        String retVal_legacy_pimp_rc = xmlHelper.getResultByTagElementName(payload,
                post_process_message_type, "legacy_pimp_rc");
        LOGGER.info("returned value for legacy pimp rc : {}", retVal_legacy_pimp_rc);
        Assert.assertEquals(retVal_legacy_pimp_rc, legacy_pimp_rc);

        // validate invoice
        String retVal_invoice_id = xmlHelper.getResultByTagElementName(
                payload, post_process_message_type, "invoice_id");
        LOGGER.info("returned value for invoice_id : {}", retVal_invoice_id);
        Assert.assertEquals(retVal_invoice_id, invoice);

        // validate is_ca_path_for_p20_payment
        String retVal_is_ca_path_for_p20_payment = xmlHelper.getResultByTagElementName(payload,
                post_process_message_type, "ca_path_for_p20_payment");
        LOGGER.info("returned value for ca_path_for_p20_payment : {}", retVal_is_ca_path_for_p20_payment);
        Assert.assertEquals(retVal_is_ca_path_for_p20_payment, legacy_pimp_rc);

        String retVal_is_mozart_flow = xmlHelper.getResultByTagElementName(payload,
                post_process_message_type, "is_mozart_flow");
        LOGGER.info("returned value for is_mozart_flow : {}", retVal_is_mozart_flow);
        Assert.assertEquals(retVal_is_mozart_flow, is_mozart_flow);

        String retVal_payment_extensions = xmlHelper.getResultByTagName(payload, "class_name");
        LOGGER.info("returned value for payment extensions : {}", retVal_payment_extensions);
        Assert.assertEquals(retVal_payment_extensions, "Money::PaymentExternalOpaqueDataVO");
    }

    public void doPaymentExtensionValidation(
            GetLegacyEquivalentByPaymentReferenceResponse apiResponse,
            String client_payment_id, String billing_agreement_handle)
                    throws IOException {

        if (apiResponse.getLegacyEquivalent().getPaymentExtensionsList() == null
                || apiResponse.getLegacyEquivalent().getPaymentExtensionsList().isEmpty()) {
            Assert.fail("Money::PaymentInternalOpaqueDataVO validation failed");
        }

        Assert.assertEquals(apiResponse.getLegacyEquivalent()
                .getPaymentExtensionsList().get(0).getPaymentExtensions()
                .get(0).getClassName(),
                "Money::PaymentExternalOpaqueDataVO",
                "Expected to have Money::PaymentExternalOpaqueDataVO but actual is"
                        + apiResponse.getLegacyEquivalent()
                                .getPaymentExtensionsList().get(0)
                                .getPaymentExtensions().get(0)
                                .getClassName());
        LOGGER.info(
                "Payment Extension VO size is: {}",
                apiResponse.getLegacyEquivalent().getPaymentExtensionsList().size());
        UniversalDeserializer deserializer = new UniversalDeserializer();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                apiResponse.getLegacyEquivalent()
                        .getPaymentExtensionsList().get(0)
                        .getPaymentExtensions().get(0).getSerializedData());

        PaymentExternalOpaqueDataVO paymentExternalOpaqueData = (PaymentExternalOpaqueDataVO) deserializer
                .deserialize(inputStream);
        String client_pay_id = paymentExternalOpaqueData
                .getClientPaymentId();
        LOGGER.info("client_payment_id: {}", client_pay_id);
        Assert.assertEquals(client_pay_id, client_payment_id,
                "Expected " + client_payment_id + " but found "
                        + client_pay_id);

        String bill_agreement_handle = paymentExternalOpaqueData
                .getBillingAgreementHandle();
        LOGGER.info("bill_agreement_handle: {}", bill_agreement_handle);
        Assert.assertEquals(bill_agreement_handle,
                billing_agreement_handle, "Expected "
                        + billing_agreement_handle + " but found "
                        + bill_agreement_handle);
    }

}
