package com.jptest.payments.fulfillment.testonia.dao.pymt;

import static com.jptest.payments.fulfillment.testonia.core.util.StringHelper.mapIdToString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.model.pymt.PaymentSideDTO;

/**
 * Represents payment_side table of PYMT database
 */
@Singleton
public class PaymentSideDao extends PymtDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSideDao.class);

    private static final String GET_PAYMENT_SIDE_FOR_ACCOUNT_NUMBER = "select * from payment_side where payment_id in ({payment_id}) order by type_code,debit_credit_code,subtype_code, status_code, reason_code,money_amount";
    private static final String PAYMENT_ID_REPLACEMENT = "{payment_id}";

    private static final String GET_PAYMENT_SIDE_FOR_PARENT_ID = "select * from payment_side where parent_id in ({parent_id})";
    private static final String PARENT_ID_REPLACEMENT = "{parent_id}";

    private static final String GET_PAYMENT_SIDE_FOR_ID = "select * from payment_side where id in ({id})";
    private static final String ID_REPLACEMENT = "{id}";

    public List<PaymentSideDTO> findByPaymentId(final Set<String> paymentIds) {
        List<PaymentSideDTO> paymentSides = new ArrayList<PaymentSideDTO>();
        if (paymentIds != null && !paymentIds.isEmpty()) {
            String query = GET_PAYMENT_SIDE_FOR_ACCOUNT_NUMBER.replace(PAYMENT_ID_REPLACEMENT,
                    mapIdToString(paymentIds, paymentId -> paymentId));
            List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
            for (Map<String, Object> result : queryResult) {
                paymentSides.add(mapPaymentSide(result));
            }
        }
        LOGGER.debug("Found paymentsides:{}", paymentSides);
        return paymentSides;
    }

    public List<PaymentSideDTO> findByParentIds(final Set<String> parentIds) {
        List<PaymentSideDTO> paymentSides = new ArrayList<PaymentSideDTO>();
        if (parentIds != null && !parentIds.isEmpty()) {
            String query = GET_PAYMENT_SIDE_FOR_PARENT_ID.replace(PARENT_ID_REPLACEMENT,
                    mapIdToString(parentIds, parentId -> parentId));
            List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
            for (Map<String, Object> result : queryResult) {
                paymentSides.add(mapPaymentSide(result));
            }
        }
        LOGGER.debug("Found paymentsides:{}", paymentSides);
        return paymentSides;
    }
    
    public List<PaymentSideDTO> findById(final Set<String> ids) {
        List<PaymentSideDTO> paymentSides = new ArrayList<PaymentSideDTO>();
        if (ids != null && !ids.isEmpty()) {
            String query = GET_PAYMENT_SIDE_FOR_ID.replace(ID_REPLACEMENT,
                    mapIdToString(ids, paymentId -> paymentId));
            List<Map<String, Object>> queryResult = dbHelper.executeSelectQuery(getDatabaseName(), query);
            for (Map<String, Object> result : queryResult) {
                paymentSides.add(mapPaymentSide(result));
            }
        }
        LOGGER.debug("Found paymentsides:{}", paymentSides);
        return paymentSides;
    }
    
    private PaymentSideDTO mapPaymentSide(Map<String, Object> result) {
        PaymentSideDTO paymentSide = new PaymentSideDTO();
        paymentSide.setId(getBigInteger(result.get("ID")));
        paymentSide.setPaymentId(getBigInteger(result.get("PAYMENT_ID")));
        paymentSide.setLegacyPaymentId(getBigInteger(result.get("LEGACY_PAYMENT_ID")));
        paymentSide.setDebitCreditCode(getString(result.get("DEBIT_CREDIT_CODE")));
        paymentSide.setAccountNumber(getBigInteger(result.get("ACCOUNT_NUMBER")));
        paymentSide.setCounterpartyAccountNumber(getBigInteger(result.get("COUNTERPARTY_ACCOUNT_NUMBER")));
        paymentSide.setPartyId(getLong(result.get("PARTY_ID")));
        paymentSide.setPrimaryPartyNameId(getLong(result.get("PRIMARY_PARTY_NAME_ID")));
        paymentSide.setPrimaryPartyPhoneId(getBigInteger(result.get("PRIMARY_PARTY_PHONE_ID")));
        paymentSide.setPrimaryPartyPostalAddrId(getBigInteger(result.get("PRIMARY_PARTY_POSTAL_ADDR_ID")));
        paymentSide.setTypeCode(getString(result.get(TYPE_CODE_COL)));
        paymentSide.setSubtypeCode(getString(result.get(SUBTYPE_CODE_COL)));
        paymentSide.setReasonCode(getString(result.get(REASON_CODE_COL)));
        paymentSide.setStatusCode(getString(result.get(STATUS_CODE_COL)));
        paymentSide.setMoneyAmount(getLong(result.get(MONEY_AMOUNT_COL)));
        paymentSide.setCurrencyCode(getString(result.get(CURRENCY_CODE_COL)));
        paymentSide.setUsdMoneyAmount(getLong(result.get(USD_MONEY_AMOUNT_COL)));
        paymentSide.setLegacyBatc(getLong(result.get("LEGACY_BATC")));
        paymentSide.setLegacyShippingAddrId(getLong(result.get("LEGACY_SHIPPING_ADDR_ID")));
        paymentSide.setInitiationTime(getLong(result.get(INITIATION_TIME_COL)));
        paymentSide.setLastUpdatedTime(getLong(result.get(LAST_UPDATED_TIME_COL)));
        paymentSide.setParentId(getBigInteger(result.get("PARENT_ID")));
        paymentSide.setUserMemo(getString(result.get("USER_MEMO")));
        paymentSide.setMessageId(getBigInteger(result.get("MESSAGE_ID")));
        paymentSide.setItemDetailIndicators(getBigInteger(result.get("ITEM_DETAIL_INDICATORS")));
        paymentSide.setItemDetailFlags(getBigInteger(result.get("ITEM_DETAIL_FLAGS")));
        paymentSide.setFlags01(getBigInteger(result.get(FLAGS01_COL)));
        paymentSide.setFlags02(getBigInteger(result.get(FLAGS02_COL)));
        paymentSide.setFlags03(getBigInteger(result.get(FLAGS03_COL)));
        paymentSide.setFlags04(getBigInteger(result.get(FLAGS04_COL)));
        paymentSide.setFlags05(getBigInteger(result.get(FLAGS05_COL)));
        paymentSide.setFlags06(getBigInteger(result.get(FLAGS06_COL)));
        paymentSide.setFlags07(getBigInteger(result.get(FLAGS07_COL)));
        paymentSide.setFlags08(getBigInteger(result.get(FLAGS08_COL)));
        paymentSide.setFlags09(getBigInteger(result.get(FLAGS09_COL)));
        paymentSide.setFlags10(getBigInteger(result.get(FLAGS10_COL)));
        paymentSide.setRowCreatedTime(getLong(result.get("ROW_CREATED_TIME")));
        paymentSide.setRowUpdatedTime(getLong(result.get("ROW_UPDATED_TIME")));
        paymentSide.setCounterpartyCredentialCode(getString(result.get("COUNTERPARTY_CREDENTIAL_CODE")));
        paymentSide.setCounterpartyCredentialId(getBigInteger(result.get("COUNTERPARTY_CREDENTIAL_ID")));
        paymentSide.setReasonDetailCode(getString(result.get("REASON_DETAIL_CODE")));
        paymentSide.setLastLoginIdentifier(getString(result.get("LAST_LOGIN_IDENTIFIER")));
        paymentSide.setLoginTypeCode(getString(result.get("LOGIN_TYPE_CODE")));
        paymentSide.setLegacySharedId(getBigInteger(result.get("LEGACY_SHARED_ID")));
        paymentSide.setFinancialJournalId(getBigInteger(result.get("FINANCIAL_JOURNAL_ID")));
        return paymentSide;
    }
}
