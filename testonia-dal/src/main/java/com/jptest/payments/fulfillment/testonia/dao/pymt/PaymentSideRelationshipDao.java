package com.jptest.payments.fulfillment.testonia.dao.pymt;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DAO for PAYMENT_SIDE_RELATIONSHIP table of PYMT (2.1) DB
 */
@Singleton
public class PaymentSideRelationshipDao extends PymtDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSideRelationshipDao.class);

    private static final String GET_RELATED_PYMT_SIDE_IDS_FROM_PYMT_SIDE_IDS = "select distinct a.related_payment_side_id, a.row_created_time "
            + "from payment_side_relationship a where a.payment_side_id in ({payment_side_id}) order by a.row_created_time asc";

    private static final String PYMT_SIDE_ID_REPLACEMENT_TOKEN = "{payment_side_id}";

    private static final String RELATED_PYMT_SIDE_COL = "RELATED_PAYMENT_SIDE_ID";
    
    /**
     * 
     * @param commaSeparatedIds
     * @return
     */
    public Set<BigInteger> getRelatedPymtSideIdsFromPymtSideIds(final String commaSeparatedIds) {
        final Set<BigInteger> resultSet = new LinkedHashSet<>();
        final String query = GET_RELATED_PYMT_SIDE_IDS_FROM_PYMT_SIDE_IDS.replace(PYMT_SIDE_ID_REPLACEMENT_TOKEN,
                commaSeparatedIds);
        final List<Map<String, Object>> queryResult = this.dbHelper.executeSelectQuery(this.getDatabaseName(), query);
        for (final Map<String, Object> result : queryResult) {
            final String relatedPymtSideId = (String) result.get(RELATED_PYMT_SIDE_COL);
            resultSet.add(new BigInteger(relatedPymtSideId));
            LOGGER.info("RELATED PYMT SIDE ID from DB {}", relatedPymtSideId);
        }
        LOGGER.info("Found related pymt set:{}", resultSet);
        return resultSet;
    }  
}
