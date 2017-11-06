package com.jptest.payments.fulfillment.testonia.business.amqdaemon;

import java.math.BigInteger;
import java.util.Base64;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.google.protobuf.Message;
import com.jptest.fulfillmentengine.ActivitySweptMessage;
import com.jptest.infra.amq.publisher.exception.TransportException;
import com.jptest.infra.amq.subscriber.MessageID;
import com.jptest.money.BankPostingEventMessage;
import com.jptest.payments.fulfillment.testonia.business.guice.DefaultTestoniaGuiceModule;
import com.jptest.payments.fulfillment.testonia.dao.AMQEnqueueDao;
import com.jptest.payments.fulfillment.testonia.model.finsys.SetTxnRecClearingDTO;
import com.jptest.txn.settlement.proto.ClearingResponsePb;
import com.jptest.txn.settlement.proto.ClearingResponsePb.ClearingResponse.Processor;
import com.jptest.txn.settlement.proto.ClearingResponsePb.ClearingResponse.Response;
import com.jptest.txn.settlement.proto.ClearingResponsePb.ClearingResponse.Response.ResponseType;
import com.jptest.txn.settlement.proto.ClearingResponsePb.ClearingResponse.Transaction;
import com.jptest.txn.settlement.proto.CommonPb;
import com.jptest.txn.settlement.proto.CommonPb.Currency;
import com.jptest.txn.settlement.proto.CommonPb.MoneyMovementType;

/**
 * Sample test that validates AMQ payload publishing logic
 * 
 * Test for {@link AMQPublisher}
 */
@Guice(modules = DefaultTestoniaGuiceModule.class)
public class AMQPublisherTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AMQPublisherTest.class);

    @Inject
    private AMQEnqueueDao dao;

    @Inject
    private Configuration config;

    @Test(enabled = false)
    public void testActivitySweptMessage() throws TransportException {
        AMQPublisher amqPublisher = new AMQPublisher("ENG:ENG01", dao, config);
        ActivitySweptMessage activitySweptMessage = new ActivitySweptMessage();
        activitySweptMessage.setActivityId(12345678L);

        MessageID messageID = amqPublisher.sendMessage(activitySweptMessage);
        LOGGER.info("MessageID for ActivitySweptMessage:{}", messageID);

        Assert.assertNotNull(messageID, "messageID should not be null");
    }

    /**
     * https://github.jptest.com/Settlement-R/batch-stlmnt-bk-ret-clr-procsr/blob/develop/src/main/java/com/jptest/bank/inbound/processor/components/AMQReturnClearingMessagePublisher.java
     */
    @Test(enabled = true)
    public void testBankPostingEventMessage() throws TransportException {
        AMQPublisher amqPublisher = new AMQPublisher("Money_MDP", dao, config);
        BankPostingEventMessage vo = new BankPostingEventMessage();
        ClearingResponsePb.ClearingResponse message = buildProto(build());
        vo.setClearingResponse(convertProtoObjToBase64EncodeString(message));
        vo.setInstructionId(null);

        MessageID messageID = amqPublisher.sendMessage(vo);
        LOGGER.info("MessageID for BankPostingEventMessage:{}", messageID);

        Assert.assertNotNull(messageID, "messageID should not be null");
    }

    public static String convertProtoObjToBase64EncodeString(Message protoMsg) {
        return String.valueOf(Base64.getEncoder().encodeToString(protoMsg.toByteArray()));
    }

    private static SetTxnRecClearingDTO build() {
        SetTxnRecClearingDTO dto = new SetTxnRecClearingDTO();
        dto.setTxnRef("18953420095143065:23:FSM:0");
        dto.setSetTxnReqTid(new BigInteger("106618890926168943"));
        dto.setIntmAgtRef("1000000004006");
        dto.setTxnAmt(new BigInteger("2000"));
        dto.setTxnCurCd("EUR");
        dto.setTxnTm(new BigInteger("1489448703"));
        dto.setMoneyMvmntType("MONEY_MOVEMENT_TYPE_DEBIT");
        dto.setBankCountry("DE");
        dto.setProcId("PROCESSOR_ID_DBAG");
        dto.setBankName("Rabobank Nederland");
        return dto;
    }

    private ClearingResponsePb.ClearingResponse buildProto(SetTxnRecClearingDTO setTxnRecClearing) {
        String requestId = String.valueOf(setTxnRecClearing.getTxnRef());
        String instructionId = "54M96441W01953947"; //CryptoEncoder.encrypt(String.valueOf(setTxnRecClearing.getMoneyMvmntId()));
        String settlementId = String.valueOf(setTxnRecClearing.getSetTxnReqTid());
        String settlementExternalReference = String.valueOf(setTxnRecClearing.getIntmAgtRef());

        return ClearingResponsePb.ClearingResponse.newBuilder()
                .setResponse(populateClearingResponseDetails())
                .setRequestId(requestId).setTransaction(getTransactionDetails(setTxnRecClearing))
                .setProcessor(getProcessorDetails(setTxnRecClearing))
                .setInstructionId(instructionId).setSettlementId(settlementId)
                .setSettlementExternalReference(settlementExternalReference).build();
    }

    private Response populateClearingResponseDetails() {
        Response.Builder clrResponseBuilder = Response.newBuilder();
        clrResponseBuilder.setResponseType(ResponseType.RESPONSE_TYPE_CLEARED);
        clrResponseBuilder.setResponseTypeValue(ResponseType.RESPONSE_TYPE_CLEARED_VALUE);
        return clrResponseBuilder.build();
    }

    /**
     * Get populated transaction details
     * 
     * @param responseData
     * @return
     */
    private Transaction getTransactionDetails(SetTxnRecClearingDTO setTxnRecClearing) {

        Transaction.Builder transactionBuilder = Transaction.newBuilder();
        if (setTxnRecClearing != null) {

            if (setTxnRecClearing.getTxnAmt() != null) {
                Currency.Builder txnAmount = Currency.newBuilder();
                txnAmount.setAmount(setTxnRecClearing.getTxnAmt().longValue());
                txnAmount.setCurrencyCode(setTxnRecClearing.getTxnCurCd());
                transactionBuilder.setAmount(txnAmount);
            }

            if (setTxnRecClearing.getTxnTm() != null) {
                transactionBuilder.setTxnDate(setTxnRecClearing.getTxnTm().longValue());
            }

            if (setTxnRecClearing.getMoneyMvmntType() != null) {
                transactionBuilder
                        .setMoneyMovementType(MoneyMovementType.valueOf(setTxnRecClearing.getMoneyMvmntType()));
            }
            transactionBuilder.setBankCountry(setTxnRecClearing.getBankCountry());
        }

        return transactionBuilder.build();
    }

    private Processor getProcessorDetails(SetTxnRecClearingDTO setTxnRecClearing) {
        if (setTxnRecClearing != null) {
            Processor.Builder processor = Processor.newBuilder();
            if (setTxnRecClearing.getProcId() != null) {
                processor.setProcessorId(CommonPb.ProcessorId.valueOf(setTxnRecClearing.getProcId()));
            }
            processor.setCountry(setTxnRecClearing.getBankCountry());
            if (setTxnRecClearing.getBankName() != null) {
                processor.setName(setTxnRecClearing.getBankName());
            }
            return processor.build();
        } else {
            return null;
        }
    }

}
