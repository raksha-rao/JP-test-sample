package com.jptest.payments.fulfillment.testonia.business.component.postpay;

import com.jptest.payments.fulfillment.testonia.business.component.PostPaymentOperations;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;

/**
 * BasePostPaymentOperationTask Represents the base post payment operation task, which calls corresponding post payment
 * operation task, like RefundPaymentOperaitonTask based on the instance it invokes in ExecutionUnitFactory
 *
 * @JP Inc.
 * @param <T>
 * @param <S>
 */
public abstract class BasePostPaymentOperationTask<T, S> extends BaseTask<S>
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
            if (isSuccess(context, response)) {
                this.populateActivityId(context, response);
                context.addReportingAttribute(this.getActivityIdKey(),
                		String.valueOf(context.getData(this.getActivityIdKey())));
                this.populateIpnEncryptedId(context, response);
            }
        } catch (Exception e) {
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

    /*
     * Populate the activity id. Transaction id is returned in the response in most cases and can be used to retrieve
     * activity id. Its delegated to individual operation to populate activity accordingly.
     */
    protected void populateActivityId(final Context context, final S response) {
    }

    public String getActivityIdKey() {
        if (this.request instanceof PostPaymentRequest) {
            return PostPaymentOperations.getActivityIdKey(((PostPaymentRequest) this.request).getRequest());
        } else {
            return PostPaymentOperations.getActivityIdKey(this.request);
        }
    }

    private static class PostPaymentOperationException extends TestExecutionBusinessException {

        /**
         * @param message
         */
        public PostPaymentOperationException(String message, Throwable e) {
            super(message, e);
        }

        @Override
        public TestoniaExceptionReasonCode getReasonCode() {
            return TestoniaExceptionReasonCode.FAILURE_POST_PAYMENT_SETUP;
        }

    }
    
    /**
     * Checks whether particular operation returned success/failure response
     * 
     * @return true/false based on whether operation returned success/failure response
     */
    protected boolean isSuccess(Context context, S response) {
    	if (((PostPaymentRequest) this.request).getReturnCode().equals("0")
                || ((PostPaymentRequest) this.request).getReturnCode().equals("true")) {
    		return true;
    	}
    	return false;
    }

    /**
     * Populate the IPN Encrypted Id to do IPN validation.
     * Its delegated to individual operation to populate ContextKeys.IPN_ENCRYPTED_ID_KEY accordingly.
     *
     * Defaulted to remove this key, so that it will fail at IPN validation.
     *
     * Options:
     * - override this method to set the correct encrypted id in the Context key (or)
     * - remove the ipnValidation from the json input.
     */
    protected void populateIpnEncryptedId(final Context context, final S response) {
        // When overriding this method, please add this line at the start
        context.removeData(ContextKeys.IPN_ENCRYPTED_ID_KEY.getName());
    }
}
