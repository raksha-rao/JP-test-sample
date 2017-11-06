package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.AuthRequestVO;
import com.jptest.money.BusinessContextVO;
import com.jptest.money.CaptureRequest;
import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class CaptureRequestBuilder implements VOBuilder<CaptureRequest> {

    private VOBuilder<AuthRequestVO> captureRequest = new NullVOBuilder<>();
    private VOBuilder<BusinessContextVO> businessContext = new NullVOBuilder<>();

    public static CaptureRequestBuilder newBuilder() {
        return new CaptureRequestBuilder();
    }

    public CaptureRequestBuilder captureRequest(final VOBuilder<AuthRequestVO> captureRequest) {
        this.captureRequest = captureRequest;
        return this;

    }

    public CaptureRequestBuilder businessContext(final VOBuilder<BusinessContextVO> businessContext) {
        this.businessContext = businessContext;
        return this;

    }

    @Override
    public CaptureRequest build() {
        final CaptureRequest vo = new CaptureRequest();
        vo.setCaptureRequest(this.captureRequest.build());
        vo.setBusinessContext(this.businessContext.build());
        return vo;
    }
}
