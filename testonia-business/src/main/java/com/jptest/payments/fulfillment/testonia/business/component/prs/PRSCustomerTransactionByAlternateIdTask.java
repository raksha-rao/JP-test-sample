/**
 * 
 */
package com.jptest.payments.fulfillment.testonia.business.component.prs;

import com.jptest.money.AlternateIDIssuer;
import com.jptest.money.AlternateIdentifierVO;
import com.jptest.money.GetCustomerTransactionByAlternateIdRequest;
import com.jptest.money.GetCustomerTransactionByAlternateIdResponse;
import com.jptest.payments.PaymentReferenceTypeCode;

/**
 * @JP Inc.
 *
 */
public class PRSCustomerTransactionByAlternateIdTask extends
        PaymentReadBaseTask<GetCustomerTransactionByAlternateIdRequest, GetCustomerTransactionByAlternateIdResponse> {

    public PRSCustomerTransactionByAlternateIdTask(String writeDir, String fileName, 
            PaymentReferenceTypeCode refTypeCode, String value, boolean persistOutput) {
        this.writeDir = writeDir;
        this.fileName = fileName;
        this.persistOutPut = persistOutput;
        this.apiKey = PRSApiTypeCode.CTBAI;
        this.refCode = refTypeCode;
        this.referenceValue = value;
    }

    @Override
    public GetCustomerTransactionByAlternateIdRequest constructPRSRequest(PaymentReferenceTypeCode refTypeCode,
            String value) {

        GetCustomerTransactionByAlternateIdRequest request = new GetCustomerTransactionByAlternateIdRequest();
        AlternateIdentifierVO alternateIdentifierVO = new AlternateIdentifierVO();
        alternateIdentifierVO.setIssuerEnum(AlternateIDIssuer.jptest_ISSUER);
        alternateIdentifierVO.setReferenceType(refTypeCode);
        alternateIdentifierVO.setIdentifier(value);
        request.setAlternateId(alternateIdentifierVO);
        return request;
    }

    @Override
    public GetCustomerTransactionByAlternateIdResponse executePrs(GetCustomerTransactionByAlternateIdRequest request) {
        return bridge.getCustomerTransactionByAlternateIdRequest(request);
    }
}
