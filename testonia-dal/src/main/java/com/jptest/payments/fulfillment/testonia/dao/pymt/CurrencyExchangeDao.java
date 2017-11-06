package com.jptest.payments.fulfillment.testonia.dao.pymt;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.model.pymt.CurrencyExchangeDTO;


/**
 * Represents currency_exchange table of PYMT database
 */
public class CurrencyExchangeDao extends PymtDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyExchangeDao.class);

    private static final String GET_CURRENCY_EXCHANGE_FOR_PAYMENT_SIDE_ID = "select * from currency_exchange where payment_side_id in ({payment_side_id}) order by debit_credit_code,rate_type_code,reason_code";
    private static final String PAYMENT_SIDE_ID_REPLACEMENT = "{payment_side_id}";

    
    private static final String PAYMENT_SIDE_ID_COL = "PAYMENT_SIDE_ID";
    private static final String DEBIT_CREDIT_CODE_COL = "DEBIT_CREDIT_CODE";
    private static final String EXCHANGE_RATE_COL = "EXCHANGE_RATE";
    private static final String EXCHANGE_TIME_COL = "EXCHANGE_TIME";
    private static final String RATE_TYPE_CODE_COL = "RATE_TYPE_CODE";
    private static final String REASON_CODE_COL = "REASON_CODE";
    private static final String FEES_WAIVED_FLAG_COL = "FEES_WAIVED_FLAG";
    private static final String FROM_CURRENCY_CODE_COL = "FROM_CURRENCY_CODE";
    private static final String FROM_MONEY_AMOUNT_COL = "FROM_MONEY_AMOUNT";
    private static final String FROM_USD_MONEY_AMOUNT_COL = "FROM_USD_MONEY_AMOUNT";
    private static final String TO_MONEY_AMOUNT_COL = "TO_MONEY_AMOUNT";
    private static final String TO_USD_MONEY_AMOUNT_COL = "TO_USD_MONEY_AMOUNT";
    private static final String EXTERNAL_EXCHANGE_FLAG_COL = "EXTERNAL_EXCHANGE_FLAG";
    private static final String LEGACY_FROM_ID_COL = "LEGACY_FROM_ID";
    private static final String LEGACY_TO_ID_COL = "LEGACY_TO_ID";
    private static final String LEGACY_FROM_BATC_COL = "LEGACY_FROM_BATC";
    private static final String LEGACY_TO_BATC_COL = "LEGACY_TO_BATC";
    private static final String CALCULATION_FACTORS_COL = "CALCULATION_FACTORS";
    private static final String LEGACY_SHARED_ID_COL = "LEGACY_SHARED_ID";
    private static final String LEGACY_PARENT_ID_COL = "LEGACY_PARENT_ID";
    private static final String ROW_VERSION_COL = "ROW_VERSION";
    private static final String FINANCIAL_JOURNAL_AID_COL = "FINANCIAL_JOURNAL_AID";
    private static final String ROW_CREATED_TIME_COL = "ROW_CREATED_TIME";
    private static final String ROW_UPDATED_TIME_COL = "ROW_UPDATED_TIME";
    private static final String FJ_CREATED_TIME_COL = "FJ_CREATED_TIME";
    private static final String FJ_UPDATED_TIME_COL = "FJ_UPDATED_TIME";
    private static final String ID_COL = "ID";
    private static final String ACCOUNT_NUMBER_COL = "ACCOUNT_NUMBER";

    public List<CurrencyExchangeDTO> findByPaymentSideIds(Set<String> paymentSetIdsSet) {
        List<CurrencyExchangeDTO> moneyMovements = new ArrayList<CurrencyExchangeDTO>();
        if (paymentSetIdsSet != null) {
            String query = GET_CURRENCY_EXCHANGE_FOR_PAYMENT_SIDE_ID.replace(PAYMENT_SIDE_ID_REPLACEMENT,
                    mapIdToString(paymentSetIdsSet, paymentSideId -> paymentSideId));
            List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
            for (Map<String, Object> result : queryResult) {
                moneyMovements.add(mapCurrencyExchange(result));
            }
        }
        LOGGER.info("Found CurrencyExchange records{} for PaymentSideIds:{}", moneyMovements.size(), paymentSetIdsSet);
        return moneyMovements;

    }

   

    private CurrencyExchangeDTO mapCurrencyExchange(Map<String, Object> result) {
        CurrencyExchangeDTO moneyMovement = new CurrencyExchangeDTO();
        moneyMovement.setId(getBigInteger(result.get(ID_COL)));
        moneyMovement.setPaymentSideId(getBigInteger(result.get(PAYMENT_SIDE_ID_COL)));
        moneyMovement.setDebitCreditCode(getString(result.get(DEBIT_CREDIT_CODE_COL)));
        moneyMovement.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));

        moneyMovement.setReasonCode(getString(result.get(REASON_CODE_COL)));
        moneyMovement.setExchangeRate(getBigDecimal(result.get(EXCHANGE_RATE_COL)));
        moneyMovement.setExchangeTime(getLong(result.get(EXCHANGE_TIME_COL)));
        moneyMovement.setRateTypeCode(getString(result.get(RATE_TYPE_CODE_COL)));
        moneyMovement.setFeesWaivedFlag(getString(result.get(FEES_WAIVED_FLAG_COL)));
        moneyMovement.setFromCurrencyCode(getString(result.get(FROM_CURRENCY_CODE_COL)));
        moneyMovement.setFromMoneyAmount(getLong(result.get(FROM_MONEY_AMOUNT_COL)));
        moneyMovement.setFromMoneyAmount(getLong(result.get(FROM_USD_MONEY_AMOUNT_COL)));
        moneyMovement.setToMoneyAmount(getLong(result.get(TO_MONEY_AMOUNT_COL)));
        moneyMovement.setToMoneyAmount(getLong(result.get(TO_USD_MONEY_AMOUNT_COL)));
        moneyMovement.setExternalExchangeFlag(getString(result.get(EXTERNAL_EXCHANGE_FLAG_COL)));
        moneyMovement.setLegacyFromId(getLong(result.get(LEGACY_FROM_ID_COL)));
        moneyMovement.setLegacyToId(getLong(result.get(LEGACY_TO_ID_COL)));
        moneyMovement.setLegacyfromBATC(getLong(result.get(LEGACY_FROM_BATC_COL)));
        moneyMovement.setLegacyToBATC(getLong(result.get(LEGACY_TO_BATC_COL)));
        moneyMovement.setCalculationFactors(getString(result.get(CALCULATION_FACTORS_COL)));
        moneyMovement.setLegacySharedId(getLong(result.get(LEGACY_SHARED_ID_COL)));
        moneyMovement.setLegacyParentId(getLong(result.get(LEGACY_PARENT_ID_COL)));
        moneyMovement.setRowVersion(getLong(result.get(ROW_VERSION_COL)));
        moneyMovement.setFinancialJournalAID(getLong(result.get(FINANCIAL_JOURNAL_AID_COL)));


        moneyMovement.setFlags01(getBigInteger(result.get(FLAGS01_COL)));
        moneyMovement.setFlags02(getBigInteger(result.get(FLAGS02_COL)));
        moneyMovement.setFlags03(getBigInteger(result.get(FLAGS03_COL)));
        moneyMovement.setFlags04(getBigInteger(result.get(FLAGS04_COL)));
        moneyMovement.setFlags05(getBigInteger(result.get(FLAGS05_COL)));
        moneyMovement.setFlags06(getBigInteger(result.get(FLAGS06_COL)));
        moneyMovement.setFlags07(getBigInteger(result.get(FLAGS07_COL)));
        moneyMovement.setFlags08(getBigInteger(result.get(FLAGS08_COL)));
        moneyMovement.setFlags09(getBigInteger(result.get(FLAGS09_COL)));
        moneyMovement.setFlags10(getBigInteger(result.get(FLAGS10_COL)));
        moneyMovement.setRowCreatedTime(getLong(result.get(ROW_CREATED_TIME_COL)));
        moneyMovement.setRowUpdatedTime(getLong(result.get(ROW_UPDATED_TIME_COL)));
        moneyMovement.setFjCreatedTime(getLong(result.get(FJ_CREATED_TIME_COL)));
        moneyMovement.setFjUpdatedTime(getLong(result.get(FJ_UPDATED_TIME_COL)));

        return moneyMovement;
    }
}
