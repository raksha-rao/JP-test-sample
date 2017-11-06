package com.jptest.payments.fulfillment.testonia.business.component.task;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.money.FulfillPaymentResponse;
import com.jptest.money.FulfillmentRequestStatus;
import com.jptest.money.TransactionUnitHandleDetailsVO;
import com.jptest.money.TransactionUnitStatusVO;
import com.jptest.payments.fulfillment.testonia.business.util.CryptoUtil;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


public class LegacyFulfillUnilateralPaymentResponseGenerationTask extends BaseTask<FulfillPaymentResponse> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LegacyFulfillUnilateralPaymentResponseGenerationTask.class);

    @Inject
    private WTransactionP20DaoImpl WTransactionDao;

    @Inject
    private CryptoUtil cryptoUtil;

    /**
     * <li>FulfillPaymentResponse details is used post payment APIs. If the test cases uses PreCreated transaction then
     * there will not be any FulfillPaymentResponse.</li>
     * <li>Need to construct FulfillPaymentResponse using WTRANSACTION_ID_KEY and Store it in
     * FULFILL_PAYMENT_RESPONSE_KEY</li>
     *
     * @param context
     * @return FulfillPaymentResponse
     */

    @Override
    public final FulfillPaymentResponse process(final Context context) {
        final FulfillPaymentResponse fulfillPaymentResponse = this.getFulfillPaymentResponse();

        final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
        final String buyerAccountNumber = buyer.getAccountNumber();

        final String buyerSideEncryptedWTransactionID = this.getEncryptedWTransactionID(buyerAccountNumber);

        fulfillPaymentResponse.getTransactionUnitStatus().get(0).getHandleDetails()
        .setDebitSideHandle(buyerSideEncryptedWTransactionID);
        fulfillPaymentResponse.getTransactionUnitStatus().get(0)
        .setTransactionUnitHandle(buyerSideEncryptedWTransactionID);

        LOGGER.info("Fulfill Payment Response: {}", printValueObject(fulfillPaymentResponse));
        return fulfillPaymentResponse;
    }

    public String getEncryptedWTransactionID(final String accountNumber) {
        String encryptedWTransactionID = null;
        final List<BigInteger> TxnId = this.WTransactionDao.getTransactionIdByAccountNumberAndType(
                new BigInteger(accountNumber), String.valueOf(WTransactionConstants.Type.USERUSER.getValue()));
        try {
            encryptedWTransactionID = this.cryptoUtil.encryptTxnId(Long.parseLong(TxnId.get(0).toString()),
                    Long.parseLong(accountNumber));
        }
        catch (final Exception e) {
            LOGGER.warn("Encryption of Transaction ID Failed during FulfillPaymentResponse Construction", e);
            throw new TestExecutionException(
                    "Encryption of Transaction ID Failed during FulfillPaymentResponse Construction");
        }
        return encryptedWTransactionID;
    }

    /**
     * Construct FulfillPaymentResponse with Default value
     *
     * @return fulfillPaymentResponse
     */
    public FulfillPaymentResponse getFulfillPaymentResponse() {
        final TransactionUnitHandleDetailsVO transactionUnitHandleDetails = new TransactionUnitHandleDetailsVO();

        final List<TransactionUnitStatusVO> transactionUnitStatusList = new ArrayList<TransactionUnitStatusVO>();
        final TransactionUnitStatusVO transactionUnitStatus = new TransactionUnitStatusVO();
        transactionUnitStatusList.add(0, transactionUnitStatus);
        transactionUnitStatusList.get(0).setTransactionUnitId("1");
        transactionUnitStatusList.get(0).setIsDeclined(false);
        transactionUnitStatusList.get(0).setHandleDetails(transactionUnitHandleDetails);

        final FulfillPaymentResponse fulfillPaymentResponse = new FulfillPaymentResponse();
        fulfillPaymentResponse.setIsDuplicatePlan(false);
        fulfillPaymentResponse.setFulfillmentRequestStatus(FulfillmentRequestStatus.PROCESSED);
        fulfillPaymentResponse.setTransactionUnitStatus(transactionUnitStatusList);
        LOGGER.info("getFulfillPaymentResponse response: {}", printValueObject(fulfillPaymentResponse));
        return fulfillPaymentResponse;
    }
}
