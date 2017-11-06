package com.jptest.payments.fulfillment.testonia.bridge;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import com.jptest.qi.rest.domain.pojo.CreditCardMill;
import com.jptest.qi.rest.domain.pojo.Creditcard;
import com.jptest.qi.rest.service.CreditCardMillService;


/**
 * Represents a single point for all external calls that relate to operations for generating FI from Testonia.
 */

@Singleton
public class FIBridge {

    @Inject
    @Named("ccMillService")
    protected CreditCardMillService ccMillService;

    public Creditcard genrateCreditCard(CreditCardMill creditcardmill) {
        Response creditcard = ccMillService.generateCreditCard(creditcardmill);
        Creditcard cc = creditcard.readEntity(Creditcard.class);
        return cc;
    }

}
