package com.jptest.payments.fulfillment.testonia.business.component.validation;

import static org.testng.Assert.assertTrue;
import java.math.BigInteger;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.TimeoutAwareComponent;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.model.FulfillPaymentPlanOptions;
import com.jptest.payments.fulfillment.testonia.model.group.FundingMethodType;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants.Type;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * Task to provide basic validations on PRS data
 */
public class PaymentReadServBasicAsserter extends BaseAsserter implements TimeoutAwareComponent {

    private FulfillPaymentPlanOptions paymentOptions;
    private WTransactionConstants.Status expectedStatus = WTransactionConstants.Status.SUCCESS;

    public PaymentReadServBasicAsserter(final FulfillPaymentPlanOptions paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public PaymentReadServBasicAsserter(final FulfillPaymentPlanOptions paymentOptions,
            final WTransactionConstants.Status expectedStatus) {
        this.paymentOptions = paymentOptions;
        this.expectedStatus = expectedStatus;
    }

    @Override
    public void validate(final Context context) {

        final User seller = (User) this.getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());
        final User buyer = (User) this.getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
        @SuppressWarnings("unchecked")
        final List<WTransactionVO> wTransactionList = (List<WTransactionVO>) this.getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());

        assertTrue(CollectionUtils.isNotEmpty(wTransactionList), "Empty WTransactions from PRS ");

        if (this.paymentOptions.isUnilateral()) {
            final BigInteger sellerAccountNumber = new BigInteger(seller.getAccountNumber());
            assertTrue(this.isPaymentRecordPresent(wTransactionList, sellerAccountNumber, Type.USERUSER),
                    "validating seller side record");
        }

        this.validateFundingMethod(buyer, wTransactionList, this.paymentOptions);
    }

    @Override
    public long getTimeoutInMs() {
        return 100000;
    }

    private boolean isPaymentRecordPresent(final List<WTransactionVO> wtransactions, final BigInteger accountNumber,
            final Type type) {
        for (final WTransactionVO wt : wtransactions) {

            if (wt.getAccountNumber().compareTo(accountNumber) == 0 && wt.getType() == type.getByte()) {
                return true;
            }
        }
        return false;
    }

    /*
     * If the transaction is successful, there'll be 'H'corresponding to funding source and 'X' for currency conversion
     * (if applicable) rows added besides 'U' rows. If the status is declined, only 'U' rows for buyer and seller will
     * be inserted.
     */
    private void validateFundingMethod(final User user, final List<WTransactionVO> wtransactions,
            final FulfillPaymentPlanOptions paymentOptions) {
        final FundingMethodType fund_type = FundingMethodType.valueOf(paymentOptions.getFundingSource());
        final BigInteger userAccountNo = new BigInteger(user.getAccountNumber());
        if (WTransactionConstants.Status.DENIED == this.expectedStatus) {
            for (final WTransactionVO wt : wtransactions) {
                if (wt.getAccountNumber().compareTo(userAccountNo) == 0) {
                    assertTrue(wt.getType().equals(Type.USERUSER.getByte()));
                    assertTrue(wt.getStatus().equals(this.expectedStatus.getByte()));
                }
            }
        }
        else {
            Byte wtFundType = '\0';
            for (final WTransactionVO wt : wtransactions) {
                if (wt.getAccountNumber().compareTo(userAccountNo) == 0
                        && wt.getType() != WTransactionConstants.Type.USERUSER.getByte()
                        && wt.getType() != WTransactionConstants.Type.CURRENCY_CONVERSION.getByte()) {

                    wtFundType = wt.getType();
                }
            }
            assertTrue(getExpectedWtFundType(fund_type).equals(wtFundType), "validating funding type");
        }
    }

    private static Byte getExpectedWtFundType(final FundingMethodType fundingMethod) {
        if (fundingMethod == FundingMethodType.CHARGE) {
            return WTransactionConstants.Type.CHARGE.getByte();
        }
        else if (fundingMethod == FundingMethodType.IACH || fundingMethod == FundingMethodType.ECHECK
                || fundingMethod == FundingMethodType.ELV) {
            return WTransactionConstants.Type.ACHDEPOSIT.getByte();
        }
        else if (fundingMethod == FundingMethodType.DUAL_CARD || fundingMethod == FundingMethodType.VIRTUAL_LINE
                || fundingMethod == FundingMethodType.TRANSACTIONAL_CREDIT) {
            return WTransactionConstants.Type.BUYER_CREDIT_CHARGE.getByte();
        }
        else {
            return '\0';
        }

    }

}
