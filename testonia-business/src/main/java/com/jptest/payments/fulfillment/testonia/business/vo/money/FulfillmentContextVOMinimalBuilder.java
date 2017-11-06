package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import java.util.Random;

import com.jptest.money.FulfillmentContextVO;

public class FulfillmentContextVOMinimalBuilder extends FulfillmentContextVOBuilder {
    private BigInteger merchantAccountNumber;

    public static FulfillmentContextVOMinimalBuilder newBuilder() {
        return new FulfillmentContextVOMinimalBuilder();
    }

    public FulfillmentContextVOMinimalBuilder setMerchantAccountNumber(final BigInteger merchantAccountNumber) {
        this.merchantAccountNumber = merchantAccountNumber;
        return this;
    }

    @Override
    public FulfillmentContextVO build() {
        final FulfillmentContextVO vo = super.build();

        final Random random = new Random();
        final int randomNumber = random.nextInt(10000);

        vo.getTransactionUnits().get(0).getUniqueIdConstraint()
                .setUniqueId(String.valueOf(randomNumber) + "_" + this.merchantAccountNumber.toString());
        return vo;
    }

}
