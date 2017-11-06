package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;


/**
 * Interface for all post payment operations tasks
 *
 * @JP Inc.
 * @param <T>
 * @param <S>
 */

public interface PostPaymentOperationsTask<T, S> {

    T constructPostPayRequest(T postPaymentRequest, Context context);

    S executePostPay(T postPayRequest, boolean call2PEX);

    void assertPostPayResponse(S postPaymentResponse, final PostPaymentRequest postPayRequest, Context context);

}
