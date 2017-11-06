/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.business.component.prs;

import com.jptest.money.AlternateIDIssuer;
import com.jptest.money.AlternateIdentifierVO;
import com.jptest.money.GetCustomerTransactionLegacyEquivalentByAlternateIdRequest;
import com.jptest.money.GetCustomerTransactionLegacyEquivalentByAlternateIdResponse;
import com.jptest.payments.PaymentReferenceTypeCode;

/**
 * @JP Inc.
 *
 */
public class PRSCustomerTransactionLegacyEquivalentByAlternateIdTask extends
        PaymentReadBaseTask<GetCustomerTransactionLegacyEquivalentByAlternateIdRequest, GetCustomerTransactionLegacyEquivalentByAlternateIdResponse> {

    public PRSCustomerTransactionLegacyEquivalentByAlternateIdTask(String writeDir, String fileName,
            PaymentReferenceTypeCode refTypeCode, String value, boolean persistOutput) {
        this.writeDir = writeDir;
        this.fileName = fileName;
        this.persistOutPut = persistOutput;
        this.apiKey = PRSApiTypeCode.CTLEBAI;
        this.refCode = refTypeCode;
        this.referenceValue = value;
    }

    @Override
    public GetCustomerTransactionLegacyEquivalentByAlternateIdRequest constructPRSRequest(
            PaymentReferenceTypeCode refTypeCode, String value) {
        GetCustomerTransactionLegacyEquivalentByAlternateIdRequest request = new GetCustomerTransactionLegacyEquivalentByAlternateIdRequest();
        AlternateIdentifierVO alternateIdentifierVO = new AlternateIdentifierVO();
        alternateIdentifierVO.setIssuerEnum(AlternateIDIssuer.jptest_ISSUER);
        alternateIdentifierVO.setReferenceType(refTypeCode);
        alternateIdentifierVO.setIdentifier(value);
        request.setAlternateId(alternateIdentifierVO);
        return request;
    }

    @Override
    public GetCustomerTransactionLegacyEquivalentByAlternateIdResponse executePrs(
            GetCustomerTransactionLegacyEquivalentByAlternateIdRequest request) {
        return bridge.getCustomerTransactionLegacyEquivalentByAlternateIdRequest(request);
    }
}
