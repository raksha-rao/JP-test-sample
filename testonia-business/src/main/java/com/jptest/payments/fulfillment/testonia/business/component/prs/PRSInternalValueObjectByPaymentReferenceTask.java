/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.business.component.prs;

import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;

/**
 * @JP Inc.
 *
 */
public class PRSInternalValueObjectByPaymentReferenceTask extends
        PaymentReadBaseTask<GetLegacyEquivalentByPaymentReferenceRequest, GetLegacyEquivalentByPaymentReferenceResponse> {

    public PRSInternalValueObjectByPaymentReferenceTask(String writeDir, String fileName,
            PaymentReferenceTypeCode refTypeCode, String value, boolean persistOutput) {
        this.writeDir = writeDir;
        this.fileName = fileName;
        this.persistOutPut = persistOutput;
        this.apiKey = PRSApiTypeCode.IVOBPR;
        this.refCode = refTypeCode;
        this.referenceValue = value;
    }

    @Override
    public GetLegacyEquivalentByPaymentReferenceRequest constructPRSRequest(PaymentReferenceTypeCode refTypeCode,
            String value) {
        GetLegacyEquivalentByPaymentReferenceRequest request = new GetLegacyEquivalentByPaymentReferenceRequest();
        PaymentSideReferenceVO reference = new PaymentSideReferenceVO();
        reference.setReferenceType(refTypeCode);
        reference.setReferenceValue(value);
        request.setPaymentReference(reference);
        return request;
    }

    @Override
    public GetLegacyEquivalentByPaymentReferenceResponse executePrs(
            GetLegacyEquivalentByPaymentReferenceRequest request) {
        return bridge.getLegacyEquivalentByPaymentReference(request);
    }
}
