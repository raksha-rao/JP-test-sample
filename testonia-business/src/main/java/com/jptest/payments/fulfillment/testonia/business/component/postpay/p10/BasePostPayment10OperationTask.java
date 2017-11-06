package com.jptest.payments.fulfillment.testonia.business.component.postpay.p10;

import com.jptest.payments.fulfillment.testonia.business.component.PostPaymentOperations;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.PostPaymentOperationsTask;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;


/**
 * BasePostPayment10OperationTask Represents the base post payment operation task specific to 1.0 stack, which calls
 * corresponding post payment operation task, like RefundPaymentOperation10Task based on the instance it invokes in
 * ExecutionUnitFactory
 *
 * @JP Inc.
 * @param <T>
 * @param <S>
 */
public abstract class BasePostPayment10OperationTask<T, S> extends BaseTask<S>
        implements PostPaymentOperationsTask<T, S> {

    T request;

    @SuppressWarnings("unchecked")
    public void setRequest(final Object request) {
        this.request = (T) request;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S process(final Context context) {
        S response = null;

        try {
            final T request = this.constructPostPayRequest((T) ((PostPaymentRequest) this.request).getRequest(),
                    context);
            response = this.executePostPay(request, ((PostPaymentRequest) this.request).isCall2PEX());

            this.assertPostPayResponse(response, (PostPaymentRequest) this.request, context);
        }
        catch (final Exception e) {
            throw new PostPaymentOperationException("Failed during post pay operation:" + e.getMessage(), e);
        }
        return response;
    }

    @Override
    public abstract T constructPostPayRequest(T postPaymentRequest, Context context);

    @Override
    public abstract S executePostPay(T postPayRequest, boolean call2PEX);

    @Override
    public abstract void assertPostPayResponse(S post_pay_response, PostPaymentRequest postPayRequest, Context context);

    public String getActivityIdKey() {
        if (this.request instanceof PostPaymentRequest) {
            return PostPaymentOperations.getActivityIdKey(((PostPaymentRequest) this.request).getRequest());
        }
        else {
            return PostPaymentOperations.getActivityIdKey(this.request);
        }
    }

    private static class PostPaymentOperationException extends TestExecutionBusinessException {

        /**
         * @param message
         */
        public PostPaymentOperationException(final String message, final Throwable e) {
            super(message, e);
        }

        @Override
        public TestoniaExceptionReasonCode getReasonCode() {
            return TestoniaExceptionReasonCode.FAILURE_POST_PAYMENT_SETUP;
        }

    }

}
