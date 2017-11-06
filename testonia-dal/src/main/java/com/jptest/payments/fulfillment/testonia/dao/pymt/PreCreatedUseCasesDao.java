package com.jptest.payments.fulfillment.testonia.dao.pymt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.model.pymt.PreCreatedUseCasesDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.PreCreatedUseCasesDTO.UseCaseTypeEnum;


/**
 * Represents precreated_usecases table of PYMT database
 */
@Singleton
public class PreCreatedUseCasesDao extends PymtDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreCreatedUseCasesDao.class);

    private static final String GET_ID_FOR_USECASEID_NOTUSED = "select id from precreated_usecases  where usecase_id = '{usecase_id}' and is_used = 'N'order by created_ts desc";
    private static final String GET_PAYMENT_DETAILS_FOR_ID = "select * from precreated_usecases  where id = '{ID}' for update skip locked order by created_ts desc";
    private static final String INSERT_PAYMENT_DETAILS = "insert into precreated_usecases(id,usecase_id,transaction_id,buyer_account_number,seller_account_number,funder_account_number,usecase_type,created_ts,updated_ts,is_used) values (PRECREATED_USECASES_SEQ.NEXTVAL,'{usecase_id}','{transaction_id}','{buyer_account_number}','{seller_account_number}','{funder_account_number}','{usecase_type}',SYSDATE ,SYSDATE ,'N')";
    private static final String UPDATE_PRECREATED_USECASES = "update precreated_usecases set is_used = 'Y', updated_ts = SYSDATE where transaction_id = '{transaction_id}' and usecase_id ='{usecase_id}'";
    private static final String UPDATE_PRECREATED_USECASES_BY_ACCOUNT_NUMBER = "update precreated_usecases set is_used = 'Y', updated_ts = SYSDATE where usecase_id ='{usecase_id}' and ";
    private static final String USECASE_ID_REPLACEMENT = "{usecase_id}";
    private static final String TRANSACTION_ID_REPLACEMENT = "{transaction_id}";
    private static final String BUYER_ACCOUNT_NUMBER_REPLACEMENT = "{buyer_account_number}";
    private static final String SELLER_ACCOUNT_NUMBER_REPLACEMENT = "{seller_account_number}";
    private static final String FUNDER_ACCOUNT_NUMBER_REPLACEMENT = "{funder_account_number}";
    private static final String USECASE_TYPE_REPLACEMENT = "{usecase_type}";
    private static final String ID_REPLACEMENT = "{ID}";

    /**
     * Fetches transaction details from precreated_usecases table by usecaseId
     *
     * @param usecaseId
     * @return
     */
    public synchronized PreCreatedUseCasesDTO getTransactionDetails(final String usecaseId) {
        PreCreatedUseCasesDTO preCreatedUseCasesDTO = new PreCreatedUseCasesDTO();
        Map<String, Object> queryResult = new HashMap<String, Object>();
        if (usecaseId != null) {
            String query = GET_ID_FOR_USECASEID_NOTUSED.replace(USECASE_ID_REPLACEMENT, usecaseId);
            final List<Map<String, Object>> ids = this.dbHelper.executeSelectQueryUsingJDBC(
                    this.getDatabaseName(),
                    query);
            // TODO see if we can better optimize this..
            for (int i = 0; i < ids.size(); i++) {
                final String id = ids.get(i).get("ID").toString();
                query = GET_PAYMENT_DETAILS_FOR_ID.replace(ID_REPLACEMENT, id);
                queryResult = this.dbHelper.executeSelectQueryForSingleResult(
                        this.getDatabaseName(),
                        query);
                if (queryResult != null) {
                    break;
                }

            }
            if (queryResult != null) {
                preCreatedUseCasesDTO = this.mapPreCreatedUseCasesDTO(queryResult);
                // For Precreated transactions, we update is_used flag by transaction id
                if (preCreatedUseCasesDTO.getTransactionId() != null) {
                    this.updateIsUsed(preCreatedUseCasesDTO.getTransactionId().toString(),
                            preCreatedUseCasesDTO.getUsecaseId());
                }
                // Pre created users we update based on the account numbers
                else {
                    this.updateIsUsedByAccountNumber(preCreatedUseCasesDTO);
                }
            }

        }
        else {
            LOGGER.debug("UseCaseId is null");
        }
        return preCreatedUseCasesDTO;

    }

    /**
     * inserts payments details into precreated_usecases through PreCreatedUseCasesDTO
     *
     * @param preCreatedUseCasesDTO
     */
    public void insertrecords(final PreCreatedUseCasesDTO preCreatedUseCasesDTO) {

        final String query = INSERT_PAYMENT_DETAILS
                .replace(USECASE_ID_REPLACEMENT, preCreatedUseCasesDTO.getUsecaseId())
                .replace(TRANSACTION_ID_REPLACEMENT, preCreatedUseCasesDTO.getTransactionId().toString())
                .replace(BUYER_ACCOUNT_NUMBER_REPLACEMENT, preCreatedUseCasesDTO.getBuyerAccountNumber().toString())
                .replace(SELLER_ACCOUNT_NUMBER_REPLACEMENT, preCreatedUseCasesDTO.getSellerAccountNumber().toString())
                .replace(FUNDER_ACCOUNT_NUMBER_REPLACEMENT, preCreatedUseCasesDTO.getFunderAccountNumber() != null
                ? preCreatedUseCasesDTO.getFunderAccountNumber().toString() : "")
                .replace(USECASE_TYPE_REPLACEMENT, preCreatedUseCasesDTO.getUsecaseType().toString());

        this.dbHelper.executeUpdateQuery(this.getDatabaseName(), query);

    }

    /**
     * updates is_used to 'y'
     *
     * @param transactionId
     * @param usecaseId
     */
    public void updateIsUsed(final String transactionId, final String usecaseId) {
        final String updateQuery = UPDATE_PRECREATED_USECASES.replace(TRANSACTION_ID_REPLACEMENT, transactionId)
                .replace(USECASE_ID_REPLACEMENT, usecaseId);
        this.dbHelper.executeUpdateQuery(this.getDatabaseName(), updateQuery);
    }

    /**
     * updates is_used to 'y'
     *
     * @param transactionId
     * @param usecaseId
     */
    public void updateIsUsedByAccountNumber(final PreCreatedUseCasesDTO preCreatedUseCasesDTO) {
        String updateQuery = UPDATE_PRECREATED_USECASES_BY_ACCOUNT_NUMBER
                .replace(USECASE_ID_REPLACEMENT, preCreatedUseCasesDTO.getUsecaseId());

        if (preCreatedUseCasesDTO.getUsecaseType() != null
                && preCreatedUseCasesDTO.getUsecaseType().equals(UseCaseTypeEnum.SINGLE.getUseCaseType())) {
            updateQuery = updateQuery + " funder_account_number = " + preCreatedUseCasesDTO.getFunderAccountNumber();
        }
        else {
            updateQuery = updateQuery + " buyer_account_number = " + preCreatedUseCasesDTO.getBuyerAccountNumber();
        }
        this.dbHelper.executeUpdateQuery(this.getDatabaseName(), updateQuery);
    }

    /**
     * Maps the ResultSet to PreCreatedUseCasesDTO
     *
     * @param result
     * @return
     */
    private PreCreatedUseCasesDTO mapPreCreatedUseCasesDTO(final Map<String, Object> result) {
        final PreCreatedUseCasesDTO preCreatedUseCasesDTO = new PreCreatedUseCasesDTO();
        if (result != null) {
            preCreatedUseCasesDTO.setId(this.getBigInteger(result.get("ID")));
            preCreatedUseCasesDTO.setUsecaseId(this.getString(result.get("USECASE_ID")));
            preCreatedUseCasesDTO.setTransactionId(this.getBigInteger(result.get("TRANSACTION_ID")));
            preCreatedUseCasesDTO.setBuyerAccountNumber(this.getBigInteger(result.get("BUYER_ACCOUNT_NUMBER")));
            preCreatedUseCasesDTO.setSellerAccountNumber(this.getBigInteger(result.get("SELLER_ACCOUNT_NUMBER")));
            if(result.get("FUNDER_ACCOUNT_NUMBER") != null)
                preCreatedUseCasesDTO.setFunderAccountNumber(this.getBigInteger(result.get("FUNDER_ACCOUNT_NUMBER")));
            preCreatedUseCasesDTO.setUsecaseType(this.getString(result.get("USECASE_TYPE")));
            preCreatedUseCasesDTO.setIsUsed(this.getChar(result.get("IS_USED")));
        }
        else {
            LOGGER.debug("User list exhausted");
        }
        return preCreatedUseCasesDTO;
    }

    /**
     * Since morethan 60 Days PreCreated Cross Currency transaction will have different currency conversion during
     * refund hence need to mark all morethan 60 Days PreCreated Cross Currency transaction as used
     *
     * @param usecaseId
     * @return
     */
    public synchronized void setOlderThan60DaysPreCreatedTransactionsToUsed(final String usecaseId) {
        if (usecaseId != null) {
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
            Date preCreatedTransactionsDate = null;
            Date currentDate = null;
            String query = GET_ID_FOR_USECASEID_NOTUSED.replace(USECASE_ID_REPLACEMENT, usecaseId);
            final List<Map<String, Object>> ids = this.dbHelper.executeSelectQueryUsingJDBC(this.getDatabaseName(),
                    query);
            for (int i = 0; i < ids.size(); i++) {
                final String id = ids.get(i).get("ID").toString();
                query = GET_PAYMENT_DETAILS_FOR_ID.replace(ID_REPLACEMENT, id);
                final Map<String, Object> queryResult = this.dbHelper
                        .executeSelectQueryForSingleResult(this.getDatabaseName(), query);
                if (queryResult != null) {
                    try {
                        currentDate = dateFormatter.parse(dateFormatter.format(new Date()));
                        preCreatedTransactionsDate = dateFormatter.parse(queryResult.get("CREATED_TS").toString());
                    }
                    catch (final ParseException e) {
                        throw new TestExecutionException(e);
                    }
                    final long diff = currentDate.getTime() - preCreatedTransactionsDate.getTime();
                    final long diffInDays = diff / (24 * 60 * 60 * 1000);
                    LOGGER.info("Difference in Days - {}", diffInDays);
                    if (diffInDays > 45) {
                        final PreCreatedUseCasesDTO preCreatedUseCasesDTO = this.mapPreCreatedUseCasesDTO(queryResult);
                        // For Precreated transactions, we update is_used flag by transaction id
                        if (preCreatedUseCasesDTO.getTransactionId() != null) {
                            this.updateIsUsed(preCreatedUseCasesDTO.getTransactionId().toString(),
                                    preCreatedUseCasesDTO.getUsecaseId());
                        }
                        // Pre created users we update based on the account numbers
                        else {
                            this.updateIsUsedByAccountNumber(preCreatedUseCasesDTO);
                        }
                    }
                }

            }
        }
        else {
            LOGGER.debug("UseCaseId is null");
        }
    }

    /**
     * Execute Custom SQL Query since most of the time might need to set some invalid transaction as used or not used.
     * This method will enhance the user to use their own custom query to update
     */
    public void updateIsUsed(final String sqlQuery) {
        this.dbHelper.executeUpdateQuery(this.getDatabaseName(), sqlQuery);
    }
}
