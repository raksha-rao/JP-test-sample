package com.jptest.payments.fulfillment.testonia.business.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.money.CompletePendingPaymentReversalRequest;
import com.jptest.money.CreatePendingPaymentReversalRequest;
import com.jptest.money.PostBankReturnRequest;
import com.jptest.money.RefundPaymentRequest;
import com.jptest.money.ReversePaymentRequest;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.CancelPendingReversalOperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.p10.BasePostPayment10OperationTask;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.p10.CompletePendingReversalOperation10Task;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.p10.CreatePendingReversalOperation10Task;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.p10.PostBankReturnOperation10Task;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.p10.ReversePaymentOperation10Task;
import com.jptest.payments.fulfillment.testonia.business.component.postpay.p10.RefundPaymentOperation10Task;

/**
 * PostPaymentOperaitons enum takes care of linking the corresponding post payment operation task for a given post
 * payment request This task is specific for 1.0 operation
 *
 * @JP Inc.
 */
public enum P10PostPaymentOperations {

    REFUND_PAYMENT(RefundPaymentRequest.class, RefundPaymentOperation10Task.class),
    REVERSE_PAYMENT(ReversePaymentRequest.class, ReversePaymentOperation10Task.class),
    CREATE_PENDING_PAYMENT_REVERSAL_REQUEST(
            CreatePendingPaymentReversalRequest.class,
            CreatePendingReversalOperation10Task.class),
    COMPLETE_PENDING_PAYMENT_REVERSAL_REQUEST(
            CompletePendingPaymentReversalRequest.class,
            CompletePendingReversalOperation10Task.class),
    POST_BANK_RETURN(PostBankReturnRequest.class, PostBankReturnOperation10Task.class);

    private Class<?> postPaymentRequest;
    private Class<? extends BasePostPayment10OperationTask<?, ?>> operationTask;
    private static final String ACTIVITY_ID = "_activityId";
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelPendingReversalOperationTask.class);

    private P10PostPaymentOperations(final Class<?> postPayRequest,
            final Class<? extends BasePostPayment10OperationTask<?, ?>> operationTask) {
        this.postPaymentRequest = postPayRequest;
        this.operationTask = operationTask;
    }

    public static BasePostPayment10OperationTask<?, ?> getOperationTask(final Object postPaymentRequest)
            throws UnsupportedOperationException {

        for (final P10PostPaymentOperations postPaymentOperation : P10PostPaymentOperations.values()) {

            if (postPaymentOperation.postPaymentRequest.isInstance(postPaymentRequest)) {

                try {
                    return postPaymentOperation.operationTask.newInstance();
                }
                catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.error("Instantiation Exception" + e);
                }

            }
        }
        throw new UnsupportedOperationException("Instantiation Exception");
    }

    public static String getActivityIdKey(final Object postPaymentRequest) {
        return postPaymentRequest.getClass().getSimpleName() + ACTIVITY_ID;
    }

    public static String getActivityIdKey(final Class<?> postPaymentRequest) {
        return postPaymentRequest.getSimpleName() + ACTIVITY_ID;
    }

    public Class<?> getPostPaymentRequest() {
        return this.postPaymentRequest;
    }

}
