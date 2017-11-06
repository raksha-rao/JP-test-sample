package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.jptest.money.PaymentStrategyVO;
import com.jptest.money.PlanPaymentV2Request;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;


public class PlanPaymentV2RequestBuilderTest {

    @Test
    public void testCreate() {
        Assert.assertNotNull(PlanPaymentV2RequestBuilder.newBuilder());
    }

    @Test
    public void testSimpleBuild() {
        PlanPaymentV2Request rq = PlanPaymentV2RequestBuilder.newBuilder().strategy(new VOBuilder<PaymentStrategyVO>() {

            @Override
            public PaymentStrategyVO build() {
                return new PaymentStrategyVO();
            }


        }).clientActor(ActorInfoVOBuilder.newBuilder(BigInteger.ZERO)).build();
        Assert.assertNotNull(rq);
    }

    @Test(enabled = false)
    public void testElvPlanRequest() {

        
        BigInteger senderAccountNumber = new BigInteger("123456789");
        BigInteger recipientAccountNumber = new BigInteger("987654321");
        String recipientEmail = "akokarski@jptest.com";
        String senderEmail = "akokarski-sender@jptest.com";
        
        User sender = new User();
        sender.setAccountNumber(senderAccountNumber.toString());
        sender.setEmailAddress(senderEmail);
        sender.setHomePhoneNumber("612-977-0663");
        sender.setHomeAddress1("Alter Neunkirchener Weg 12244039");
        sender.setHomeAddress2("ESpachstr. 20867418a");
        sender.setHomeCity("Freiburg");
        sender.setHomeZip("79111");
        sender.setCountry("DE");
        
        
        User recipient = new User();
        recipient.setAccountNumber(recipientAccountNumber.toString());
        recipient.setEmailAddress(recipientEmail);        
        recipient.setHomeAddress1("Prinzregentenstr 52582195");
        recipient.setHomeAddress2("Alter Neunkirchener Weg 75031660");
        recipient.setHomeCity("Freiburg");
        recipient.setHomeZip("79111");
        recipient.setHomeCountry("DE");
        
        Currency total = Currency.parseCurrency("EUR1050");
        PlanPaymentV2RequestBuilder planBuilder = PlanPaymentV2RequestBuilder
                .newBuilder()
                .strategy(
                        PaymentStrategyVOBuilder
                                .newBuilder()
                                .recipient(PartyVOsBuilder.recipient(recipientAccountNumber, recipientEmail))
                                .sender(PartyVOsBuilder.sender(senderAccountNumber))
                                .transaction(
                                        TransactionUnitVOBuilder
                                                .newBuilder()
                                                .total(total)
                                                .sender(senderAccountNumber)
                                                .recipient(recipientAccountNumber, recipientEmail)
                                                .purchaseContext(
                                                        PurchaseContextVOBuilder
                                                                .newBuilder(
                                                                        "/vo_mocks/com/jptest/payments/txnfulfillmentserv/elv/OnjpincELVFulfillPaymentTest/PurchaseContext.vo")
                                                                .sender(sender)
                                                                .recipient(recipient)
                                                                .invoice("12341234").total(total))))
                .clientActor(ActorInfoVOBuilder.newBuilder(senderAccountNumber));

        System.out.println(VoHelper.printValueObject(planBuilder.build()));
    }
}
