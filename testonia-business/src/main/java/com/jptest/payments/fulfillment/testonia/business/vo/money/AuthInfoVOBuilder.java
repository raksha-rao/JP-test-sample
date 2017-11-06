package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import com.jptest.money.AuthInfoVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class AuthInfoVOBuilder implements VOBuilder<AuthInfoVO> {

    private BigInteger expirationTimeOff = new BigInteger("2592000");
    private Long honorExpirationOff = new Long(294939);

    public static AuthInfoVOBuilder newBuilder() {
        return new AuthInfoVOBuilder();
    }

    public AuthInfoVOBuilder expirationTimeOff(final BigInteger expirationTimeOff) {
        this.expirationTimeOff = expirationTimeOff;
        return this;

    }

    public AuthInfoVOBuilder honorExpirationOff(final Long honorExpirationOff) {
        this.honorExpirationOff = honorExpirationOff;
        return this;

    }

    @Override
    public AuthInfoVO build() {
        final AuthInfoVO vo = new AuthInfoVO();
        vo.setExpirationTimeOffset(this.expirationTimeOff);
        vo.setHonorExpirationOffset(this.honorExpirationOff);
        return vo;
    }
}
