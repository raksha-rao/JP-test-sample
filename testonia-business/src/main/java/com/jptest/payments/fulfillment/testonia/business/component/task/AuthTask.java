package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;
import com.jptest.money.AuthRequest;
import com.jptest.money.AuthResponse;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;


/**
 * @JP Inc.
 *
 * Calls auth operation with the request built by AuthRequestBuilderTask
 *
 */
public class AuthTask extends BaseTask<AuthResponse> {

    @Inject
    private PaymentServBridge paymentservBridge;

    @Override
    public AuthResponse process(Context context) {

        AuthRequest authRequest = (AuthRequest) context.getData(ContextKeys.AUTH_REQUEST.getName());
        AuthResponse authResponse = paymentservBridge.auth(authRequest);
        
        return authResponse;

    }

}
