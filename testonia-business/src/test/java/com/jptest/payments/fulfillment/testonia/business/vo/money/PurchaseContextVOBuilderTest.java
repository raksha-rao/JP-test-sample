package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.jptest.money.PurchaseContextVO;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;


public class PurchaseContextVOBuilderTest {

    @Test
    public void test() throws Exception {

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

        PurchaseContextVOBuilder b = PurchaseContextVOBuilder
                .newBuilder("/PurchaseContext.vo").invoice("123")
                .recipient(recipient).sender(sender).total(Currency.parseCurrency("USD1250"));
        Assert.assertNotNull(b);
        PurchaseContextVO vo = b.build();
        Assert.assertNotNull(vo);

    }

}
