package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WithdrawalReveiwDetailsDTO;


/**
 * Represents WITHDRAWAL_REVIEW_DETAILS table of MONEY database
 */
@Singleton
public class WithdrawalReviewDetailsDao extends MoneyDao {

    private static final String GET_WITHDRAWAL_REVIEW_DETAILS_QUERY = "SELECT * FROM WITHDRAWAL_REVIEW_DETAILS where transaction_id in ({commaSeparatedIds}) order by time_created,transaction_id";

    private static final String REASON_CODE_COL = "REASON_CODE";
    private static final String HOLD_DURATION_COL = "HOLD_DURATION";

    /**
     * Queries WITHDRAWAL_REVIEW_DETAILS table for input transaction
     * 
     * @param transactions
     * @return
     */
    public List<WithdrawalReveiwDetailsDTO> getWithdrawalReveiwDetails(final List<WTransactionDTO> transactions) {
        final String query = GET_WITHDRAWAL_REVIEW_DETAILS_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        final List<WithdrawalReveiwDetailsDTO> withdrawalReviewDetails = new ArrayList<>();
        for (final Map<String, Object> result : queryResult) {
            final WithdrawalReveiwDetailsDTO withdrawalReviewDetail = new WithdrawalReveiwDetailsDTO();
            withdrawalReviewDetail.setTransactionId(this.getBigInteger(result.get(TRANSACTION_ID_COL)));
            withdrawalReviewDetail.setStatus(this.getByte(result.get(STATUS_COL)));
            withdrawalReviewDetail.setHoldDuration(this.getBigInteger(result.get(HOLD_DURATION_COL)));
            withdrawalReviewDetail.setReasonCode(this.getBigInteger(result.get(REASON_CODE_COL)));
            withdrawalReviewDetails.add(withdrawalReviewDetail);
        }
        return withdrawalReviewDetails;
    }
}
