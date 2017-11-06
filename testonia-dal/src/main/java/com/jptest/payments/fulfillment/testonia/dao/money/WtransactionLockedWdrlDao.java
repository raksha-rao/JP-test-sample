package com.jptest.payments.fulfillment.testonia.dao.money;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import com.jptest.payments.fulfillment.testonia.model.money.WTransactionDTO;
import com.jptest.payments.fulfillment.testonia.model.money.WtransactionLockedWdrlDTO;


/**
 * Represents WTRANSACTION_LOCKED_WDRL table of MONEY database
 */
@Singleton
public class WtransactionLockedWdrlDao extends MoneyDao {

    private static final String GET_WTRANSACTION_LOCKED_WDRL_QUERY = "SELECT * FROM WTRANSACTION_LOCKED_WDRL where transaction_id in ({commaSeparatedIds}) order by time_locked,transaction_id";

    private static final String HOLD_DURATION_SECONDS_COL = "HOLD_DURATION_SECONDS";
    private static final String WITHDRAWAL_SCORE_COL = "WITHDRAWAL_SCORE";
    private static final String IS_FRAUDSERV_PROCESSED_COL = "IS_FRAUDSERV_PROCESSED";
    private static final String RULE_ID_COL = "RULE_ID";

    /**
     * Queries WTRANSACTION_LOCKED_WDRL table for input transaction
     *
     * @param transactions
     * @return
     */
    public List<WtransactionLockedWdrlDTO> getWtransactionLockedWdrl(final List<WTransactionDTO> transactions) {
        final String query = GET_WTRANSACTION_LOCKED_WDRL_QUERY.replace(COMMA_SEPARATED_IDS_REPLACEMENT_TOKEN,
                mapIdToString(transactions, transaction -> transaction.getId().toString()));
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        final List<WtransactionLockedWdrlDTO> wtransactionLockedWdrlDetails = new ArrayList<>();
        for (final Map<String, Object> result : queryResult) {
            final WtransactionLockedWdrlDTO wtransactionLockedWdrl = new WtransactionLockedWdrlDTO();
            wtransactionLockedWdrl.setTransactionId(this.getBigInteger(result.get(TRANSACTION_ID_COL)));
            wtransactionLockedWdrl.setAccountNumber(this.getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
            wtransactionLockedWdrl.setStatus(this.getByte(result.get(STATUS_COL)));
            wtransactionLockedWdrl.setWithdrawalScore(this.getBigInteger(result.get(WITHDRAWAL_SCORE_COL)));
            wtransactionLockedWdrl.setIsfraudservprocessed(this.getByte(result.get(IS_FRAUDSERV_PROCESSED_COL)));
            wtransactionLockedWdrl.setHoldDurationSeconds(this.getBigInteger(result.get(HOLD_DURATION_SECONDS_COL)));
            wtransactionLockedWdrl.setRuleId(this.getBigInteger(result.get(RULE_ID_COL)));
            wtransactionLockedWdrlDetails.add(wtransactionLockedWdrl);
        }
        return wtransactionLockedWdrlDetails;
    }
}
