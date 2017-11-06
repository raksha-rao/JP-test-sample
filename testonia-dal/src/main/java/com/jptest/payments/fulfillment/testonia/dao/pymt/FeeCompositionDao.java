package com.jptest.payments.fulfillment.testonia.dao.pymt;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.model.pymt.FeeCompositionDTO;


/**
 * Represents money_movement table of PYMT database
 */
public class FeeCompositionDao extends PymtDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeeCompositionDao.class);

    private static final String GET_FEE_COMPOSITION_FOR_ACCOUNT_NUMBER = "select * from fee_composition where payment_side_id in ({payment_side_id}) order by type_code, debit_credit_code, fee_amount";
    private static final String PAYMENT_SIDE_ID_REPLACEMENT = "{payment_side_id}";

    private static final String ACCOUNT_NUMBER_COL = "ACCOUNT_NUMBER";
    private static final String ID_COL = "ID";
    private static final String PAYMENT_SIDE_ID_COL = "PAYMENT_SIDE_ID";
    private static final String DEBIT_CREDIT_CODE_COL = "DEBIT_CREDIT_CODE";
    private static final String CALCULATION_FACTORS_COL = "CALCULATION_FACTORS";
    private static final String FEE_AMOUNT_COL = "FEE_AMOUNT";
    private static final String FEE_PERCENTAGE_COL = "FEE_PERCENTAGE";
    private static final String FEE_AMOUNT_FIXED_COL = "FEE_AMOUNT_FIXED";
    private static final String ROW_CREATED_TIME_COL = "ROW_CREATED_TIME";
    private static final String ROW_UPDATED_TIME_COL = "ROW_UPDATED_TIME";


    
    public List<FeeCompositionDTO> findByPaymentSideIds(Set<String> paymentSetIdsSet) {
        List<FeeCompositionDTO> feeCompositionIds = new ArrayList<FeeCompositionDTO>();
        if (paymentSetIdsSet != null) {
            String query = GET_FEE_COMPOSITION_FOR_ACCOUNT_NUMBER.replace(PAYMENT_SIDE_ID_REPLACEMENT,
                    mapIdToString(paymentSetIdsSet, paymentSideId -> paymentSideId));
            List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
            for (Map<String, Object> result : queryResult) {
                feeCompositionIds.add(mapFeeComposition(result));
            }
        }
        LOGGER.info("Found feeComposition records :{} for payment_side ids:{}", feeCompositionIds.size(), paymentSetIdsSet);
        return feeCompositionIds;

    }

    private FeeCompositionDTO mapFeeComposition(Map<String, Object> result) {
        FeeCompositionDTO moneyMovement = new FeeCompositionDTO();
        moneyMovement.setId(getBigInteger(result.get(ID_COL)));
        moneyMovement.setPaymentSideId(getBigInteger(result.get(PAYMENT_SIDE_ID_COL)));
        moneyMovement.setDebitCreditCode(getString(result.get(DEBIT_CREDIT_CODE_COL)));
        moneyMovement.setAccountNumber(getBigInteger(result.get(ACCOUNT_NUMBER_COL)));
        moneyMovement.setTypeCode(getString(result.get(TYPE_CODE_COL)));
        moneyMovement.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
        moneyMovement.setCalculationFactors(getString(result.get(CALCULATION_FACTORS_COL)));
        moneyMovement.setFeeAmount(getLong(result.get(FEE_AMOUNT_COL)));
        moneyMovement.setFeePercentage(getBigDecimal(result.get(FEE_PERCENTAGE_COL)));
        moneyMovement.setFeeAmountFixed(getLong(result.get(FEE_AMOUNT_FIXED_COL)));
        moneyMovement.setInitiationTime(getLong(result.get(INITIATION_TIME_COL)));
        moneyMovement.setRowCreatedTime(getLong(result.get(ROW_CREATED_TIME_COL)));
        moneyMovement.setRowUpdatedTime(getLong(result.get(ROW_UPDATED_TIME_COL)));

        return moneyMovement;
    }
}
