/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.business.component.prs;

import com.jptest.payments.PaymentReferenceTypeCode;
/**
 * @JP Inc.
 * 
 */
public interface PaymentReadOperationsTask<T, S> {
    
    T constructPRSRequest(PaymentReferenceTypeCode refCode, String value);

    S executePrs(T request);

}
