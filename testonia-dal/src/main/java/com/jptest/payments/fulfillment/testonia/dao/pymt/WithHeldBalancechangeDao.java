package com.jptest.payments.fulfillment.testonia.dao.pymt;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.model.pymt.WithHeldBalanceChangeDTO;


/**
 * Represents money_movement table of PYMT database
 */
public class WithHeldBalancechangeDao extends PymtDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithHeldBalancechangeDao.class);

    private static final String GET_WITHHELD_BALANCE_FOR_ACCOUNT_NUMBER = "select * from withheld_balance_change where payment_side_id in ({payment_side_id}) order by balance_type_code,change_type_code,debit_credit_code,reason_code,status_code";
    private static final String PAYMENT_SIDE_ID_REPLACEMENT = "{payment_side_id}";
    private static final String DEBIT_CREDIT_CODE_COL = "DEBIT_CREDIT_CODE";
    private static final String ACCOUNT_NUMBER_COL = "ACCOUNT_NUMBER";
    private static final String BALANCE_TYPE_CODE_COL = "BALANCE_TYPE_CODE";
    private static final String CHANGE_TIME_COL = "CHANGE_TIME";
    private static final String CHANGE_TYPE_CODE_COL = "CHANGE_TYPE_CODE";
    private static final String ROW_VERSION_COL = "ROW_VERSION";
    private static final String CURRENCY_EXCHANGE_ID_COL = "CURRENCY_EXCHANGE_ID";
    private static final String AT_POSTING_HOLDING_BALANCE_COL = "AT_POSTING_HOLDING_BALANCE";
    private static final String AT_POSTING_WITHHELD_BALANCE_COL = "AT_POSTING_WITHHELD_BALANCE";
    private static final String BALANCE_POSTING_TIME_COL = "BALANCE_POSTING_TIME";
    private static final String ROW_CREATED_TIME_COL = "ROW_CREATED_TIME";
    private static final String ROW_UPDATED_TIME_COL = "ROW_UPDATED_TIME";
    private static final String VOIDS_BALANCE_CHANGE_ID_COL = "VOIDS_BALANCE_CHANGE_ID";
    private static final String VOIDS_BALANCE_CHANGE_TIME_COL = "VOIDS_BALANCE_CHANGE_TIME";
    private static final String MONEY_MOVEMENT_ID_COL = "MONEY_MOVEMENT_ID";
    private static final String DERIVED_AVAILABLE_BALANCE_COL = "DERIVED_AVAILABLE_BALANCE";
    private static final String PAYMENT_SIDE_ID_COL = "PAYMENT_SIDE_ID";
    private static final String ID_COL = "ID";
    
    
    public List<WithHeldBalanceChangeDTO> findByPaymentSideIds(Set<String> paymentSetIdsSet) {
        List<WithHeldBalanceChangeDTO> withHeldBalanceChange = new ArrayList<WithHeldBalanceChangeDTO>();
        if (paymentSetIdsSet != null) {
            String query = GET_WITHHELD_BALANCE_FOR_ACCOUNT_NUMBER.replace(PAYMENT_SIDE_ID_REPLACEMENT,
                    mapIdToString(paymentSetIdsSet, paymentSideId -> paymentSideId));
            List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(
                    getDatabaseName(), query);
            for (Map<String, Object> result : queryResult) {
                withHeldBalanceChange.add(mapWithHeldBalance(result));
            }
        }
        LOGGER.info("Found WithHeld Balance change records :{} for paymentside Ids:{}", withHeldBalanceChange.size(), paymentSetIdsSet);
        return withHeldBalanceChange;

    }

    private WithHeldBalanceChangeDTO mapWithHeldBalance(Map<String, Object> result) {
        WithHeldBalanceChangeDTO withHeldBalanceDTO = new WithHeldBalanceChangeDTO();
        withHeldBalanceDTO.setId(getBigInteger(result.get(ID_COL)));
        withHeldBalanceDTO.setPaymentSideId(getBigInteger(result.get(PAYMENT_SIDE_ID_COL)));
        withHeldBalanceDTO.setDebitCreditCode(getString(result.get(DEBIT_CREDIT_CODE_COL)));
        withHeldBalanceDTO.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
        withHeldBalanceDTO.setReasonCode(getString(result.get(REASON_CODE_COL)));
        withHeldBalanceDTO.setBalanceTypeCode(getString(result.get(BALANCE_TYPE_CODE_COL)));
        withHeldBalanceDTO.setChangeTypeCode(getString(result.get(CHANGE_TYPE_CODE_COL)));
        withHeldBalanceDTO.setChangeTime(getLong(result.get(CHANGE_TIME_COL)));
        withHeldBalanceDTO.setRowVersion(getLong(result.get(ROW_VERSION_COL)));
        withHeldBalanceDTO.setStatusCode(getString(result.get(STATUS_CODE_COL)));
        withHeldBalanceDTO.setMoneyAmount(getLong(result.get(MONEY_AMOUNT_COL)));
        withHeldBalanceDTO.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
        withHeldBalanceDTO.setCurrencyExchangeId(getBigInteger(result.get(CURRENCY_EXCHANGE_ID_COL)));
        withHeldBalanceDTO.setAtPostingHoldingBalance(getLong(result.get(AT_POSTING_HOLDING_BALANCE_COL)));
        withHeldBalanceDTO.setAtPostingWithheldBalance(getLong(result.get(AT_POSTING_WITHHELD_BALANCE_COL)));
        withHeldBalanceDTO.setBalancePostingTime(getLong(result.get(BALANCE_POSTING_TIME_COL)));

        withHeldBalanceDTO.setFlags01(getBigInteger(result.get(FLAGS01_COL)));
        withHeldBalanceDTO.setFlags02(getBigInteger(result.get(FLAGS02_COL)));
        withHeldBalanceDTO.setFlags03(getBigInteger(result.get(FLAGS03_COL)));
        withHeldBalanceDTO.setFlags04(getBigInteger(result.get(FLAGS04_COL)));
        withHeldBalanceDTO.setFlags05(getBigInteger(result.get(FLAGS05_COL)));
        withHeldBalanceDTO.setFlags06(getBigInteger(result.get(FLAGS06_COL)));
        withHeldBalanceDTO.setFlags07(getBigInteger(result.get(FLAGS07_COL)));
        withHeldBalanceDTO.setFlags08(getBigInteger(result.get(FLAGS08_COL)));
        withHeldBalanceDTO.setFlags09(getBigInteger(result.get(FLAGS09_COL)));
        withHeldBalanceDTO.setFlags10(getBigInteger(result.get(FLAGS10_COL)));
        withHeldBalanceDTO.setRowCreatedTime(getLong(result.get(ROW_CREATED_TIME_COL)));
        withHeldBalanceDTO.setRowUpdatedTime(getLong(result.get(ROW_UPDATED_TIME_COL)));
        withHeldBalanceDTO.setRowCreatedTime(getLong(result.get(VOIDS_BALANCE_CHANGE_ID_COL)));
        withHeldBalanceDTO.setRowCreatedTime(getLong(result.get(VOIDS_BALANCE_CHANGE_TIME_COL)));
        withHeldBalanceDTO.setRowCreatedTime(getLong(result.get(MONEY_MOVEMENT_ID_COL)));
        withHeldBalanceDTO.setRowCreatedTime(getLong(result.get(DERIVED_AVAILABLE_BALANCE_COL)));

        return withHeldBalanceDTO;
    }
}
