package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.AuthAttributeVO;
import com.jptest.money.AuthCaptureAnalyzeVO;
import com.jptest.money.AuthInfoVO;
import com.jptest.money.TransactionAuthVO;
import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class AuthAttributesVOBuilder implements VOBuilder<AuthAttributeVO> {

    private VOBuilder<AuthInfoVO> authInfo = new NullVOBuilder<>();
    private VOBuilder<AuthCaptureAnalyzeVO> authCaptureAnalyze = new NullVOBuilder<>();
    private VOBuilder<TransactionAuthVO> transactionAuthInfo = new NullVOBuilder<>();

    public static AuthAttributesVOBuilder newBuilder() {
        return new AuthAttributesVOBuilder();
    }

    public AuthAttributesVOBuilder authInfo(final VOBuilder<AuthInfoVO> authInfo) {
        this.authInfo = authInfo;
        return this;

    }

    public AuthAttributesVOBuilder authCaptureAnalyze(final VOBuilder<AuthCaptureAnalyzeVO> authCaptureAnalyze) {
        this.authCaptureAnalyze = authCaptureAnalyze;
        return this;

    }

    public AuthAttributesVOBuilder transactionAuthInfo(final VOBuilder<TransactionAuthVO> transactionAuthInfo) {
        this.transactionAuthInfo = transactionAuthInfo;
        return this;

    }

    @Override
    public AuthAttributeVO build() {
        final AuthAttributeVO vo = new AuthAttributeVO();
        vo.setAuthInfo(this.authInfo.build());
        vo.setAuthCaptureAnalyze(this.authCaptureAnalyze.build());
        vo.setTransactionAuthInfo(this.transactionAuthInfo.build());
        return vo;
    }
}
