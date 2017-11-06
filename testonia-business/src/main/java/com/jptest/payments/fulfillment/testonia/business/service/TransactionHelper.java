package com.jptest.payments.fulfillment.testonia.business.service;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.test.money.constants.WTransactionConstants.Subtype;
import com.jptest.test.money.constants.WTransactionConstants.Type;

@Singleton
public class TransactionHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHelper.class);

    /**
     * Returns sender's transactionId from list of WTransactionVOs
     * @param wTransactionList
     * @return
     */
    public WTransactionVO getSenderTransaction(final List<WTransactionVO> wTransactionList) {
        WTransactionVO senderTransaction = null;
        for (final WTransactionVO transactionVO : wTransactionList) {
            if ((Type.USERUSER.getByte() == transactionVO.getType()
                    || transactionVO.getType() == Type.AUTHORIZATION.getByte()
                    || transactionVO.getType() == Type.ACHWITHDRAW.getByte()) &&
                    transactionVO.getSubtype() != Subtype.USERUSER_DISPUTED_PAYMENT.getByte()
                    && transactionVO.getAmount().getAmount() < 0L) {
                senderTransaction = transactionVO;
            }
        }
        Assert.assertNotNull(senderTransaction, "Sender's TransactionId is empty");
        LOGGER.info("senderTransactionId:{}", senderTransaction.getId());
        return senderTransaction;
    }

    public WTransactionVO getRecipientTransaction(final List<WTransactionVO> wTransactionList) {
        WTransactionVO receiverTransaction = null;
        for (final WTransactionVO transactionVO : wTransactionList) {
            LOGGER.info("transaction-type():{} transactionId:{}", transactionVO.getType(),
                    transactionVO.getId().toString());
            if ((Type.USERUSER.getByte() == transactionVO.getType())
                    && transactionVO.getAmount().getAmount() > 0L) {
                receiverTransaction = transactionVO;
            }
        }
        Assert.assertNotNull(receiverTransaction, "Receiver's Transaction is empty");
        LOGGER.info("receiverTransaction:{}", receiverTransaction);
        return receiverTransaction;
    }

    public WTransactionVO getRecipientTransaction(final List<WTransactionVO> wTransactionList, final String encryptedId) {
        WTransactionVO receiverTransaction = null;
        for (final WTransactionVO transactionVO : wTransactionList) {
            if (transactionVO.getEncryptedId().equals(encryptedId)) {
                receiverTransaction = transactionVO;
                break;
            }
        }
        Assert.assertNotNull(receiverTransaction, "Receiver's Transaction not found in list for " + encryptedId);
        LOGGER.info("receiverTransaction:{}", receiverTransaction);
        return receiverTransaction;
    }

    public WTransactionDTO getPendingReversalTransaction(final List<WTransactionDTO> pendingTransactions) {
        WTransactionDTO pendingReversalTransaction = null;
        for (final WTransactionDTO transactionVO : pendingTransactions) {
            LOGGER.info("transaction-type():{} transactionId:{}", transactionVO.getType(),
                    transactionVO.getId().toString());
            if (Type.USERUSER.getByte() == transactionVO.getSubtype() && (transactionVO.getFlags1() & WTransactionConstants.Flag1.ECHECK.getValue())==0) {
                pendingReversalTransaction = transactionVO;
            }
        }
        Assert.assertNotNull(pendingReversalTransaction, "Receiver's Transaction is empty");
        LOGGER.info("pendingReversalTransaction:{}", pendingReversalTransaction);
        return pendingReversalTransaction;
    }


    public boolean isP20Stack(final WTransactionVO wTransaction) {
        return wTransaction.getFlags5().testBit(11);
    }

    public boolean isCheetahStack(final WTransactionVO wTransaction) {
        return wTransaction.getFlags5().testBit(54);
    }

    public boolean isTransactionAtributesSet(final WTransactionVO wTransaction) {
        return wTransaction.getFlags5().testBit(53);
    }

    public boolean isP10Stack(final WTransactionVO wTransaction) {
        return BigInteger.valueOf(wTransaction.getFlags()).testBit(2);
    }

    public boolean isUnilateral(final WTransactionVO wTransaction) {
        return wTransaction.getFlags6().testBit(18);
    }

    public boolean isFlexTransaction(final WTransactionVO wTransaction) {
        return wTransaction.getFlags6().testBit(16);
    }


}
